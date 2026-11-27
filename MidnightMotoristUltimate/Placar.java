import greenfoot.*;

/** Apresentacao compacta dos pontos durante a partida. */
public class Placar extends Actor {
    private int pontos = -1;
    /** Comeca a exibicao do placar em zero. */
    public Placar() { definirPontos(0); }
    /** Acrescenta pontos ao numero exibido. */
    public void adicionarPontos(int valor) { definirPontos(pontos + valor); }
    /** Atualiza o texto quando o total muda. */
    public void definirPontos(int valor) {
        if (valor == pontos) return;
        pontos = valor;
        setImage(new GreenfootImage("PTS " + pontos, 18, Color.WHITE,
                                   new Color(15, 19, 24, 190)));
    }
    /** Consulta o total atualmente exibido. */
    public int getPontos() { return pontos; }
}
