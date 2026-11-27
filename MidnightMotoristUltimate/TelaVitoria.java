/** Tela de vitoria separada, acessada ao vencer o chefe. */
public class TelaVitoria extends TelaFim {
    
    /** Encaminha a vitoria e o placar para a tela final. */
    public TelaVitoria(GerenciadorComandos comandos, EstadoPartida estado) {
        super(comandos, estado, true);
    }
}
