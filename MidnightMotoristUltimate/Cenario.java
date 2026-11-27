import greenfoot.*;

/**
 * Objeto do ambiente. O objeto do cenario define se a passagem por ali esta bloqueada,
 * o tamanho da area de colisao e se podem ser usados como saida.
 * os sprites em si nao determinam a colisao
 */
public class Cenario extends Actor {
    private String nome;
    private int id;
    private boolean interativo;
    private boolean bloqueia;
    private int meiaLargura;
    private int meiaAltura;
    private int deslocamentoY;

    // Cria um objeto de cenario com dimensoes padrao
    public Cenario(String nome, int id, boolean interativo, String imagem) {
        this(nome, id, interativo, !interativo, imagem, 65, 65, 25, 13, 0);
    }

    // configura imagem, interacao e caixa de colisao do objeto
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

    // testa caixas de colisao pequenas, independentes da transparencia do sprite
    public boolean bloqueiaPosicao(int x, int y, int raioX, int raioY) {
        if (!bloqueia) return false;
        int centroY = getY() + deslocamentoY;
        
        // valor absoluto para garantir valores não negativos
        return Math.abs(x - getX()) < meiaLargura + raioX
            && Math.abs(y - centroY) < meiaAltura + raioY;
    }

    // getter de nome
    public String getNome() { 
        return nome; 
    }
    
    // getter de identificador
    public int getId() { 
        return id; 
    }
    
    // informa se o obj é acessivel / interativo
    public boolean acessar() { 
        return interativo; 
    }
}
