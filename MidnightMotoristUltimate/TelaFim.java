import greenfoot.*;

/** Superclasse das telas finais: placar por fase, tempo, reinicio e menu. */
public class TelaFim extends World {
    private final GerenciadorComandos comandos;
    private final EstadoPartida estado;
    private final boolean vitoria;

    /** Abre o resultado final com o placar da partida atual. */
    public TelaFim(GerenciadorComandos comandos, EstadoPartida estado, boolean vitoria) {
        super(1024, 572, 1);
        this.comandos = comandos;
        this.estado = estado;
        this.vitoria = vitoria;
        prepararTela();
    }

    /** Mantem a construcao alternativa com um placar novo. */
    public TelaFim(GerenciadorComandos comandos, boolean vitoria) {
        this(comandos, new EstadoPartida(), vitoria);
    }

    /** Monta a arte correta e escreve pontos, tempo e opcoes. */
    private void prepararTela() {
        GreenfootImage fundo = ArteTelas.criarResultado(estado.getFase(), vitoria);
        setBackground(fundo);
        int primeiraLinha = !vitoria && estado.getFase() == 3 ? 480 : 465;
        desenhar("FASE 1: " + estado.getPontosFase(1) + "     FASE 2: " + estado.getPontosFase(2)
                + "     FASE 3: " + estado.getPontosFase(3), 18, 60, primeiraLinha);
        desenhar("TOTAL: " + estado.getPontos() + " pts       TEMPO: " + estado.getSegundos() + " s",
                18, 60, primeiraLinha + 26);
        desenhar("R: REJOGAR DO INICIO       ENTER: MENU", 17, 60, primeiraLinha + 55);
        GerenciadorAudio.pararMusica();
    }

    /** Desenha uma linha de texto sobre o resultado. */
    private void desenhar(String texto, int tamanho, int x, int y) {
        getBackground().drawImage(new GreenfootImage(texto, tamanho, Color.WHITE, null), x, y);
    }

    /** Permite recomecar ou voltar ao menu sem usar o botao Run. */
    public void act() {
        String tecla = Greenfoot.getKey();
        if ("r".equals(tecla)) Greenfoot.setWorld(new Fase01(comandos, new EstadoPartida()));
        else if ("enter".equals(tecla) || "escape".equals(tecla))
            Greenfoot.setWorld(new TelaInicial(comandos));
    }
}
