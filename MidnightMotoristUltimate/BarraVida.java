import greenfoot.*;

/** Indicador visual compacto; a vida permanece no Jogador. */
public class BarraVida extends Actor {
    private final int vidaMaxima;
    private int ultimaVida = -1;
    /** Cria a barra com o limite de vida do jogador. */
    public BarraVida(int vidaMaxima) {
        this.vidaMaxima = vidaMaxima;
        atualizar(vidaMaxima);
    }
    /** Redesenha a barra somente quando a vida muda. */
    public void atualizar(int vida) {
        if (vida == ultimaVida) return;
        ultimaVida = vida;
        GreenfootImage imagem = new GreenfootImage(150, 20);
        imagem.setColor(new Color(20, 24, 29, 230));
        imagem.fillRect(0, 0, 150, 20);
        imagem.setColor(new Color(75, 85, 74));
        imagem.fillRect(2, 2, 146, 16);
        int largura = Math.max(0, 146 * vida / Math.max(1, vidaMaxima));
        imagem.setColor(new Color(56, 180, 104));
        imagem.fillRect(2, 2, largura, 16);
        imagem.setColor(Color.WHITE);
        imagem.drawString("HP " + vida + "/" + vidaMaxima, 38, 15);
        setImage(imagem);
    }
}
