"""Prepara uma previa com as folhas de personagens fornecidas pelo grupo.

Este arquivo e uma ferramenta de preparacao, nao faz parte da execucao do jogo.
Ele espera encontrar o ZIP original na pasta Downloads e usa Pillow localmente.
"""

import io
import sys
import zipfile
from pathlib import Path

try:
    from PIL import Image, ImageDraw
except ImportError:
    pasta = Path.home() / "AppData/Local/Temp/opencode/midnight-pillow"
    sys.path.insert(0, str(pasta))
    from PIL import Image, ImageDraw


RAIZ = Path(__file__).resolve().parent.parent
PREVIA = RAIZ / "ferramentas" / "previa_sprites_enviados"
PACK = next((Path.home() / "Downloads").glob("gangster-pixel-character*zip"))

PALETA_HEROI = {
    (180, 117, 56): (50, 36, 32),
    (214, 168, 81): (84, 57, 45),
    (45, 27, 30): (87, 24, 30),
    (79, 52, 47): (157, 38, 40),
    (115, 23, 45): (193, 49, 51),
    (125, 56, 51): (210, 75, 61),
    (59, 23, 37): (67, 22, 30),
}
PALETA_VIGIA = {
    (203, 198, 193): (91, 113, 151),
    (70, 71, 98): (53, 76, 123),
    (36, 34, 52): (27, 39, 64),
}
PALETA_CHEFE = {
    (97, 39, 33): (111, 109, 103),
    (103, 57, 49): (156, 151, 137),
    (189, 106, 98): (214, 205, 183),
    (59, 32, 39): (68, 67, 68),
}


def abrir_quadro(arquivo, indice, paleta=None):
    """Recorta um quadro e aplica uma paleta consistente entre as poses."""
    with zipfile.ZipFile(PACK) as zipado:
        folha = Image.open(io.BytesIO(zipado.read(arquivo))).convert("RGBA")
    quadro = folha.crop((indice * 128, 0, (indice + 1) * 128, 128))
    if paleta:
        pixels = quadro.load()
        for y in range(128):
            for x in range(128):
                r, g, b, a = pixels[x, y]
                substituto = paleta.get((r, g, b))
                if a and substituto:
                    pixels[x, y] = (*substituto, a)
    return quadro


def adaptar_quadro(quadro):
    """Ajusta a pose a uma area 80x80, mantendo os pes na altura do jogo."""
    trecho = quadro.crop((14, 48, 114, 128))
    trecho = trecho.resize((80, 64), Image.Resampling.NEAREST)
    resultado = Image.new("RGBA", (80, 80))
    resultado.alpha_composite(trecho, (0, 0))
    return resultado


def quadro_heroi(arquivo, indice, acao=None):
    """Usa o mesmo personagem para todas as poses e acrescenta faca ou granada."""
    quadro = abrir_quadro(arquivo, indice, PALETA_HEROI)
    lapis = ImageDraw.Draw(quadro)
    if acao == "faca":
        lapis.line([(73, 73), (78, 71)], fill=(30, 25, 28, 255), width=3)
        lapis.polygon([(77, 70), (89, 64), (84, 71), (79, 74)],
                      fill=(207, 218, 218, 255), outline=(31, 32, 40, 255))
        lapis.line([(79, 70), (85, 67)], fill=(255, 250, 226, 255), width=1)
    elif acao == "granada":
        lapis.ellipse((73, 68, 83, 78), fill=(28, 36, 25, 255))
        lapis.ellipse((74, 69, 81, 76), fill=(95, 127, 58, 255))
        lapis.rectangle((76, 66, 79, 69), fill=(172, 180, 143, 255))
        lapis.point((76, 70), fill=(211, 219, 145, 255))
    return adaptar_quadro(quadro)


def quadro_chefe(arquivo, indice, disparo=False):
    """Diferencia o chefao do minichefe e destaca o cano da shotgun."""
    quadro = abrir_quadro(arquivo, indice, PALETA_CHEFE)
    if disparo:
        lapis = ImageDraw.Draw(quadro)
        lapis.rectangle((68, 87, 88, 90), fill=(30, 31, 34, 255))
        lapis.rectangle((72, 87, 87, 88), fill=(123, 132, 132, 255))
        lapis.rectangle((68, 91, 76, 92), fill=(106, 63, 38, 255))
        lapis.polygon([(89, 86), (96, 83), (94, 88), (98, 90),
                       (90, 91)], fill=(255, 166, 42, 255))
    return adaptar_quadro(quadro)


