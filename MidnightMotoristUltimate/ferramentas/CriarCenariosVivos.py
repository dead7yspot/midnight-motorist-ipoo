"""Enriquece os cenarios existentes sem alterar as areas de colisao."""

import colorsys
import random
import sys
from pathlib import Path

try:
    from PIL import Image, ImageDraw, ImageEnhance, ImageFilter, ImageFont
except ImportError:
    sys.path.insert(0, str(Path.home() / "AppData/Local/Temp/opencode/midnight-pillow"))
    from PIL import Image, ImageDraw, ImageEnhance, ImageFilter, ImageFont


RAIZ = Path(__file__).resolve().parent.parent
IMAGENS = RAIZ / "images"
PREVIA = RAIZ / "ferramentas" / "previa_cenarios_vivos"


def carregar(nome):
    """Abre uma copia da arte existente e realca discretamente suas cores."""
    imagem = Image.open(IMAGENS / nome).convert("RGB")
    imagem = ImageEnhance.Color(imagem).enhance(1.18)
    imagem = ImageEnhance.Brightness(imagem).enhance(1.07)
    return imagem.convert("RGBA")


def fonte(tamanho):
    """Usa uma fonte local para os letreiros pequenos."""
    caminho = Path("C:/Windows/Fonts/consolab.ttf")
    return ImageFont.truetype(str(caminho), tamanho) if caminho.exists() else ImageFont.load_default()


def brilho(imagem, pontos):
    """Acrescenta um brilho difuso atras de lampadas e letreiros."""
    camada = Image.new("RGBA", imagem.size)
    lapis = ImageDraw.Draw(camada)
    for caixa, cor in pontos:
        lapis.rectangle(caixa, fill=cor)
    camada = camada.filter(ImageFilter.GaussianBlur(7))
    return Image.alpha_composite(imagem, camada)


def reflexos(imagem, pontos, semente):
    """Espalha pequenos reflexos coloridos no asfalto molhado."""
    camada = Image.new("RGBA", imagem.size)
    lapis = ImageDraw.Draw(camada)
    sorteio = random.Random(semente)
    for centro, cor in pontos:
        for i in range(24):
            y = sorteio.randrange(218, 348)
            espalhamento = 12 + (y - 218) // 5
            x = centro + sorteio.randrange(-espalhamento, espalhamento + 1)
            largura = sorteio.randrange(5, 27)
            lapis.rectangle((x, y, x + largura, y + 1), fill=cor + (sorteio.randrange(28, 70),))
    return Image.alpha_composite(imagem, camada)


def fase_um():
    """Da cor as fachadas e preserva a rua e os obstaculos da primeira fase."""
    imagem = carregar("noir_fase1.png")
    camada = Image.new("RGBA", imagem.size)
    pintura = ImageDraw.Draw(camada)
    fachadas = [
        (1, 194, (153, 76, 101)),
        (199, 411, (168, 124, 74)),
        (417, 652, (126, 85, 153)),
        (658, 840, (64, 144, 148)),
        (846, 1023, (155, 132, 69)),
    ]
    for x1, x2, tom in fachadas:
        pintura.rectangle((x1, 4, x2, 146), fill=tom + (25,))
        pintura.rectangle((x1 + 7, 100, x2 - 8, 138), fill=tom + (23,))
    imagem = Image.alpha_composite(imagem, camada)
    lojas = [
        (30, 147, "HOTEL", (234, 127, 149)),
        (246, 363, "CAFE", (249, 196, 102)),
        (486, 614, "LICOR", (207, 145, 223)),
        (709, 826, "BAR", (103, 213, 202)),
        (890, 998, "TAXI", (241, 194, 89)),
    ]
    pontos = []
    for x1, x2, nome, cor in lojas:
        pontos.append(((x1, 72, x2, 96), cor + (100,)))
    imagem = brilho(imagem, pontos)
    d = ImageDraw.Draw(imagem)
    for x1, x2, nome, cor in lojas:
        d.rectangle((x1, 72, x2, 94), fill=(22, 30, 40, 255), outline=cor + (255,), width=2)
        d.line((x1 + 4, 91, x2 - 4, 91), fill=cor + (170,), width=1)
        d.text((x1 + 9, 76), nome, font=fonte(12), fill=cor + (255,))
        d.line((x1, 96, x2, 96), fill=(174, 170, 151, 255), width=2)
        d.line((x1 + 8, 106, x1 + 26, 106), fill=cor + (150,), width=2)
        d.rectangle((x2 - 28, 116, x2 - 18, 128), fill=(38, 53, 59, 255), outline=cor + (180,))
        d.point((x2 - 23, 121), fill=(244, 222, 165, 255))
    # Toldos e vitrines distinguem as lojas, sem acrescentar barreiras.
    for inicio, fim, escuro, claro in (
        (247, 362, (122, 65, 54, 255), (227, 183, 128, 255)),
        (710, 825, (41, 101, 111, 255), (133, 197, 184, 255)),
    ):
        d.rectangle((inicio, 96, fim, 102), fill=escuro)
        for x in range(inicio + 5, fim, 18):
            d.polygon([(x, 96), (x + 8, 96), (x + 4, 102), (x - 4, 102)], fill=claro)
        d.line((inicio, 103, fim, 103), fill=(35, 40, 45, 255), width=2)
    d.rectangle((258, 108, 270, 122), fill=(33, 45, 49, 255), outline=(222, 180, 110, 255))
    d.arc((261, 110, 269, 119), 180, 350, fill=(240, 198, 139, 255), width=1)
    d.line((260, 120, 269, 120), fill=(240, 198, 139, 255))
    d.rectangle((737, 109, 748, 124), fill=(30, 48, 54, 255), outline=(92, 195, 185, 255))
    d.line((742, 111, 742, 121), fill=(131, 231, 213, 255), width=1)
    d.line((739, 121, 745, 121), fill=(131, 231, 213, 255), width=1)
    for x in (150, 378, 633, 828, 1007):
        d.rectangle((x, 135, x + 10, 143), fill=(102, 65, 49, 255), outline=(30, 39, 36, 255))
        d.rectangle((x + 3, 129, x + 7, 135), fill=(69, 126, 80, 255))
        d.rectangle((x + 1, 131, x + 4, 134), fill=(106, 162, 95, 255))
        d.rectangle((x + 7, 128, x + 10, 132), fill=(91, 153, 87, 255))
    d.rectangle((914, 108, 975, 125), fill=(45, 50, 44, 255), outline=(216, 184, 103, 255))
    d.text((922, 110), "24 HORAS", font=fonte(10), fill=(248, 218, 143, 255))
    # Faixa de pedestres e bueiro sao desenhos no asfalto, nao obstaculos.
    camada = Image.new("RGBA", imagem.size)
    c = ImageDraw.Draw(camada)
    for y in range(235, 340, 22):
        c.rectangle((6, y, 58, y + 9), fill=(195, 201, 193, 72))
        c.line((6, y + 9, 58, y + 9), fill=(19, 27, 35, 100))
    c.ellipse((700, 322, 740, 338), fill=(20, 30, 38, 115), outline=(120, 136, 134, 150), width=2)
    for x in range(707, 738, 7):
        c.line((x, 326, x - 3, 334), fill=(132, 145, 141, 145))
    imagem = Image.alpha_composite(imagem, camada)
    imagem = reflexos(imagem, [(85, (234, 127, 149)), (305, (249, 196, 102)),
                               (550, (207, 145, 223)), (765, (103, 213, 202)),
                               (945, (241, 194, 89))], 81)
    return imagem


