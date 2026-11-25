import greenfoot.*;

/** Confronto final em duas partes: chefe humano, depois tanque. */
public class Fase03 extends FaseBase {
    private boolean tanqueAtivo;
    private int tempoAbastecimento;

    // Prepara a fase final com o jogador vindo da perseguicao
    public Fase03(GerenciadorComandos comandos, EstadoPartida estado, Jogador anterior) {
        super(3, comandos, estado);
        prepararCenario(anterior);
    }
    
    // construtor para debug, para facilitar testes
    public Fase03() {
        this(TelaInicial.criarComandosPadrao(), new EstadoPartida(), new Jogador("teste", 200, 4, TelaInicial.criarComandosPadrao()));
    }

    /** Posiciona o chefao, os suprimentos e os obstaculos do confronto. */
    private void prepararCenario(Jogador anterior) {
        colocarJogador(anterior, 108, 459);
        getJogador().recuperarVida(30);
        adicionarObstaculo("Carro", "noir_carro_abandonado.png", 415, 335, 147, 92, 57, 17, 27);
        adicionarObstaculo("Caixa de carga", "noir_caixa.png", 236, 199, 80, 72, 29, 12, 22);
        adicionarObstaculo("Poste", "noir_poste.png", 610, 273, 40, 118, 10, 10, 50);
        adicionarObstaculo("Barril", "noir_barril.png", 642, 480, 58, 58, 22, 20, 12);
        addObject(new Suprimento("Kit medico", 45), 185, 495);
        addObject(new ArmaFogo("Revolver final", "Muita municao", "revolverJogo.png",
                               150, 540, 65, 23), 345, 477);
        addObject(new ChefeFinal(getJogador()), 820, 403);
        avisar("CHEFE FINAL: enfrente o chefe e prepare-se para a segunda forma!");
    }

    /** A propria fase coordena a troca de forma e a disponibilizacao das granadas. */
    public void iniciarFaseTanque(ChefeFinal chefe, Jogador alvo) {
        if (tanqueAtivo || chefe.getWorld() != this) return;
        int x = chefe.getX(), y = chefe.getY();
        tanqueAtivo = true;
        removeObject(chefe);
        
        addObject(new TanqueChefe(alvo, 
              new ArmaFogo("Canhao", "Disparo de tanque", "revolverJogo.png",
                           500, 600, 500, 20, 25, 1)), x, y);
                           
        getEstado().adicionarPontos(250);
        adicionarGranadas();
        avisar("O CHEFE ENTROU NO TANQUE! Colete com "
               + getComandos().obterTeclaExibicao("INTERAGIR") + " e lance com "
               + getComandos().obterTeclaExibicao("LANCAR_GRANADA") + "!");
        Greenfoot.playSound("sirene.wav");
    }

    /** Espalha as primeiras granadas coletaveis quando o tanque aparece. */
    private void adicionarGranadas() {
        int[] posicoes = {220, 345, 495, 715};
        int[] alturas = {425, 493, 489, 445};
        for (int i = 0; i < posicoes.length; i++) {
            addObject(new GranadaColetavel(2), posicoes[i], alturas[i]);
        }
    }

    /** Reabastece o mapa periodicamente para permitir novas tentativas. */
    @Override
    public void act() {
        super.act();
        if (!tanqueAtivo || getJogador().estaMorto()) return;
        // Reposicao garante que a fase possa ser vencida mesmo apos errar lancamentos.
        tempoAbastecimento++;
        if (tempoAbastecimento >= 330) {
            tempoAbastecimento = 0;
            if (getObjects(GranadaColetavel.class).size() < 4) {
                int x = 240 + Greenfoot.getRandomNumber(440);
                int y = 420 + Greenfoot.getRandomNumber(85);
                addObject(new GranadaColetavel(2), x, y);
            }
        }
    }

    /** Informa se o chefao ja entrou no tanque. */
    public boolean estaNoTanque() { return tanqueAtivo; }
    /** Consulta a vida da forma do chefe que estiver ativa. */
    public int getVidaChefe() {
        if (!tanqueAtivo && !getObjects(ChefeFinal.class).isEmpty())
            return getObjects(ChefeFinal.class).get(0).getVida();
        if (tanqueAtivo && !getObjects(TanqueChefe.class).isEmpty())
            return getObjects(TanqueChefe.class).get(0).getVida();
        return 0;
    }
}
