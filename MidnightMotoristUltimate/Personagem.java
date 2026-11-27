import greenfoot.*;
import java.util.List;

/**
 * Base de jogador e inimigos: vida, animacao, movimentacao e combate.
 * A colisao vem da versao V1: mover um eixo por vez, usando a base do pe.
 * Atores possuem sprites visuais, mas nao dependem do tamanho da imagem para colidir.
 */
public class Personagem extends Actor {
    private final String nome;
    private final int velocidade;
    private final int vidaMaxima;
    private int vida;
    private GreenfootImage parado, corridaA, corridaB;
    private GreenfootImage[] quadrosCorrida;
    private int quadroCorrida;
    private boolean olhandoDireita = true;
    private boolean frameA;
    private int tempoAnimacao;
    private int escalaVisual = 100;
    private String ultimaImagem = "";
    private ItemEmpunhado itemNaMao;

    /** 
     * Guarda os dados do personagem e carrega suas poses basicas. 
    */
    public Personagem(String nome, int vida, int velocidade,
                      String arquivoParado, String arquivoA, String arquivoB) {
        this.nome = nome;
        this.vida = vida;
        this.vidaMaxima = vida;
        this.velocidade = velocidade;
        parado = new GreenfootImage(arquivoParado);
        corridaA = new GreenfootImage(arquivoA);
        corridaB = new GreenfootImage(arquivoB);
        aplicarSprite(parado, "parado");
    }

    /** Move X e Y separadamente; cada eixo avanca em passos unitarios.
     *  Evita atravessar paredes em velocidades maiores e permite deslizar.
     */
    protected boolean moverComColisao(int dx, int dy) {
        if (getWorld() == null) return false;
        boolean mudou = false;
        int passoX = Integer.compare(dx, 0);
        int passoY = Integer.compare(dy, 0);
        for (int i = 0; i < Math.abs(dx); i++) {
            if (podeOcupar(getX() + passoX, getY())) {
                setLocation(getX() + passoX, getY());
                mudou = true;
            } else break;
        }
        for (int i = 0; i < Math.abs(dy); i++) {
            if (podeOcupar(getX(), getY() + passoY)) {
                setLocation(getX(), getY() + passoY);
                mudou = true;
            } else break;
        }
        return mudou;
    }

    // Hitbox = corpo inferior; colisoes em sprites grandes nao bloqueiam de longe
    private boolean podeOcupar(int x, int y) {
        int limiteSuperior = getWorld() instanceof FaseBase ? FaseBase.LIMITE_SUPERIOR_PISO : 77;
        if (x < 18 || x > getWorld().getWidth() - 18
            || y < limiteSuperior || y > getWorld().getHeight() - 27) return false;
        List<Cenario> objetos = getWorld().getObjects(Cenario.class);
        for (Cenario objeto : objetos) {
            if (objeto.bloqueiaPosicao(x, y + 23, 12, 9)) 
                return false;
        }
        return true;
    }

    // Mantem o personagem olhando para o lado em que se movimenta.
    protected void atualizarDirecao(int dx) {
        if (dx > 0) olhandoDireita = true;
        if (dx < 0) olhandoDireita = false;
    }

    // Alterna os passos enquanto anda e volta a pose parada ao parar
    protected void atualizarAnimacao(boolean andando) {
        if (!andando) {
            tempoAnimacao = 0;
            aplicarSprite(parado, "parado");
            return;
        }
        tempoAnimacao++;
        if (quadrosCorrida != null) {
            if (tempoAnimacao >= 7) {
                tempoAnimacao = 0;
                quadroCorrida = (quadroCorrida + 1) % quadrosCorrida.length;
            }
            aplicarSprite(quadrosCorrida[quadroCorrida], "corrida" + quadroCorrida);
            return;
        }
        if (tempoAnimacao >= 10) {
            tempoAnimacao = 0;
            frameA = !frameA;
        }
        aplicarSprite(frameA ? corridaA : corridaB, frameA ? "corridaA" : "corridaB");
    }

    // carrega quadros extras quando o personagem possui uma corrida mais detalhada.
    protected void configurarQuadrosCorrida(String[] arquivos) {
        if (arquivos == null || arquivos.length == 0) 
            return;
        
        quadrosCorrida = new GreenfootImage[arquivos.length];
        for (int i = 0; i < arquivos.length; i++) {
            quadrosCorrida[i] = new GreenfootImage(arquivos[i]);
        }
        quadroCorrida = 0;
    }

    // Ajusta apenas o desenho, mantendo a referencia dos pes e a colisao do mapa.
    protected void configurarEscalaVisual(int percentual) {
        escalaVisual = Math.max(100, percentual);
        ultimaImagem = "";
        aplicarSprite(parado, "parado");
    }

