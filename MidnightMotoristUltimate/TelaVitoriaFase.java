import greenfoot.*;

/** Tela exclusiva de missao concluida, com arte que mostra o protagonista vitorioso. */
public class TelaVitoriaFase extends World {
    private final GerenciadorComandos comandos;
    private final EstadoPartida estado;
    private final Jogador jogador;
    private final int faseConcluida;

    // Apresenta a vitoria da fase e guarda o jogador para a proxima
    public TelaVitoriaFase(int faseConcluida, GerenciadorComandos comandos,
                           EstadoPartida estado, Jogador jogador) {
        super(1024, 572, 1);
        
        // qual fase foi concluida
        this.faseConcluida = faseConcluida;
        this.comandos = comandos;
        this.estado = estado;
        this.jogador = jogador;
        
        // cria a imagem de acordo com a fase que foi concluida. Ex: faseConcluida = 2 -> mostrara viaturas, "fuga concluida" etc
        GreenfootImage fundo = ArteTelas.criarResultado(faseConcluida, true);
        setBackground(fundo);
        
        escreverPlacar();
        
        // musica para essa fase concluida
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

    // Aguarda o jogador continuar para a fase seguinte ou voltar ao menu
    // como temos tres fases, é possivel manter controle da prox fase com um if/else
    public void act() {
        String tecla = Greenfoot.getKey();
        if ("escape".equals(tecla)) {
            Greenfoot.setWorld(new TelaInicial(comandos));
        } else if ("enter".equals(tecla) || "space".equals(tecla)) {
            estado.avancarFase();
            
            if (faseConcluida == 1) 
                Greenfoot.setWorld(new Fase02(comandos, estado, jogador));
            else 
                Greenfoot.setWorld(new Fase03(comandos, estado, jogador));
        }
    }
}
