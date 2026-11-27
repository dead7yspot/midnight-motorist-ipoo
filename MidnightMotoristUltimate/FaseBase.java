import greenfoot.*;

/**
 * Responsabilidades comuns das fases: progresso, HUD, sons e transicoes.
 * Cada subclasse e responsavel apenas por montar seu proprio mapa.
 */
public class FaseBase extends World {
    public static final int LARGURA = 1024;
    public static final int ALTURA = 572;
    // limite superior do piso, para nao entrar nas fachadas do fundo.
    public static final int LIMITE_SUPERIOR_PISO = 160;

    private final GerenciadorComandos comandos;
    private final EstadoPartida estado;
    private final int numeroFase;
    private Jogador jogador;
    private Cenario saida;
    private GreenfootImage marcaSaida;
    private BarraVida barraVida;
    private Placar placar;
    private TextoHUD tituloHUD, itemHUD, objetivoHUD, avisoHUD;
    private boolean terminou;
    private String aviso = "";
    private int tempoAviso;
    private int totalInimigos;
    private int derrotados;


    // Prepara o cenario, a ordem de desenho e a musica da fase
    public FaseBase(int numero, GerenciadorComandos comandos, EstadoPartida estado) {
        super(LARGURA, ALTURA, 1);
        this.numeroFase = numero;
        this.comandos = comandos;
        this.estado = estado;
        
        GreenfootImage fundo = new GreenfootImage(numero == 3 ? "cidade_viva_fase3.png" : "cidade_viva_fase1.png");
        fundo.scale(LARGURA, ALTURA);
        
        // faixa de contraste para a pontuacao e a vida nao sumirem sobre o mapa.
        fundo.setColor(new Color(12, 15, 20, 223));
        fundo.fillRect(0, 0, LARGURA, 44);
        fundo.setColor(new Color(77, 83, 91, 105));
        fundo.fillRect(0, 43, LARGURA, 1);
        setBackground(fundo);
        
        setPaintOrder(TextoHUD.class, BarraVida.class, Placar.class, ItemEmpunhado.class,
                      Jogador.class, Inimigo.class, Equipamento.class, Projetil.class, LuzesCidade.class);
        addObject(new LuzesCidade(numero), LARGURA / 2, ALTURA / 2);
        
        GerenciadorAudio.iniciarMusica("No More (Remastered).mp3");
    }

    /** Reutiliza o mesmo Jogador entre fases: preserva vida, equipamentos e comandos. */
    protected void colocarJogador(Jogador anterior, int x, int y) {
        if (anterior != null) {
            anterior.removerItemMao();
            if (anterior.getWorld() != null) 
                anterior.getWorld().removeObject(anterior);
            jogador = anterior;
        } else {
            
            // Jogador padrao: 110 de vida, velocidade 4 de pixeis
            jogador = new Jogador("Motorist", 110, 4, comandos);
        }
        addObject(jogador, x, y);
        barraVida = new BarraVida(jogador.getVidaMaxima());
        placar = new Placar();
        addObject(barraVida, 85, 14);
        addObject(placar, LARGURA - 70, 14);
        tituloHUD = new TextoHUD(14, Color.WHITE);
        
        itemHUD = new TextoHUD(30, Color.WHITE);
        objetivoHUD = new TextoHUD(13, new Color(175, 217, 190));
        avisoHUD = new TextoHUD(17, Color.WHITE);
        
        addObject(tituloHUD, 211, 13);
        addObject(itemHUD, LARGURA/2, 34);
        addObject(objetivoHUD, LARGURA/2, 13);
        addObject(avisoHUD, LARGURA/2, ALTURA - 47);
        atualizarHUD();
    }

    // Adiciona ao mapa um objeto com sua propria area de colisao
    protected void adicionarObstaculo(String nome, String imagem, int x, int y,
                                      int largura, int altura, int raioX, int raioY, int offsetY) {
        Cenario obj = new Cenario(nome, 0, false, true, imagem,
                                  largura, altura, raioX, raioY, offsetY);
        addObject(obj, x, y);
    }

    // Marca discretamente no asfalto o ponto para sair da fase
    protected void definirSaida(int x, int y) {
        saida = new Cenario("Saida da rua", numeroFase, true, false,
                            "saida_chao.png", 86, 104, 0, 0, 0);
        marcaSaida = new GreenfootImage(saida.getImage());
        saida.setImage(new GreenfootImage(1, 1));
        addObject(saida, x, y);
    }

    // Atualiza os indicadores e controla a duracao dos avisos
    public void act() {
        if (terminou || jogador == null) 
            return;
            
        atualizarHUD();
        if (tempoAviso > 0) 
            tempoAviso--;
        if (tempoAviso == 0) 
            aviso = "";
        avisoHUD.definirTexto(aviso);
    }