    // Troca a pose somente quando necessario e a espelha conforme a direcao
    protected void aplicarSprite(GreenfootImage imagem, String identificador) {
        String chave = identificador + (olhandoDireita ? "-D" : "-E");
        if (chave.equals(ultimaImagem)) return;
        GreenfootImage copia = new GreenfootImage(imagem);
        if (escalaVisual != 100) {
            int largura = copia.getWidth() * escalaVisual / 100;
            int altura = copia.getHeight() * escalaVisual / 100;
            copia.scale(largura, altura);
            GreenfootImage alinhada = new GreenfootImage(largura, altura);
            int ajusteDosPes = 24 * (100 - escalaVisual) / 100;
            alinhada.drawImage(copia, 0, ajusteDosPes);
            copia = alinhada;
        }
        if (!olhandoDireita) copia.mirrorHorizontally();
        setImage(copia);
        ultimaImagem = chave;
    }

    // Posiciona a representacao visual de um equipamento junto ao personagem.
    protected void atualizarItemNaMao(Equipamento equipamento) {
        if (getWorld() == null) return;
        if (equipamento == null) {
            removerItemMao();
            return;
        }
        if (itemNaMao == null) itemNaMao = new ItemEmpunhado();
        if (itemNaMao.getWorld() == null) getWorld().addObject(itemNaMao, getX(), getY());
        itemNaMao.mostrar(equipamento.getImagem());
        itemNaMao.atualizarDirecao(olhandoDireita);
        itemNaMao.setLocation(getX() + (olhandoDireita ? 29 : -29), getY() + 4);
    }

    // retira a representacao da mao quando ela nao deve aparecer
    protected void removerItemMao() {
        if (itemNaMao != null && itemNaMao.getWorld() != null) {
            itemNaMao.getWorld().removeObject(itemNaMao);
        }
    }

    // Dano corpo a corpo/acerto de arma. Nao ataca aliados
    protected boolean usarArma(Armamento arma) {
        if (getWorld() == null || arma == null) return false;
        if (!arma.podeUsar()) {
            Greenfoot.playSound("gun_inoperante.wav");
            return false;
        }
        if (arma instanceof ArmaFogo) {
            ArmaFogo fogo = (ArmaFogo) arma;
            if (!fogo.temMunicao()) {
                Greenfoot.playSound("gun_inoperante.wav");
                return false;
            }
            int dano = fogo.usar();
            fogo.gastarMunicao();
            int direcao = olhandoDireita ? 1 : -1;
            for (int i = 0; i < fogo.getQuantidadeProjetis(); i++) {
                int desvio = fogo.getQuantidadeProjetis() == 3 ? i - 1 : 0;
                getWorld().addObject(new Projetil(direcao, 10, dano, arma.getAlcance(), this, desvio),
                                     getX() + direcao * 19, getY() + 2);
            }
            return true;
        }
        Greenfoot.playSound("faca_ataque.wav");
        int dano = arma.usar();
        List<Personagem> alvos = getObjectsInRange(arma.getAlcance(), Personagem.class);
        for (Personagem alvo : alvos) {
            boolean inimigoDoJogador = this instanceof Jogador && alvo instanceof Inimigo;
            boolean jogadorDoInimigo = this instanceof Inimigo && alvo instanceof Jogador;
            if ((inimigoDoJogador || jogadorDoInimigo) && !alvo.estaMorto()
                && Math.abs(alvo.getY() - getY()) < 37
                && (olhandoDireita ? alvo.getX() >= getX()-7 : alvo.getX() <= getX()+7)) {
                alvo.receberDano(dano);
                return true;
            }
        }
        return true; // golpe executado, ainda que nao atinja um inimigo
    }

    // desconta vida e chama a rotina de morte quando ela chega a zero.
    public void receberDano(int dano) {
        if (dano <= 0 || estaMorto()) return;
        vida = Math.max(0, vida - dano);
        Greenfoot.playSound("golpe_acerto.wav");
        if (vida == 0) 
            aoMorrer();
    }

    // limpa a imagem do equipamento quando o personagem morre.
    protected void aoMorrer() { 
        removerItemMao(); 
    }
    
    // Informa se o personagem ficou sem vida.
    public boolean estaMorto() { 
        return vida <= 0; 
    }
    
    // Consulta a vida atual.
    public int getVida() { 
        return vida; 
    }

    // Consulta o limite de vida.
    public int getVidaMaxima() { 
        return vidaMaxima; 
    }
        
    public String getNome() { 
        return nome; 
    }
    
    public int getVelocidade() {
        return velocidade; 
    }
    
    protected boolean isOlhandoDireita() { return olhandoDireita; }

    // Conserva a vida quando um jogador atravessa para a proxima fase
    public void recuperarVida(int quantia) {
        if (quantia <= 0 || estaMorto()) return;
        vida = Math.min(vidaMaxima, vida + quantia);
    }
}
