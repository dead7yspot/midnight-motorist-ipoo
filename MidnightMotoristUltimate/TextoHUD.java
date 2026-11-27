import greenfoot.*;

/** 
 * Classe responsavel por criar textos. Os textos são criados como imagem para melhor visualização
 * 
 */
public class TextoHUD extends Actor {
    private final int tamanho;
    private final Color cor;
    private String textoAnterior;

    // Prepara uma etiqueta com tamanho e cor definidos.
    public TextoHUD(int tamanho, Color cor) {
        this.tamanho = tamanho;
        this.cor = cor;
        textoAnterior = null;
        definirTexto("");
    }

    // Altera a imagem apenas quando o texto exibido muda
    public void definirTexto(String texto) {
        if (texto == null) texto = "";
        if (texto.equals(textoAnterior)) return;
        textoAnterior = texto;
        if (texto.length() == 0) setImage(new GreenfootImage(1, 1));
        else setImage(new GreenfootImage(texto, tamanho, cor, null));
    }
}
