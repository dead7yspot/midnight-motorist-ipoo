"""Prepara as imagens enviadas pelo grupo para as telas do Greenfoot.

Os arquivos originais da Area de Trabalho nao sao alterados. Os numeros
escritos nas imagens sao retirados para o jogo exibir o placar real.
Esta ferramenta de preparacao usa OpenCV e Pillow; o jogo nao depende deles.
"""

import sys
from pathlib import Path

TEMP = Path.home() / "AppData/Local/Temp/opencode"
sys.path.insert(0, str(TEMP / "midnight-image-tools"))
sys.path.insert(0, str(TEMP / "midnight-pillow"))

import cv2
import numpy as np
from PIL import Image, ImageEnhance, ImageFilter, ImageOps


RAIZ = Path(__file__).resolve().parent.parent
DESKTOP = Path.home() / "Desktop"
PREVIA = RAIZ / "ferramentas" / "previa_telas_cinematicas"
IMAGENS = RAIZ / "images"

# Cada retangulo cobre somente uma linha de texto fixo da imagem original.
TELAS = {
    "cinema_inicio.png": (
        "Tela Inicial nova.jpg",
        [(48, 642, 275, 680), (515, 642, 852, 678),
         (515, 680, 955, 710), (48, 707, 365, 747),
         (515, 707, 935, 747)],
    ),
    "cinema_vitoria1.png": (
        "MiniBoss Derrotado.jpg",
        [(76, 621, 550, 648), (76, 694, 535, 723)],
    ),
    "cinema_derrota1.png": (
        "Mafia venceu.jpg",
        [(76, 620, 455, 649), (76, 653, 382, 682), (76, 694, 512, 723)],
    ),
    "cinema_vitoria2.png": (
        "Venceu fuga.jpg",
        [(76, 621, 565, 648), (76, 694, 535, 723)],
    ),
    "cinema_vitoria3.png": (
        "Venceu Chefão.jpg",
        [(76, 620, 520, 649), (76, 654, 410, 682), (76, 694, 502, 723)],
    ),
    "cinema_derrota3.png": (
        "Chefão venceu.jpg",
        [],
    ),
}


def remover_texto_fixo(caminho, areas):
    """Retira os tracos das legendas fixas sem apagar o restante da cena."""
    original = cv2.imdecode(np.fromfile(str(caminho), dtype=np.uint8), cv2.IMREAD_COLOR)
    if original is None:
        raise ValueError("Nao foi possivel abrir a imagem: " + str(caminho))
    altura, largura = original.shape[:2]
    if (largura, altura) != (1376, 768):
        raise ValueError("A imagem possui tamanho inesperado: " + str(caminho))
    mascara = np.zeros((altura, largura), dtype=np.uint8)
    for x1, y1, x2, y2 in areas:
        trecho = original[y1:y2, x1:x2]
        minimo = trecho.min(axis=2)
        maximo = trecho.max(axis=2)
        cinza = cv2.cvtColor(trecho, cv2.COLOR_BGR2GRAY)
        letras = (cinza >= 115) & (minimo >= 85) & ((maximo - minimo) <= 115)
        mascara[y1:y2, x1:x2] = np.where(letras, 255, 0).astype(np.uint8)
    if areas:
        # Inclui o contorno escuro e o serrilhado das letras, nao so o branco.
        mascara = cv2.dilate(mascara, np.ones((7, 7), dtype=np.uint8), iterations=1)
        original = cv2.inpaint(original, mascara, 4, cv2.INPAINT_TELEA)
    rgb = cv2.cvtColor(original, cv2.COLOR_BGR2RGB)
    return Image.fromarray(rgb)


def preparar_derrota_policia(caminho):
    """Retira o rodape fixo e adapta a imagem vertical sem deformar os personagens."""
    with Image.open(caminho) as original:
        foto = original.convert("RGB")
    if foto.size != (1104, 974):
        raise ValueError("A imagem da perseguicao possui tamanho inesperado: " + str(caminho))
    # A area inferior preta da imagem original continha numeros de exemplo.
    cena = foto.crop((0, 0, 1104, 778))
    fundo = ImageOps.fit(cena, (1024, 572), method=Image.Resampling.LANCZOS)
    fundo = ImageEnhance.Brightness(fundo.filter(ImageFilter.GaussianBlur(24))).enhance(0.45)
    centro = ImageOps.contain(cena, (1024, 572), method=Image.Resampling.LANCZOS)
    fundo.paste(centro, ((1024 - centro.width) // 2, (572 - centro.height) // 2))
    return fundo


def main():
    """Gera copias preparadas e so instala novos arquivos quando solicitado."""
    PREVIA.mkdir(parents=True, exist_ok=True)
    nomes = list(TELAS) + ["cinema_derrota2.png"]
    if "--atualizar" in sys.argv:
        # So atualiza o que ainda corresponde a previa produzida anteriormente.
        for nome in nomes:
            anterior = PREVIA / nome
            instalado = IMAGENS / nome
            if not anterior.is_file() or not instalado.is_file() or anterior.read_bytes() != instalado.read_bytes():
                raise FileExistsError("Arquivo alterado ou ausente; nao vou sobrescrever: " + str(instalado))
    prontas = {}
    for destino, (original, areas) in TELAS.items():
        imagem = remover_texto_fixo(DESKTOP / original, areas)
        prontas[destino] = ImageOps.fit(imagem, (1024, 572), method=Image.Resampling.LANCZOS)
    prontas["cinema_derrota2.png"] = preparar_derrota_policia(DESKTOP / "Policia venceu.jpg")
    for nome, imagem in prontas.items():
        imagem.save(PREVIA / nome, optimize=True)
    print("Previa:", PREVIA)
    if "--instalar" in sys.argv:
        existentes = [IMAGENS / nome for nome in prontas if (IMAGENS / nome).exists()]
        if existentes:
            raise FileExistsError("Nao vou sobrescrever arquivos existentes: " + str(existentes))
        for nome, imagem in prontas.items():
            imagem.save(IMAGENS / nome, optimize=True)
        print("Telas copiadas para:", IMAGENS)
    elif "--atualizar" in sys.argv:
        for nome, imagem in prontas.items():
            imagem.save(IMAGENS / nome, optimize=True)
        print("Telas preparadas atualizadas em:", IMAGENS)


if __name__ == "__main__":
    main()
