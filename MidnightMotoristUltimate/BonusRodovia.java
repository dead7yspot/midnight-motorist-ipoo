/** Coleta opcional de dinheiro: altera a pontuacao final. */
public class BonusRodovia extends ItemRodovia {
    /** Cria a coleta de dinheiro da rodovia. */
    public BonusRodovia() { super("ultimate_dinheiro.png"); }
    /** Acrescenta pontos sem alterar a distancia da policia. */
    protected void aplicar(Fase02 fase) { fase.bonus(100, 0); }
}
