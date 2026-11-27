/** Guarda apenas a progressao global; inventario e vida pertencem ao Jogador. */
public class EstadoPartida {
    private int pontos;
    private final int[] pontosFase = new int[3];
    private final long inicio = System.currentTimeMillis();
    private int fase;

    /** Inicia o placar e a contagem de tempo de uma nova partida. */
    public EstadoPartida() {
        pontos = 0;
        fase = 1;
    }
    /** Soma pontos ao total e a fase em andamento. */
    public void adicionarPontos(int valor) {
        if (valor > 0) {
            pontos += valor;
            pontosFase[Math.min(3, Math.max(1, fase)) - 1] += valor;
        }
    }
    /** Consulta o total de pontos da partida. */
    public int getPontos() { return pontos; }
    /** Consulta a fase em andamento. */
    public int getFase() { return fase; }
    /** Avanca o registro para a fase seguinte. */
    public void avancarFase() { fase++; }
    /** Consulta os pontos conquistados em uma fase. */
    public int getPontosFase(int numero) { return pontosFase[numero - 1]; }
    /** Calcula o tempo decorrido desde o inicio da partida. */
    public long getSegundos() { return (System.currentTimeMillis() - inicio) / 1000; }
    /** Descarta apenas os pontos da fase que sera repetida. */
    public void reiniciarFaseAtual() {
        pontos -= pontosFase[fase - 1];
        pontosFase[fase - 1] = 0;
    }
}
