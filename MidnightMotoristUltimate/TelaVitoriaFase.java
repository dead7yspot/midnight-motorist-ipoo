import greenfoot.*;

/** Tela exclusiva de missao concluida, com arte que mostra o protagonista vitorioso. */
public class TelaVitoriaFase extends World {
    private final GerenciadorComandos comandos;
    private final EstadoPartida estado;
    private final Jogador jogador;
    private final int faseConcluida;

    /** Apresenta a vitoria da fase e guarda o jogador para a proxima. */
    public TelaVitoriaFase(int numero, GerenciadorComandos comandos,
                           EstadoPartida estado, Jogador jogador) {
        super(1024, 572, 1);
        this.faseConcluida = numero;
        this.comandos = comandos;
        this.estado = estado;
        this.jogador = jogador;
        GreenfootImage fundo = ArteTelas.criarResultado(numero, true);
        setBackground(fundo);
        escreverPlacar();
        GerenciadorAudio.iniciarMusica("Relaxed Scene.mp3");
    }

    /** Mostra pontos, tempo e instrucoes para continuar. */
    private void escreverPlacar() {
        GreenfootImage fundo = getBackground();
        fundo.drawImage(new GreenfootImage("PONTOS DA FASE: " + estado.getPontosFase(faseConcluida)
             + "    TOTAL: " + estado.getPontos() + "    TEMPO: " + estado.getSegundos() + "s",
             18, Color.WHITE, null), 60, 465);
        fundo.drawImage(new GreenfootImage("ENTER / ESPACO: PROXIMA FASE    |    ESC: MENU",
             17, Color.WHITE, null), 60, 520);
    }

    /** Aguarda o jogador continuar para a fase seguinte ou voltar ao menu. */
    public void act() {
        String tecla = Greenfoot.getKey();
        if ("escape".equals(tecla)) {
            Greenfoot.setWorld(new TelaInicial(comandos));
        } else if ("enter".equals(tecla) || "space".equals(tecla)) {
            estado.avancarFase();
            if (faseConcluida == 1) Greenfoot.setWorld(new Fase02(comandos, estado, jogador));
            else Greenfoot.setWorld(new Fase03(comandos, estado, jogador));
        }
    }
}
