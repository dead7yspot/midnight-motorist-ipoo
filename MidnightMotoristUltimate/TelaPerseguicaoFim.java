import greenfoot.*;

/** Tela de derrota na rodovia, com opcao de tentar novamente do checkpoint. */
public class TelaPerseguicaoFim extends World {
    private final GerenciadorComandos comandos;
    private final EstadoPartida estado;
    private final Jogador jogador;

    /** Mostra a derrota na rodovia e guarda os dados do checkpoint. */
    public TelaPerseguicaoFim(GerenciadorComandos comandos, EstadoPartida estado, Jogador jogador) {
        super(1024, 572, 1);
        this.comandos = comandos;
        this.estado = estado;
        this.jogador = jogador;
        
        GreenfootImage imagem = ArteTelas.criarResultado(2, false);
        
        // O placar real fica em uma faixa discreta sobre a imagem da rodovia
        imagem.setColor(new Color(5, 10, 17, 190));
        imagem.fillRect(38, 449, 520, 97);
        
        imagem.drawImage(new GreenfootImage("F1: " + estado.getPontosFase(1) + "   F2: "
            + estado.getPontosFase(2) + "   F3: " + estado.getPontosFase(3)
            + "   TOTAL: " + estado.getPontos(), 18, Color.WHITE, null), 52, 462);
        
        imagem.drawImage(new GreenfootImage("TEMPO: " + estado.getSegundos()
            + "s    R: REPETIR FASE 2    ENTER: MENU", 17, Color.WHITE, null), 52, 512);
        
        setBackground(imagem);
        
        // faz a musica anterior parar
        GerenciadorAudio.pararMusica();
    }

    // Repete a perseguicao ou manda o jogador de volta pro menu
    public void act() {
        String tecla = Greenfoot.getKey();
        
        if ("r".equals(tecla)) {
            estado.reiniciarFaseAtual();
            Greenfoot.setWorld(new Fase02(comandos, estado, jogador));
        } else if ("enter".equals(tecla)) 
            Greenfoot.setWorld(new TelaInicial(comandos));
    }
}