def desenhar_tanque(passo=0, disparando=False):
    """Desenha um tanque lateral novo, com esteiras e canhao independentes."""
    imagem = Image.new("RGBA", (85, 55))
    d = ImageDraw.Draw(imagem)
    sombra = (15, 21, 24, 255)
    escuro = (36, 45, 42, 255)
    medio = (73, 87, 70, 255)
    claro = (113, 130, 101, 255)
    luz = (157, 168, 126, 255)
    d.polygon([(7, 33), (12, 29), (67, 29), (75, 33), (73, 45),
               (66, 49), (16, 49), (8, 45), (5, 38)], fill=sombra)
    d.polygon([(9, 34), (15, 31), (65, 31), (72, 34), (71, 42),
               (65, 47), (16, 47), (10, 43)], fill=(47, 52, 49, 255))
    for x in range(17, 67, 10):
        d.ellipse((x - 5, 34, x + 5, 44), fill=(17, 23, 24, 255),
                  outline=(105, 111, 92, 255), width=1)
        d.ellipse((x - 2, 37, x + 2, 41), fill=(84, 94, 78, 255))
        if passo == 1:
            d.point((x - 2, 36), fill=(165, 174, 138, 255))
        elif passo == 2:
            d.point((x + 2, 42), fill=(165, 174, 138, 255))
    d.line([(16 + passo * 2, 46), (28 + passo * 2, 46)], fill=luz, width=1)
    d.line([(38 + passo * 2, 46), (50 + passo * 2, 46)], fill=luz, width=1)
    d.polygon([(10, 31), (14, 23), (24, 19), (60, 19), (70, 27),
               (75, 33), (71, 36), (11, 36)], fill=escuro, outline=sombra)
    d.polygon([(14, 29), (18, 24), (28, 22), (57, 22), (67, 28),
               (70, 32), (13, 32)], fill=medio)
    d.polygon([(17, 25), (27, 21), (56, 21), (62, 25), (53, 26),
               (25, 26)], fill=claro)
    d.line([(14, 32), (68, 32)], fill=luz, width=1)
    d.rectangle((15, 33, 24, 35), fill=(42, 52, 46, 255))
    d.rectangle((62, 32, 69, 35), fill=(39, 47, 42, 255))
    d.rectangle((66, 33, 69, 34), fill=(225, 191, 111, 255))
    d.polygon([(22, 22), (25, 13), (32, 10), (49, 10), (55, 14),
               (58, 22)], fill=escuro, outline=sombra)
    d.polygon([(25, 20), (28, 14), (34, 12), (48, 12), (53, 15),
               (55, 20)], fill=medio)
    d.polygon([(29, 14), (34, 12), (48, 12), (51, 14),
               (46, 15), (31, 15)], fill=claro)
    d.rectangle((34, 8, 46, 10), fill=sombra)
    d.rectangle((36, 7, 44, 8), fill=claro)
    d.rectangle((49, 15, 78, 19), fill=sombra)
    d.rectangle((50, 16, 76, 18), fill=medio)
    d.line([(52, 16), (75, 16)], fill=luz)
    d.rectangle((75, 15, 80, 20), fill=escuro, outline=sombra)
    d.rectangle((78, 16, 80, 19), fill=(17, 21, 21, 255))
    d.polygon([(38, 16), (39, 18), (42, 18), (40, 20), (41, 22),
               (38, 21), (36, 22), (36, 20), (34, 18), (37, 18)],
              fill=(178, 56, 44, 255))
    if disparando:
        d.polygon([(79, 16), (84, 12), (82, 17), (84, 19), (80, 20),
                   (83, 23), (78, 20)], fill=(255, 122, 31, 255))
        d.rectangle((78, 17, 83, 19), fill=(255, 221, 100, 255))
    return imagem.resize((170, 110), Image.Resampling.NEAREST)