def fase_dois():
    """Ilumina a rodovia e seus arredores sem mudar as faixas jogaveis."""
    imagem = carregar("noir_fase2.png")
    postes = [95, 325, 565, 805, 985]
    pontos = []
    for x in postes:
        pontos.append(((x - 10, 81, x + 10, 91), (248, 192, 112, 160)))
    pontos.extend([((350, 50, 458, 76), (97, 183, 196, 95)),
                   ((707, 52, 792, 74), (237, 138, 91, 90))])
    imagem = brilho(imagem, pontos)
    d = ImageDraw.Draw(imagem)
    d.rectangle((349, 48, 459, 75), fill=(27, 49, 56, 255), outline=(106, 198, 194, 255), width=2)
    d.text((357, 54), "MIDNIGHT CITY", font=fonte(12), fill=(168, 227, 213, 255))
    d.rectangle((707, 50, 791, 74), fill=(57, 43, 41, 255), outline=(235, 154, 102, 255), width=2)
    d.text((718, 55), "MOTEL", font=fonte(12), fill=(250, 196, 130, 255))
    for x in postes:
        d.line((x, 86, x, 104), fill=(29, 38, 43, 255), width=3)
        d.line((x - 8, 85, x + 8, 85), fill=(33, 39, 40, 255), width=3)
        d.rectangle((x - 5, 86, x + 5, 88), fill=(255, 215, 145, 255))
    d.line((0, 101, 1023, 101), fill=(134, 147, 141, 255), width=2)
    d.line((0, 479, 1023, 479), fill=(205, 175, 117, 255), width=3)
    for x in range(22, 1024, 108):
        d.rectangle((x, 102, x + 11, 105), fill=(239, 192, 112, 255))
        d.rectangle((x + 42, 483, x + 60, 487), fill=(193, 210, 198, 255))
        d.rectangle((x + 42, 488, x + 60, 490), fill=(180, 86, 77, 255))
    return reflexos(imagem, [(285, (75, 125, 163)), (725, (135, 102, 153))], 82)


