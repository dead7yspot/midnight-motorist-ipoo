import greenfoot.*;

/**
 * Objeto do ambiente. O proprio cenario define se bloqueia a passagem,
 * o tamanho da area de colisao e se pode ser usado como saida.
 * As imagens nao determinam a colisao: somente a base do objeto o faz.
 */
public class Cenario extends Actor {
    private String nome;
    private int id;
    private boolean interativo;
    private boolean bloqueia;
    private int meiaLargura;
    private int meiaAltura;
    private int deslocamentoY;

    /** Cria um objeto de cenario com dimensoes padrao. */
    public Cenario(String nome, int id, boolean interativo, String imagem) {
        this(nome, id, interativo, !interativo, imagem, 65, 65, 25, 13, 0);
    }

    /** Configura imagem, interacao e caixa de colisao do objeto. */
    public Cenario(String nome, int id, boolean interativo, boolean bloqueia,
                   String imagem, int larguraImagem, int alturaImagem,
                   int meiaLargura, int meiaAltura, int deslocamentoY) {
        this.nome = nome;
        this.id = id;
        this.interativo = interativo;
        this.bloqueia = bloqueia;
        this.meiaLargura = meiaLargura;
        this.meiaAltura = meiaAltura;
        this.deslocamentoY = deslocamentoY;
        GreenfootImage img = new GreenfootImage(imagem);
        img.scale(larguraImagem, alturaImagem);
        setImage(img);
    }

    /** Testa caixas de colisao pequenas, independentes da transparencia do sprite. */
    public boolean bloqueiaPosicao(int x, int y, int raioX, int raioY) {
        if (!bloqueia) return false;
        int centroY = getY() + deslocamentoY;
        return Math.abs(x - getX()) < meiaLargura + raioX
            && Math.abs(y - centroY) < meiaAltura + raioY;
    }

    /** Consulta o nome do objeto de cenario. */
    public String getNome() { return nome; }
    /** Consulta o identificador do objeto. */
    public int getId() { return id; }
    /** Informa se o objeto pode ser acessado. */
    public boolean acessar() { return interativo; }
}
