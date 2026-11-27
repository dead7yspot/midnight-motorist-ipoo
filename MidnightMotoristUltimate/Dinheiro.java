/** Dinheiro opcional: cria caminhos com pontuacoes finais diferentes. */
public class Dinheiro extends Equipamento {
    private final int valor;
    /** Cria uma coleta de dinheiro com o valor informado. */
    public Dinheiro(int valor) {
        super("Dinheiro", "Bonus de pontuacao", "ultimate_dinheiro.png");
        this.valor = valor;
    }
    /** Consulta os pontos concedidos pela coleta. */
    public int getValor() { return valor; }
}