def fase_tres():
    """Reforca a identidade industrial do deposito da mafia."""
    imagem = carregar("noir_fase3.png")
    imagem = brilho(imagem, [((320, 117, 337, 128), (243, 106, 75, 145)),
                             ((686, 117, 703, 128), (243, 106, 75, 145)),
                             ((38, 64, 116, 112), (107, 166, 174, 70)),
                             ((907, 64, 986, 112), (107, 166, 174, 70))])
    d = ImageDraw.Draw(imagem)
    for x in (13, 177, 721, 887):
        d.line((x, 31, x, 198), fill=(74, 89, 90, 255), width=3)
        d.line((x + 3, 31, x + 3, 198), fill=(27, 37, 40, 255), width=2)
    d.line((0, 144, 1023, 144), fill=(93, 106, 103, 255), width=3)
    d.line((0, 149, 1023, 149), fill=(30, 39, 42, 255), width=2)
    for x in (320, 686):
        d.rectangle((x, 115, x + 17, 127), fill=(43, 37, 35, 255), outline=(183, 119, 82, 255))
        d.rectangle((x + 4, 118, x + 13, 124), fill=(232, 94, 67, 255))
    d.rectangle((434, 90, 589, 119), fill=(33, 37, 40, 255), outline=(201, 155, 101, 255), width=2)
    d.text((447, 98), "ACESSO RESTRITO", font=fonte(12), fill=(232, 194, 137, 255))
    d.rectangle((739, 84, 815, 115), fill=(38, 48, 51, 255), outline=(120, 191, 185, 255), width=2)
    d.text((746, 93), "CARGA 03", font=fonte(10), fill=(152, 215, 198, 255))
    for x in (194, 838):
        d.rectangle((x, 114, x + 20, 140), fill=(35, 46, 47, 255), outline=(182, 145, 87, 255), width=2)
        d.polygon([(x + 10, 118), (x + 16, 132), (x + 4, 132)],
                  fill=(222, 180, 88, 255), outline=(55, 47, 37, 255))
        d.line((x + 10, 122, x + 10, 128), fill=(51, 45, 38, 255), width=2)
        d.point((x + 10, 130), fill=(51, 45, 38, 255))
    d.rectangle((82, 119, 149, 136), fill=(36, 47, 48, 255), outline=(105, 156, 150, 255))
    d.text((90, 121), "ARMAZEM", font=fonte(10), fill=(134, 196, 181, 255))
    camada = Image.new("RGBA", imagem.size)
    c = ImageDraw.Draw(camada)
    for x, y in ((310, 250), (560, 390), (850, 260)):
        c.ellipse((x, y, x + 48, y + 12), fill=(27, 34, 38, 110), outline=(137, 131, 110, 95))
        c.line((x + 9, y + 5, x + 39, y + 5), fill=(145, 151, 136, 105), width=1)
    c.line((59, 503, 262, 503), fill=(214, 168, 94, 145), width=2)
    c.line((739, 503, 942, 503), fill=(214, 168, 94, 145), width=2)
    imagem = Image.alpha_composite(imagem, camada)
    return reflexos(imagem, [(330, (224, 100, 77)), (697, (224, 100, 77))], 83)


def marca_saida():
    """Desenha somente uma seta baixa no asfalto, em vez da porta."""
    imagem = Image.new("RGBA", (86, 104))
    d = ImageDraw.Draw(imagem)
    d.rounded_rectangle((8, 75, 81, 94), radius=3, fill=(19, 30, 35, 130))
    d.polygon([(20, 80), (48, 80), (48, 76), (68, 85), (48, 93), (48, 89),
               (20, 89)], fill=(221, 213, 149, 200), outline=(44, 54, 50, 190))
    d.line((21, 81, 47, 81), fill=(255, 244, 192, 170), width=1)
    return imagem


def cor_de_carro(matiz):
    """Recolore apenas a lataria do carro amarelo, preservando janelas e pneus."""
    imagem = Image.open(IMAGENS / "noir_carro_transito.png").convert("RGBA")
    pixels = imagem.load()
    for y in range(imagem.height):
        for x in range(imagem.width):
            r, g, b, alpha = pixels[x, y]
            h, s, v = colorsys.rgb_to_hsv(r / 255.0, g / 255.0, b / 255.0)
            if alpha and 0.06 < h < 0.18 and s > 0.43 and v > 0.30:
                novo_r, novo_g, novo_b = colorsys.hsv_to_rgb(matiz, min(0.78, s), v)
                pixels[x, y] = (int(novo_r * 255), int(novo_g * 255), int(novo_b * 255), alpha)
    return imagem


def main():
    """Salva uma previa e instala somente arquivos novos quando solicitado."""
    PREVIA.mkdir(parents=True, exist_ok=True)
    imagens = {
        "cidade_viva_fase1.png": fase_um(),
        "cidade_viva_fase2.png": fase_dois(),
        "cidade_viva_fase3.png": fase_tres(),
        "saida_chao.png": marca_saida(),
        "transito_azul.png": cor_de_carro(0.59),
        "transito_verde.png": cor_de_carro(0.41),
        "transito_roxo.png": cor_de_carro(0.75),
    }
    for nome, imagem in imagens.items():
        imagem.save(PREVIA / nome)
    if "--instalar" in sys.argv:
        existentes = [IMAGENS / nome for nome in imagens if (IMAGENS / nome).exists()]
        if existentes:
            raise FileExistsError("Nao vou sobrescrever arquivos existentes: " + str(existentes))
        for nome, imagem in imagens.items():
            imagem.save(IMAGENS / nome)
        print("Cenarios instalados em:", IMAGENS)
    print("Previa criada em:", PREVIA)


if __name__ == "__main__":
    main()
