"""Cria cenarios pixelados novos com as poses das folhas enviadas."""

import math
import random
import sys
from pathlib import Path

try:
    from PIL import Image, ImageDraw, ImageFont
except ImportError:
    sys.path.insert(0, str(Path.home() / "AppData/Local/Temp/opencode/midnight-pillow"))
    from PIL import Image, ImageDraw, ImageFont

from ImportarSpritesEnviados import abrir_quadro, adaptar_quadro, PALETA_HEROI, PALETA_CHEFE


PASTA = Path(__file__).resolve().parent / "previa_sprites_enviados"
LARGURA, ALTURA = 512, 286
BRANCO = (222, 224, 210, 255)


def fonte(tamanho):
    """Busca uma fonte simples para as placas desenhadas no cenario."""
    caminho = Path("C:/Windows/Fonts/consolab.ttf")
    return ImageFont.truetype(str(caminho), tamanho) if caminho.exists() else ImageFont.load_default()


def ceu(imagem, fase, vitoria):
    """Desenha ceu, lua e predios proprios para a cena."""
    d = ImageDraw.Draw(imagem)
    cores = {
        1: ((22, 27, 42), (72, 42, 55)),
        2: ((14, 30, 51), (34, 66, 81)),
        3: ((28, 25, 39), (80, 46, 42)),
    }
    inicio, fim = cores[fase]
    for y in range(160):
        t = y / 160
        cor = tuple(int(inicio[c] * (1 - t) + fim[c] * t) for c in range(3))
        d.line((0, y, 511, y), fill=cor + (255,))
    if not vitoria:
        d.rectangle((0, 0, 511, 159), fill=(55, 17, 21, 65))
    d.ellipse((418, 17, 463, 62), fill=(236, 192, 139, 130))
    d.ellipse((425, 22, 460, 57), fill=(244, 206, 154, 220))
    gerador = random.Random(fase * 712 + (0 if vitoria else 91))
    x = 0
    while x < 512:
        largura = gerador.randrange(22, 48)
        altura = gerador.randrange(35, 95)
        topo = 151 - altura
        d.rectangle((x, topo, x + largura - 2, 155), fill=(19, 26, 37, 255))
        d.line((x, topo, x + largura - 2, topo), fill=(70, 70, 78, 255), width=1)
        for px in range(x + 5, x + largura - 4, 9):
            for py in range(topo + 7, 145, 12):
                if gerador.randrange(3) != 0:
                    cor = (198, 146, 90, 140) if gerador.randrange(3) else (95, 145, 157, 150)
                    d.rectangle((px, py, px + 2, py + 4), fill=cor)
        x += largura


