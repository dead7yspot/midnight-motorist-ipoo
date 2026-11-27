import greenfoot.*;

/** 
 * Fase da perseguicao policial
 */
public class Fase02 extends World {
    public static final int DURACAO = 2100;
    private static final int INTERVALO_TRANSITO = 88;
    private final GerenciadorComandos comandos;
    private final EstadoPartida estado;
    private final Jogador jogador;
    private int tempo;
    private int distanciaPolicia = 760;
    private boolean terminou;
    private boolean musicaPressionada;
    
    // Responsavel pela sirene da policia que é utilizada somente nesta fase;
    // deixar sob responsabilidade da GerenciadorAudio pode causar resultados inesperados devido às 
    // musicas de fundo
    private GreenfootSound sirene;
    
    // Monta a rodovia preservando o protagonista e o placar anteriores (passados por parametrO)
    public Fase02(GerenciadorComandos comandos, EstadoPartida estado, Jogador jogador) {
        super(1024, 572, 1);
        this.comandos = comandos;
        this.estado = estado;
        this.jogador = jogador;
        
        musicaPressionada = comandoMusicaPressionado();
        
        sirene = new GreenfootSound("sirene.wav");
        
        GreenfootImage fundo = new GreenfootImage("cidade_viva_fase2.png");
        fundo.setColor(new Color(12, 15, 20, 225));
        fundo.fillRect(0, 0, 1024, 45);
        setBackground(fundo);
        
        setPaintOrder(CarroJogador.class, CarroPolicia.class, ItemRodovia.class,
                      ObstaculoRodovia.class, PlacaSaidaRodovia.class, FaixaRodovia.class);
        
        // cuida das faixas se movendo pra dar sensacao de velocidade
        for (int x = 35; x < 1024; x += 140) {
            addObject(new FaixaRodovia(), x, 220);
            addObject(new FaixaRodovia(), x, 350);
        }
        
        addObject(new CarroJogador(comandos), 220, 285);
        addObject(new CarroPolicia(), 70, 285);
        atualizarHUD();
        
        sirene.playLoop();
        // tava muito alto
        sirene.setVolume(75);
        
        GerenciadorAudio.iniciarMusica("midlife-crisis.mp3");
    }

    // construtor debug para facilitar a fase de desenvolvimento (sem ter que ficar passando a fase 01 toda hora)
    public Fase02() {
        this(TelaInicial.criarComandosPadrao(), new EstadoPartida(), new Jogador("teste", 100, 4, TelaInicial.criarComandosPadrao()));
    }
    
    public void act() {
        boolean musicaAgora = comandoMusicaPressionado();
        
        if (musicaAgora && !musicaPressionada) 
            GerenciadorAudio.alternarMusica();
        musicaPressionada = musicaAgora;
        tempo++;
        
        if (tempo % 20 == 0) {
            addObject(new FaixaRodovia(), 1023, 220);
            addObject(new FaixaRodovia(), 1023, 350);
        }
        if (tempo % 4 == 0) 
            distanciaPolicia--;
            
        if (tempo % INTERVALO_TRANSITO == 0) {
            int faixa = Greenfoot.getRandomNumber(3);
            addObject(new ObstaculoRodovia(Greenfoot.getRandomNumber(3) != 0),
                      990, 155 + faixa * 130);
        }
        
        if (tempo % 235 == 0) {
            int y = 155 + Greenfoot.getRandomNumber(3) * 130;
            
            // tempo para criar um objeto nitro
            if ((tempo / 235) % 2 == 0) 
                addObject(new BonusRodovia(), 990, y);
            else addObject(new TurboRodovia(), 990, y);
        }
        
        if (tempo == DURACAO - 150) 
            addObject(new PlacaSaidaRodovia(), 990, 98);
        
        // a viatura esta relativamente e perigosamente perto do jogador. Exibir um aviso
        if (distanciaPolicia <= 400) {
            showText("CUIDADO! A POLICIA ESTÁ NA SUA COLA!\nPEGUE NITRO PARA SE DISTANCIAR", 512, 520);
        }   else showText("", 512, 520);
        
        atualizarHUD();
        
        // Condicoes que terminam a fase
        if (distanciaPolicia <= 100) {
            sirene.stop();
            
            terminou = true;
            Greenfoot.setWorld(new TelaPerseguicaoFim(comandos, estado, jogador));
        } else if (tempo >= DURACAO) {
            sirene.stop();
            
            terminou = true;
            estado.adicionarPontos(270);
            Greenfoot.setWorld(new TelaVitoriaFase(2, comandos, estado, jogador, "midlife-crisis_transicao2.mp3"));
        }
    }

    // aproxima a policia quando o carro bate em um obstaculo. 
    public void colisao() {
        if (terminou) 
            return;
        distanciaPolicia -= 150;
        Greenfoot.playSound("veh_col.wav");
    }

    // Consulta a distancia atual da viatura
    public int getDistanciaPolicia() { 
        return distanciaPolicia; 
    }

    // Aplica os pontos e o afastamento obtidos com um item da rodovia
    public void bonus(int pontos, int distancia) {
        estado.adicionarPontos(pontos);
        
        distanciaPolicia = Math.min(850, distanciaPolicia + distancia);
        
        Greenfoot.playSound("som_pickup.wav");
    }

    //  Mostra pontuacao e progresso desde a criacao da rodovia.
    private void atualizarHUD() {
        showText("FASE 2 / FUGA     Pontos: " + estado.getPontos()
                 + "     POLICIA: " + Math.max(0, distanciaPolicia) + " m", 512, 13);
        showText("PROGRESSO: " + (tempo * 100 / DURACAO) + "%     $: Pontos    N2O: Distanciar da policia", 512, 33);
    }

    // Verifica as teclas de musica configuradas para a perseguicao
    private boolean comandoMusicaPressionado() {
        for (String tecla : comandos.obterTeclas("ALTERNAR_MUSICA"))
            if (Greenfoot.isKeyDown(tecla)) 
                return true;
        return false;
    }
}
