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
    
    private GreenfootSound sirene;

    // Monta a rodovia preservando o protagonista e o placar anteriores
    public Fase02(GerenciadorComandos comandos, EstadoPartida estado, Jogador jogador) {
        super(1024, 572, 1);
        this.comandos = comandos;
        this.estado = estado;
        this.jogador = jogador;
        musicaPressionada = comandoMusicaPressionado();
        GreenfootImage fundo = new GreenfootImage("cidade_viva_fase2.png");
        fundo.setColor(new Color(12, 15, 20, 225));
        fundo.fillRect(0, 0, 1024, 45);
        setBackground(fundo);
        setPaintOrder(CarroJogador.class, CarroPolicia.class, ItemRodovia.class,
                      ObstaculoRodovia.class, PlacaSaidaRodovia.class, FaixaRodovia.class, LuzesCidade.class);
        addObject(new LuzesCidade(2), 512, 286);
        for (int x = 35; x < 1024; x += 140) {
            addObject(new FaixaRodovia(), x, 220);
            addObject(new FaixaRodovia(), x, 350);
        }
        addObject(new CarroJogador(comandos), 220, 285);
        addObject(new CarroPolicia(), 70, 285);
        atualizarHUD();
        
        sirene = new GreenfootSound("sirene.wav");
        
        sirene.playLoop();
        
        GerenciadorAudio.iniciarMusica("No More (Remastered).mp3");
    }

    
    public void act() {
        
        if (terminou) 
            return;
        boolean musicaAgora = comandoMusicaPressionado();
        
        if (musicaAgora && !musicaPressionada) GerenciadorAudio.alternarMusica();
        musicaPressionada = musicaAgora;
        tempo++;
        
        if (tempo % 20 == 0) {
            addObject(new FaixaRodovia(), 1023, 220);
            addObject(new FaixaRodovia(), 1023, 350);
        }
        if (tempo % 12 == 0) distanciaPolicia--;
        if (tempo % INTERVALO_TRANSITO == 0) {
            int faixa = Greenfoot.getRandomNumber(3);
            addObject(new ObstaculoRodovia(Greenfoot.getRandomNumber(3) != 0),
                      990, 155 + faixa * 130);
        }
        if (tempo % 235 == 0) {
            int y = 155 + Greenfoot.getRandomNumber(3) * 130;
            if ((tempo / 235) % 2 == 0) addObject(new BonusRodovia(), 990, y);
            else addObject(new TurboRodovia(), 990, y);
        }
        if (tempo == DURACAO - 150) addObject(new PlacaSaidaRodovia(), 990, 98);
        atualizarHUD();
        if (distanciaPolicia <= 0) {
            terminou = true;
            Greenfoot.setWorld(new TelaPerseguicaoFim(comandos, estado, jogador));
        } else if (tempo >= DURACAO) {
            terminou = true;
            estado.adicionarPontos(270);
            Greenfoot.setWorld(new TelaVitoriaFase(2, comandos, estado, jogador));
        }
    }

    // aproxima a policia quando o carro bate em um obstaculo. 
    public void colisao() {
        if (terminou) return;
        distanciaPolicia -= 110;
        Greenfoot.playSound("som_dano.wav");
    }

    // Consulta a distancia atual da viatura
    public int getDistanciaPolicia() { 
        return distanciaPolicia; 
    }

    // Aplica os pontos e o afastamento obtidos com um item da rodovia.
    public void bonus(int pontos, int distancia) {
        estado.adicionarPontos(pontos);
        distanciaPolicia = Math.min(850, distanciaPolicia + distancia);
        Greenfoot.playSound("hit_som.wav");
    }

    //  Mostra pontuacao e progresso desde a criacao da rodovia.
    private void atualizarHUD() {
        showText("FASE 2 / FUGA     PONTOS: " + estado.getPontos()
                 + "     POLICIA: " + Math.max(0, distanciaPolicia) + " m", 512, 13);
        showText("PROGRESSO: " + (tempo * 100 / DURACAO)
                 + "%     $: PONTOS    N2O: AFASTA A POLICIA", 512, 33);
    }

    // Verifica as teclas de musica configuradas para a perseguicao
    private boolean comandoMusicaPressionado() {
        for (String tecla : comandos.obterTeclas("ALTERNAR_MUSICA"))
            if (Greenfoot.isKeyDown(tecla)) 
                return true;
        return false;
    }
}