def montar_sprites():
    """Reune os quadros usados nas acoes solicitadas para cada personagem."""
    sprites = {}
    sprites["jogador_parado"] = quadro_heroi("Gangsters_3/Idle.png", 3)
    sprites["jogador_andar1"] = quadro_heroi("Gangsters_3/Run.png", 2)
    sprites["jogador_andar2"] = quadro_heroi("Gangsters_3/Run.png", 7)
    sprites["jogador_andar3"] = quadro_heroi("Gangsters_3/Run.png", 0)
    sprites["jogador_andar4"] = quadro_heroi("Gangsters_3/Run.png", 5)
    sprites["jogador_faca"] = quadro_heroi("Gangsters_3/Attack.png", 2, "faca")
    sprites["jogador_tiro"] = quadro_heroi("Gangsters_3/Shot.png", 8)
    sprites["jogador_granada"] = quadro_heroi("Gangsters_3/Attack.png", 2, "granada")
    for grupo, paleta in (("capanga", None), ("vigia", PALETA_VIGIA)):
        sprites[grupo + "_parado"] = adaptar_quadro(abrir_quadro("Gangsters_2/Idle.png", 3, paleta))
        sprites[grupo + "_andar1"] = adaptar_quadro(abrir_quadro("Gangsters_2/Run.png", 2, paleta))
        sprites[grupo + "_andar2"] = adaptar_quadro(abrir_quadro("Gangsters_2/Run.png", 7, paleta))
        sprites[grupo + "_andar3"] = adaptar_quadro(abrir_quadro("Gangsters_2/Run.png", 0, paleta))
        sprites[grupo + "_andar4"] = adaptar_quadro(abrir_quadro("Gangsters_2/Run.png", 5, paleta))
        sprites[grupo + "_soco"] = adaptar_quadro(abrir_quadro("Gangsters_2/Attack_1.png", 3, paleta))
    for pose, fonte, quadro in (
        ("parado", "Gangsters_1/Idle.png", 3),
        ("andar1", "Gangsters_1/Run.png", 2),
        ("andar2", "Gangsters_1/Run.png", 7),
        ("andar3", "Gangsters_1/Run.png", 0),
        ("andar4", "Gangsters_1/Run.png", 5),
        ("tiro", "Gangsters_1/Shot.png", 0),
    ):
        quadro_elite = 2 if pose == "tiro" else quadro
        sprites["elite_" + pose] = adaptar_quadro(abrir_quadro(fonte, quadro_elite))
        sprites["chefe_" + pose] = quadro_chefe(fonte, quadro, pose == "tiro")
    sprites["tanque_parado"] = desenhar_tanque()
    sprites["tanque_andar1"] = desenhar_tanque(1)
    sprites["tanque_andar2"] = desenhar_tanque(2)
    sprites["tanque_tiro"] = desenhar_tanque(0, True)
    return sprites


def criar_contato(sprites, destino):
    """Mostra lado a lado as poses que serao usadas no jogo."""
    filas = [
        ("PROTAGONISTA", ("jogador_parado", "jogador_andar1", "jogador_andar2",
                         "jogador_faca", "jogador_tiro", "jogador_granada")),
        ("CAPANGAS", ("capanga_parado", "capanga_andar1", "capanga_andar2", "capanga_soco",
                      "vigia_parado", "vigia_soco")),
        ("MINICHEFE", ("elite_parado", "elite_andar1", "elite_andar2", "elite_tiro")),
        ("CHEFAO", ("chefe_parado", "chefe_andar1", "chefe_andar2", "chefe_tiro")),
        ("TANQUE", ("tanque_parado", "tanque_andar1", "tanque_andar2", "tanque_tiro")),
    ]
    folha = Image.new("RGBA", (1020, 900), (25, 30, 39, 255))
    lapis = ImageDraw.Draw(folha)
    for indice, (titulo, nomes) in enumerate(filas):
        y = indice * 180
        lapis.text((12, y + 7), titulo, fill=(226, 228, 219, 255))
        for coluna, nome in enumerate(nomes):
            sprite = sprites[nome]
            limite = (150, 145)
            copia = sprite.copy()
            copia.thumbnail(limite, Image.Resampling.NEAREST)
            if sprite.width <= 80:
                copia = sprite.resize((128, 128), Image.Resampling.NEAREST)
            x = coluna * 166 + 10
            folha.alpha_composite(copia, (x, y + 30))
    folha.convert("RGB").save(destino)


def main():
    """Gera a previa e, com --instalar, copia os quadros para o projeto."""
    PREVIA.mkdir(parents=True, exist_ok=True)
    sprites = montar_sprites()
    for nome, sprite in sprites.items():
        sprite.save(PREVIA / ("novo_" + nome + ".png"))
    criar_contato(sprites, PREVIA / "poses_enviadas.png")
    print("Previa gerada em:", PREVIA)
    if "--instalar" in sys.argv:
        imagens = RAIZ / "images"
        destinos = [imagens / ("novo_" + nome + ".png") for nome in sprites]
        existentes = [arquivo for arquivo in destinos if arquivo.exists()]
        for arquivo in existentes:
            nome = arquivo.stem[5:]
            dados = io.BytesIO()
            sprites[nome].save(dados, format="PNG")
            if arquivo.read_bytes() != dados.getvalue():
                raise FileExistsError("Nao vou sobrescrever arquivo diferente: " + str(arquivo))
        for nome, sprite in sprites.items():
            destino = imagens / ("novo_" + nome + ".png")
            if not destino.exists():
                sprite.save(destino)
        print("Sprites copiados para:", imagens)


if __name__ == "__main__":
    main()
