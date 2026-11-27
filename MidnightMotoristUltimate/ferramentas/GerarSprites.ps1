# Gera as poses novas a partir das imagens que ja acompanhavam o jogo.
# Nao usa nem redistribui os arquivos dos pacotes externos de referencia.
Add-Type -AssemblyName System.Drawing
$pasta = Join-Path (Split-Path -Parent $PSScriptRoot) 'images'

function AbrirImagem($nome) {
    return [System.Drawing.Bitmap]::new((Join-Path $pasta $nome))
}

function SalvarImagem($imagem, $nome) {
    $imagem.Save((Join-Path $pasta $nome), [System.Drawing.Imaging.ImageFormat]::Png)
    $imagem.Dispose()
}

function Pincel($r, $g, $b) {
    return [System.Drawing.SolidBrush]::new([System.Drawing.Color]::FromArgb(255, $r, $g, $b))
}

# Mantem a mesma silhueta em todas as poses, mudando a cor da jaqueta.
function ColorirJaqueta($imagem, $tipo) {
    for ($y = 20; $y -lt 60; $y++) {
        for ($x = 0; $x -lt $imagem.Width; $x++) {
            $c = $imagem.GetPixel($x, $y)
            if ($c.A -gt 0 -and $c.R -gt 55 -and $c.R -gt $c.G * 2 -and $c.R -gt $c.B * 1.3) {
                if ($tipo -eq 'heroi') {
                    $r = [Math]::Min(255, $c.R + 15)
                    $g = $c.G
                    $b = $c.B
                } elseif ($tipo -eq 'capanga') {
                    $r = [Math]::Min(255, [int]($c.R * 0.57))
                    $g = [Math]::Min(255, [int]($c.G * 0.80))
                    $b = [Math]::Min(255, [int]($c.R * 0.75))
                } else {
                    $r = [Math]::Min(255, [int]($c.R * 0.35))
                    $g = [Math]::Min(255, [int]($c.R * 0.55))
                    $b = [Math]::Min(255, [int]($c.R * 0.95))
                }
                $imagem.SetPixel($x, $y, [System.Drawing.Color]::FromArgb($c.A, $r, $g, $b))
            }
        }
    }
}

$poses = @('parado', 'andar1', 'andar2', 'tiro')
foreach ($pose in $poses) {
    $imagem = AbrirImagem "noir_jogador_$pose.png"
    ColorirJaqueta $imagem 'heroi'
    SalvarImagem $imagem "mm_jogador_$pose.png"
}

foreach ($tipo in @('capanga', 'vigia')) {
    foreach ($pose in @('parado', 'andar1', 'andar2', 'golpe')) {
        $imagem = AbrirImagem "noir_jogador_$pose.png"
        ColorirJaqueta $imagem $tipo
        if ($pose -eq 'golpe') {
            # Retira o arco do golpe original: o capanga usa o proprio punho.
            for ($y = 0; $y -lt $imagem.Height; $y++) {
                for ($x = 66; $x -lt $imagem.Width; $x++) {
                    $imagem.SetPixel($x, $y, [System.Drawing.Color]::Transparent)
                }
            }
            SalvarImagem $imagem "mm_${tipo}_soco.png"
        } else {
            SalvarImagem $imagem "mm_${tipo}_$pose.png"
        }
    }
}

$imagem = AbrirImagem 'noir_jogador_golpe.png'
ColorirJaqueta $imagem 'heroi'
for ($y = 0; $y -lt $imagem.Height; $y++) {
    for ($x = 66; $x -lt $imagem.Width; $x++) {
        $imagem.SetPixel($x, $y, [System.Drawing.Color]::Transparent)
    }
}
$g = [System.Drawing.Graphics]::FromImage($imagem)
$escuro = Pincel 27 30 35
$metal = Pincel 205 215 220
$brilho = Pincel 245 246 225
$g.FillRectangle($escuro, 61, 30, 6, 3)
$g.FillPolygon($metal, [System.Drawing.Point[]]@([System.Drawing.Point]::new(65,29), [System.Drawing.Point]::new(79,23), [System.Drawing.Point]::new(73,31), [System.Drawing.Point]::new(65,32)))
$g.FillRectangle($brilho, 68, 29, 6, 1)
$g.Dispose()
SalvarImagem $imagem 'mm_jogador_faca.png'

$imagem = AbrirImagem 'noir_jogador_golpe.png'
ColorirJaqueta $imagem 'heroi'
for ($y = 0; $y -lt $imagem.Height; $y++) {
    for ($x = 66; $x -lt $imagem.Width; $x++) {
        $imagem.SetPixel($x, $y, [System.Drawing.Color]::Transparent)
    }
}
$g = [System.Drawing.Graphics]::FromImage($imagem)
$sombra = Pincel 28 39 25
$verde = Pincel 83 112 49
$luz = Pincel 162 171 83
$g.FillRectangle($sombra, 60, 23, 9, 9)
$g.FillRectangle($verde, 61, 24, 7, 7)
$g.FillRectangle($luz, 63, 25, 2, 2)
$g.FillRectangle($metal, 63, 21, 4, 2)
$g.Dispose()
SalvarImagem $imagem 'mm_jogador_granada.png'

$imagem = AbrirImagem 'noir_chefe_tiro.png'
for ($y = 0; $y -lt 24; $y++) {
    for ($x = 58; $x -lt 80; $x++) {
        $imagem.SetPixel($x, $y, [System.Drawing.Color]::Transparent)
    }
}
$g = [System.Drawing.Graphics]::FromImage($imagem)
$madeira = Pincel 119 64 30
$cano = Pincel 69 75 80
$g.FillRectangle($escuro, 53, 21, 24, 5)
$g.FillRectangle($madeira, 53, 22, 8, 3)
$g.FillRectangle($cano, 61, 22, 16, 2)
$g.FillRectangle($metal, 61, 21, 14, 1)
$g.FillRectangle($brilho, 77, 20, 2, 6)
$g.Dispose()
SalvarImagem $imagem 'mm_chefe_shotgun.png'

foreach ($passo in 1..2) {
    $imagem = AbrirImagem 'noir_tanque.png'
    $g = [System.Drawing.Graphics]::FromImage($imagem)
    $poeira = Pincel 125 118 88
    $offset = if ($passo -eq 1) { 0 } else { 7 }
    for ($x = 24 + $offset; $x -lt 85; $x += 14) {
        $g.FillRectangle($poeira, $x, 96, 8, 2)
    }
    $g.Dispose()
    SalvarImagem $imagem "mm_tanque_movimento$passo.png"
}

$imagem = AbrirImagem 'noir_tanque.png'
$g = [System.Drawing.Graphics]::FromImage($imagem)
$laranja = Pincel 245 112 25
$amarelo = Pincel 255 205 55
$g.FillPolygon($laranja, [System.Drawing.Point[]]@([System.Drawing.Point]::new(147,47), [System.Drawing.Point]::new(168,39), [System.Drawing.Point]::new(162,48), [System.Drawing.Point]::new(169,55), [System.Drawing.Point]::new(147,54)))
$g.FillRectangle($amarelo, 147, 48, 15, 5)
$g.FillRectangle($brilho, 147, 49, 9, 3)
$g.Dispose()
SalvarImagem $imagem 'mm_tanque_tiro.png'
