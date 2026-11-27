/** Tela de derrota separada para facilitar manutencao. */
public class TelaGameOver extends TelaFim {
    /** Encaminha a derrota e o placar para a tela final. */
    public TelaGameOver(GerenciadorComandos comandos, EstadoPartida estado) {
        super(comandos, estado, false);
    }
}
