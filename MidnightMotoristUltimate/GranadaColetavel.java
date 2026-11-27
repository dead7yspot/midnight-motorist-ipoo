/** Item fisico que precisa ser coletado antes de poder lancar granadas. */
public class GranadaColetavel extends Equipamento {
    private final int quantidade;

    /** Prepara uma coleta com a quantidade indicada de granadas. */
    public GranadaColetavel(int quantidade) {
        super("Granadas", "Explosivos contra a blindagem", "ultimate_granada.png");
        this.quantidade = quantidade;
    }

    /** Consulta quantas granadas esta coleta fornece. */
    public int getQuantidade() { return quantidade; }
}
