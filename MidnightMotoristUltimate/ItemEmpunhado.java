import greenfoot.*;

/** Elemento visual, nao coletavel, que acompanha o equipamento na mao. */
public class ItemEmpunhado extends Actor {
    private static final int LARGURA = 28;
    private String arquivoAtual = "";
    private boolean olhandoDireita = true;
    private GreenfootImage original;

    /** Comeca com uma imagem transparente ate receber um equipamento. */
    public ItemEmpunhado() {
        GreenfootImage transparente = new GreenfootImage(1, 1);
        setImage(transparente);
    }
    /** Carrega e ajusta a imagem do equipamento escolhido. */
    public void mostrar(String arquivo) {
        if (arquivo.equals(arquivoAtual)) return;
        original = new GreenfootImage(arquivo);
        int altura = Math.max(1, original.getHeight() * LARGURA / original.getWidth());
        original.scale(LARGURA, altura);
        arquivoAtual = arquivo;
        atualizarImagem();
    }
    /** Atualiza o lado para o qual o equipamento deve apontar. */
    public void atualizarDirecao(boolean direita) {
        if (direita == olhandoDireita) return;
        olhandoDireita = direita;
        atualizarImagem();
    }
    /** Aplica uma copia espelhada quando o personagem olha a esquerda. */
    private void atualizarImagem() {
        if (original == null) return;
        GreenfootImage img = new GreenfootImage(original);
        if (!olhandoDireita) img.mirrorHorizontally();
        setImage(img);
    }
}