def rua_mafia(imagem, vitoria):
    """Desenha fachada, toldo, postes e asfalto do territorio da mafia."""
    d = ImageDraw.Draw(imagem)
    d.rectangle((232, 61, 510, 159), fill=(62, 43, 51, 255), outline=(21, 22, 31, 255), width=3)
    for y in range(65, 157, 8):
        d.line((234, y, 508, y), fill=(89, 58, 61, 255))
        for x in range(237 + ((y // 8) % 2) * 8, 508, 16):
            d.line((x, y, x, y + 6), fill=(72, 49, 52, 255))
    d.rectangle((300, 83, 390, 156), fill=(14, 20, 28, 255), outline=(122, 89, 81, 255), width=3)
    d.rectangle((307, 91, 348, 151), fill=(35, 55, 62, 255), outline=(133, 109, 93, 255))
    d.line((328, 92, 328, 151), fill=(147, 114, 90, 255), width=2)
    d.rectangle((362, 91, 383, 155), fill=(35, 30, 35, 255), outline=(166, 129, 99, 255))
    d.rectangle((285, 62, 402, 82), fill=(25, 21, 30, 255), outline=(224, 105, 88, 255), width=2)
    d.text((294, 66), "MAFIA CLUB", font=fonte(12), fill=(250, 175, 123, 255))
    d.polygon([(300, 84), (389, 84), (399, 94), (292, 94)],
              fill=(125, 51, 51, 255), outline=(32, 25, 31, 255))
    for x in range(300, 395, 14):
        d.line((x, 86, x - 5, 93), fill=(215, 152, 124, 255), width=3)
    d.rectangle((0, 157, 511, 210), fill=(39, 45, 53, 255))
    d.rectangle((0, 157, 511, 161), fill=(124, 104, 96, 255))
    for x in range(200, 512, 53):
        d.line((x, 183, x + 24, 183), fill=(109, 100, 97, 255), width=2)
    d.line((440, 85, 440, 160), fill=(18, 21, 27, 255), width=4)
    d.polygon([(427, 83), (453, 83), (450, 89), (430, 89)], fill=(23, 25, 30, 255))
    d.ellipse((437, 85, 443, 91), fill=(247, 202, 133, 255))
    if not vitoria:
        for i in range(24):
            x = 190 + (i * 31) % 320
            y = 40 + (i * 19) % 125
            d.line((x, y, x - 4, y + 9), fill=(101, 133, 146, 160))


def pista(imagem, vitoria):
    """Desenha rodovia, guard rail e marcas de velocidade."""
    d = ImageDraw.Draw(imagem)
    d.rectangle((0, 137, 511, 210), fill=(35, 44, 53, 255))
    d.polygon([(0, 157), (511, 143), (511, 207), (0, 210)], fill=(44, 51, 58, 255))
    d.line((0, 143, 511, 133), fill=(161, 149, 122, 255), width=3)
    d.line((0, 150, 511, 140), fill=(94, 96, 92, 255), width=2)
    for x in range(15, 512, 27):
        d.line((x, 142, x, 155), fill=(88, 90, 88, 255), width=2)
    for x in range(145, 512, 75):
        d.line((x, 181, x + 43, 180), fill=(213, 203, 155, 220), width=2)
    for x, y, largura in ((270, 162, 40), (334, 169, 26), (447, 163, 39), (214, 198, 31)):
        d.line((x, y, x + largura, y), fill=(131, 148, 150, 180), width=1)
    d.rectangle((399, 66, 492, 103), fill=(28, 69, 67, 255), outline=(164, 201, 174, 255), width=2)
    d.text((410, 73), "SAIDA 03", font=fonte(11), fill=(221, 226, 206, 255))
    d.text((412, 85), "DOWNTOWN >", font=fonte(9), fill=(204, 219, 201, 255))
    d.line((432, 103, 432, 143), fill=(108, 116, 109, 255), width=3)
    if not vitoria:
        d.polygon([(0, 126), (92, 145), (0, 183)], fill=(77, 37, 61, 80))
        d.polygon([(511, 125), (422, 151), (511, 188)], fill=(37, 69, 104, 85))


def deposito(imagem, vitoria):
    """Desenha o deposito e as faixas industriais do confronto final."""
    d = ImageDraw.Draw(imagem)
    d.rectangle((200, 47, 511, 160), fill=(48, 50, 55, 255), outline=(18, 21, 25, 255), width=3)
    for x in range(204, 511, 42):
        d.rectangle((x, 51, x + 2, 157), fill=(78, 75, 68, 255))
    d.rectangle((280, 72, 452, 157), fill=(29, 34, 38, 255), outline=(132, 117, 98, 255), width=3)
    for y in range(80, 155, 7):
        d.line((285, y, 447, y), fill=(76, 78, 73, 255), width=2)
    d.rectangle((308, 52, 424, 67), fill=(32, 27, 27, 255), outline=(177, 91, 72, 255))
    d.text((318, 54), "DEPOSITO 03", font=fonte(10), fill=(219, 161, 127, 255))
    d.rectangle((0, 158, 511, 210), fill=(44, 43, 44, 255))
    d.rectangle((0, 157, 511, 162), fill=(194, 159, 93, 255))
    for x in range(0, 512, 16):
        d.polygon([(x, 157), (x + 7, 157), (x + 3, 162), (x - 4, 162)], fill=(32, 33, 33, 255))
    d.rectangle((450, 136, 483, 160), fill=(99, 67, 46, 255), outline=(31, 27, 25, 255), width=2)
    d.line((453, 140, 480, 157), fill=(157, 111, 71, 255), width=2)
    d.line((479, 140, 454, 157), fill=(157, 111, 71, 255), width=2)
    if vitoria:
        d.ellipse((367, 96, 444, 172), fill=(104, 57, 39, 130))
        d.ellipse((377, 110, 432, 165), fill=(237, 122, 40, 200))
        d.ellipse((386, 121, 422, 158), fill=(255, 206, 87, 230))


def veiculo(imagem, x, y, policia=False, tamanho=1.0):
    """Desenha um carro lateral e o aplica em tamanho apropriado a cena."""
    carro = Image.new("RGBA", (120, 48))
    d = ImageDraw.Draw(carro)
    escuro = (18, 21, 26, 255)
    cor = (193, 52, 54, 255) if not policia else (45, 71, 91, 255)
    luz = (239, 100, 83, 255) if not policia else (201, 211, 207, 255)
    d.polygon([(5, 22), (17, 18), (33, 7), (72, 7), (91, 18), (112, 21),
               (116, 30), (112, 38), (9, 38), (3, 32)], fill=escuro)
    d.polygon([(7, 23), (19, 19), (35, 9), (71, 9), (89, 19), (110, 23),
               (113, 30), (110, 34), (8, 34)], fill=cor)
    d.polygon([(37, 10), (51, 10), (51, 19), (24, 19)], fill=(73, 109, 124, 255), outline=escuro)
    d.polygon([(54, 10), (70, 10), (84, 19), (54, 19)], fill=(92, 128, 139, 255), outline=escuro)
    d.line((9, 24, 106, 24), fill=luz, width=2)
    d.rectangle((107, 25, 112, 28), fill=(255, 222, 150, 255))
    d.rectangle((6, 25, 10, 29), fill=(248, 113, 81, 255))
    for roda in (28, 91):
        d.ellipse((roda - 8, 28, roda + 8, 44), fill=escuro)
        d.ellipse((roda - 4, 32, roda + 4, 40), fill=(115, 128, 129, 255))
        d.ellipse((roda - 2, 34, roda + 2, 38), fill=(43, 49, 54, 255))
    if policia:
        d.rectangle((47, 3, 66, 7), fill=escuro)
        d.rectangle((49, 3, 55, 6), fill=(242, 64, 62, 255))
        d.rectangle((58, 3, 64, 6), fill=(80, 191, 255, 255))
        d.rectangle((48, 24, 76, 29), fill=(194, 211, 211, 255))
        d.text((51, 24), "POLICE", font=fonte(6), fill=(28, 43, 55, 255))
    if tamanho != 1.0:
        carro = carro.resize((int(120 * tamanho), int(48 * tamanho)), Image.Resampling.NEAREST)
    imagem.alpha_composite(carro, (int(x), int(y)))


def ator(imagem, sprite, centro, pe, escala, virar=False):
    """Cola o personagem pelo centro e pela altura dos pes, sem borramento."""
    if virar:
        sprite = sprite.transpose(Image.Transpose.FLIP_LEFT_RIGHT)
    caixa = sprite.getbbox()
    if not caixa:
        return
    sprite = sprite.crop(caixa)
    sprite = sprite.resize((max(1, int(sprite.width * escala)),
                            max(1, int(sprite.height * escala))), Image.Resampling.NEAREST)
    imagem.alpha_composite(sprite, (int(centro - sprite.width / 2), int(pe - sprite.height)))


def camadas(imagem, fase, vitoria, inicio=False):
    """Acrescenta os personagens e os efeitos que contam cada resultado."""
    sprites = {}
    for arquivo in PASTA.glob("novo_*.png"):
        sprites[arquivo.stem[5:]] = Image.open(arquivo).convert("RGBA")
    morto_heroi = adaptar_quadro(abrir_quadro("Gangsters_3/Dead.png", 4, PALETA_HEROI))
    morto_elite = adaptar_quadro(abrir_quadro("Gangsters_1/Dead.png", 4))
    morto_chefe = adaptar_quadro(abrir_quadro("Gangsters_1/Dead.png", 4, PALETA_CHEFE))
    d = ImageDraw.Draw(imagem)
    if inicio:
        veiculo(imagem, 324, 144, tamanho=1.35)
        ator(imagem, sprites["jogador_parado"], 296, 204, 2.65)
        d.line((263, 192, 294, 192), fill=(211, 108, 76, 255), width=2)
    elif fase == 1 and vitoria:
        ator(imagem, morto_elite, 414, 205, 2.2, True)
        ator(imagem, sprites["jogador_tiro"], 294, 204, 2.45)
        d.polygon([(381, 136), (393, 126), (388, 139), (397, 146),
                   (382, 144)], fill=(255, 169, 70, 220))
    elif fase == 1:
        ator(imagem, morto_heroi, 300, 205, 2.8)
        ator(imagem, sprites["elite_tiro"], 408, 204, 2.4, True)
        d.line((254, 198, 336, 198), fill=(150, 64, 61, 180), width=2)
    elif fase == 2 and vitoria:
        veiculo(imagem, 234, 156, policia=True, tamanho=0.85)
        veiculo(imagem, 345, 151, tamanho=1.18)
        ator(imagem, sprites["jogador_andar1"], 299, 205, 2.2)
        d.line((226, 173, 246, 173), fill=(238, 94, 85, 255), width=2)
        d.line((226, 179, 249, 179), fill=(92, 182, 232, 255), width=2)
    elif fase == 2:
        veiculo(imagem, 248, 161, tamanho=0.93)
        veiculo(imagem, 366, 157, policia=True, tamanho=1.05)
        ator(imagem, sprites["jogador_parado"], 326, 205, 2.35, True)
        ator(imagem, sprites["vigia_soco"], 431, 204, 1.85, True)
        d.line((399, 138, 399, 127), fill=(255, 100, 84, 255), width=3)
        d.line((416, 138, 416, 127), fill=(97, 191, 249, 255), width=3)
    elif fase == 3 and vitoria:
        ator(imagem, sprites["tanque_parado"], 409, 202, 1.1, True)
        ator(imagem, morto_chefe, 421, 206, 2.4)
        ator(imagem, sprites["jogador_granada"], 279, 205, 2.4)
        for x, y in ((356, 112), (367, 101), (384, 94), (421, 94), (438, 109), (446, 121)):
            d.rectangle((x, y, x + 3, y + 3), fill=(255, 196, 83, 255))
        for x, y, r in ((408, 90, 9), (416, 79, 8), (395, 80, 7)):
            d.ellipse((x-r, y-r, x+r, y+r), fill=(52, 48, 45, 210))
    else:
        ator(imagem, morto_heroi, 288, 205, 2.8)
        ator(imagem, sprites["tanque_tiro"], 406, 203, 1.1, True)
        ator(imagem, sprites["chefe_parado"], 448, 180, 1.5, True)
        d.polygon([(342, 159), (321, 151), (328, 160), (312, 165),
                   (333, 167), (328, 175), (344, 168)],
                  fill=(255, 142, 45, 220))


def criar_tela(fase, vitoria, inicio=False):
    """Monta uma ilustracao completa para uma situacao especifica."""
    imagem = Image.new("RGBA", (LARGURA, ALTURA))
    ceu(imagem, fase, vitoria)
    if fase == 1:
        rua_mafia(imagem, vitoria)
    elif fase == 2:
        pista(imagem, vitoria)
    else:
        deposito(imagem, vitoria)
    camadas(imagem, fase, vitoria, inicio)
    sombra = Image.new("RGBA", imagem.size)
    d = ImageDraw.Draw(sombra)
    for x in range(0, 260):
        opacidade = max(0, 225 - int(x * 0.8))
        d.line((x, 0, x, 209), fill=(8, 11, 19, opacidade))
    imagem = Image.alpha_composite(imagem, sombra)
    d = ImageDraw.Draw(imagem)
    destaque = (93, 213, 206, 255) if vitoria else (226, 88, 78, 255)
    d.rectangle((0, 0, 3, 209), fill=destaque)
    d.rectangle((0, 210, 511, 285), fill=(8, 12, 20, 245))
    d.rectangle((0, 210, 511, 211), fill=destaque)
    return imagem.resize((1024, 572), Image.Resampling.NEAREST).convert("RGB")


def main():
    """Gera as sete telas e, com --instalar, copia as imagens para o projeto."""
    PASTA.mkdir(parents=True, exist_ok=True)
    criar_tela(1, True, True).save(PASTA / "nova_tela_inicio.png")
    for fase in (1, 2, 3):
        for vitoria in (True, False):
            nome = "vitoria" if vitoria else "derrota"
            criar_tela(fase, vitoria).save(PASTA / ("nova_tela_" + nome + str(fase) + ".png"))
    print("Telas novas geradas em:", PASTA)
    if "--instalar" in sys.argv:
        imagens = PASTA.parent.parent / "images"
        arquivos = [PASTA / "nova_tela_inicio.png"]
        for fase in (1, 2, 3):
            for nome in ("vitoria", "derrota"):
                arquivos.append(PASTA / ("nova_tela_" + nome + str(fase) + ".png"))
        existentes = [imagens / arquivo.name for arquivo in arquivos if (imagens / arquivo.name).exists()]
        if existentes:
            raise FileExistsError("Nao vou sobrescrever arquivos existentes: " + str(existentes))
        for arquivo in arquivos:
            (imagens / arquivo.name).write_bytes(arquivo.read_bytes())
        print("Telas copiadas para:", imagens)


if __name__ == "__main__":
    main()
