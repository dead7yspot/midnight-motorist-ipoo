import greenfoot.*;

/**
 * Inimigo de perseguicao do alvo_meu. Concentramos aqui combate e vida;
 * subclasses podem substituir apenas o padrao de movimentacao.
 */
public class Inimigo extends Personagem {
    private final Jogador jogador;
    private final int distanciaParada;
    private final Armamento arma;
    private int cooldownAtaque;
    private int intervaloAtaque = 68;
    private int raioVisao = 325;
    private int animacaoAtaque;
    private final GreenfootImage spriteAtaque;

    // configura o alvo, a arma e as poses compartilhadas pelos inimigos
    public Inimigo(String nome, int vida, int velocidade, Jogador jogador,
                   int distanciaParada, Armamento arma, String parado,
                   String corridaA, String corridaB, String ataque) {
        super(nome, vida, velocidade, parado, corridaA, corridaB);
        configurarEscalaVisual(135);
        this.jogador = jogador;
        this.distanciaParada = distanciaParada;
        this.arma = arma;
        spriteAtaque = new GreenfootImage(ataque);
    }

    /** Move o inimigo, atualiza sua animacao e tenta atacar o jogador
     *
     */
    public void act() {
        if (getWorld() == null || estaMorto()) 
            return;
        if (jogador == null || jogador.getWorld() != getWorld() || jogador.estaMorto()) 
            return;
        
        boolean andando = moverInimigo();
        
        if (animacaoAtaque > 0) {
            animacaoAtaque--;
            aplicarSprite(spriteAtaque, "ataque");
        } else atualizarAnimacao(andando);
        removerItemMao();
        
        if (cooldownAtaque > 0) cooldownAtaque--;
        tentarAtacar();
    }

    // persegue o jogador quando ele esta no raio de visao.
    protected boolean moverInimigo() {
        int deltaX = jogador.getX() - getX();
        int deltaY = jogador.getY() - getY();
        atualizarDirecao(deltaX);
        
        if (Math.abs(deltaX) + Math.abs(deltaY) > raioVisao) 
            return false;
        
        if (Math.abs(deltaX) < distanciaParada && Math.abs(deltaY) < 32) 
            return false;
        
        int passoX = deltaX > 3 ? getVelocidade() : deltaX < -3 ? -getVelocidade() : 0;
        int passoY = deltaY > 3 ? getVelocidade() : deltaY < -3 ? -getVelocidade() : 0;
        
        return moverComColisao(passoX, passoY);
    }

    // Ataca somente quando o alvo esta alinhado, ao alcance e sem recarga
    private void tentarAtacar() {
        if (cooldownAtaque > 0 || arma == null || !arma.podeUsar()) return;
        int dx = jogador.getX() - getX();
        int dy = jogador.getY() - getY();
        if (Math.abs(dx) > arma.getAlcance() || Math.abs(dy) > 31) return;
        atualizarDirecao(dx);
        if (usarArma(arma)) {
            cooldownAtaque = intervaloAtaque;
            animacaoAtaque = 10;
        }
    }

    // devolve o jogador que este inimigo persegue
    protected Jogador obterJogador() {
        return jogador; 
    }
    
    // Permite que cada inimigo defina sua distancia de deteccao.
    protected void ajustarRaioVisao(int raio) { 
        raioVisao = raio; 
    }
    
    // Ajusta o intervalo entre ataques deste inimigo
    protected void ajustarIntervaloAtaque(int intervalo) { 
        intervaloAtaque = intervalo;
    }
    
    // define a recompensa padrao por derrotar o inimigo.
    protected int pontosPorMorte() { 
        return 120; 
    }

    // registra a recompensa e retira o inimigo derrotado do mundo
    @Override
    protected void aoMorrer() {
        World mundo = getWorld();
        super.aoMorrer();
        if (mundo instanceof FaseBase) 
            ((FaseBase) mundo).inimigoDerrotado(pontosPorMorte(), this);
        if (mundo != null) 
            mundo.removeObject(this);
    }
}