    // Mostra vida, pontos, equipamento e objetivo atual
    private void atualizarHUD() {
        if (barraVida != null) 
            barraVida.atualizar(jogador.getVida());
        if (placar != null) 
            placar.definirPontos(estado.getPontos());
        tituloHUD.definirTexto("FASE " + numeroFase);
        itemHUD.definirTexto(jogador.obterEquipamentoTexto());
        
        if (numeroFase == 1) {
            if (totalInimigos > 0 && derrotados >= totalInimigos)
                objetivoHUD.definirTexto("RUA LIVRE - VA A DIREITA E PRESSIONE "
                                       + comandos.obterTeclaExibicao("INTERAGIR"));
            else 
                objetivoHUD.definirTexto("INIMIGOS " + derrotados + "/" + totalInimigos + " - elimine TODOS!");
        } else if (numeroFase < 3) {
            objetivoHUD.definirTexto("Encontre a saida");
        } else {
            Fase03 terceira = this instanceof Fase03 ? (Fase03) this : null;
            if (terceira != null && terceira.estaNoTanque())
                objetivoHUD.definirTexto("TANQUE HP: " + terceira.getVidaChefe() + " | "
                    + comandos.obterTeclaExibicao("INTERAGIR") + ": pegar granadas  "
                    + comandos.obterTeclaExibicao("LANCAR_GRANADA") + ": lancar");
            else objetivoHUD.definirTexto("Chefe HP: " + (terceira == null ? "?" : terceira.getVidaChefe())
                 + " | Vença a primeira forma!");
        }
    }

    // Mostra uma mensagem temporaria ao jogador
    public void avisar(String mensagem) {
        aviso = mensagem;
        tempoAviso = 125;
    }

    // Soma ao placar os pontos recebidos por uma coleta
    public void coletouItem(int pontos) { 
        estado.adicionarPontos(pontos); 
    }

    // Numero de inimigos e declarado pela fase que os cria
    protected void configurarObjetivoAbates(int quantidade) {
        totalInimigos = quantidade;
        derrotados = 0;
        
        if (jogador != null) 
            atualizarHUD();
    }

    /** Atualiza pontos e objetivos quando um inimigo e derrotado. 
    */
    public void inimigoDerrotado(int pontos, Inimigo inimigo) {
        if (terminou) 
            return;
        
        estado.adicionarPontos(pontos);
        if (numeroFase == 1 && totalInimigos > 0) {
            derrotados++;
            if (derrotados >= totalInimigos) {
                if (saida != null && marcaSaida != null) saida.setImage(marcaSaida);
                avisar("Caminho livre! Va para a direita e pressione "
                       + comandos.obterTeclaExibicao("INTERAGIR") + ".");
            } else {
                avisar("Inimigo derrotado! Restam " + (totalInimigos - derrotados));
            }
        } else if (numeroFase == 3 && inimigo instanceof TanqueChefe) {
            terminarComVitoria();
        }
    }

    // Verifica objetivo e proximidade antes de liberar a saida da rua
    public void tentarSair(Jogador solicitante) {
        if (terminou || solicitante != jogador || saida == null) 
            return;
        
        if (numeroFase == 1 && derrotados < totalInimigos) {
            avisar("Derrote todos os inimigos antes de sair.");
            return;
        }
        
        if (Math.abs(jogador.getX()-saida.getX()) >= 61
            || Math.abs(jogador.getY()-saida.getY()) >= 68) {
            avisar("Aproxime-se da saida a direita para atravessar.");
            return;
        }
        
        if (numeroFase == 1) {
            terminou = true;
            estado.adicionarPontos(200);
            Greenfoot.setWorld(new TelaVitoriaFase(1, comandos, estado, jogador));
        }
    }

    // Encerra a fase atual e abre a derrota
    public void jogadorMorreu() {
        if (terminou) 
            return;
        terminou = true;
        
        GerenciadorAudio.pararMusica();
        Greenfoot.setWorld(new TelaGameOver(comandos, estado));
    }

    // encerra o confronto final e abre o resultado da partida
    public void terminarComVitoria() {
        if (terminou) 
            return;
        terminou = true;
        
        GerenciadorAudio.pararMusica();
        Greenfoot.setWorld(new TelaVitoria(comandos, estado));
    }

    // Devolve os comandos configurados da partida
    public GerenciadorComandos getComandos() { 
        return comandos;
    }
    
    // devolve o estado compartilhado entre as fases
    public EstadoPartida getEstado() { 
        return estado; 
    }
    
    // informa o numero desta fase
    public int getNumeroFase() {
        return numeroFase; 
    }
    
    // Devolve o protagonista presente na fase
    public Jogador getJogador() { 
        return jogador; 
    }
}
