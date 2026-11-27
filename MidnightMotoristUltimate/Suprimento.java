/** Suprimento coletavel que recupera a vida quando usado. */
public class Suprimento extends Equipamento {
    private final int cura;
    /** Cria um kit com a quantidade de vida que ele recupera. */
    public Suprimento(String nome, int cura) {
        super(nome, "Recupera a saude", "ultimate_kit.png");
        this.cura = cura;
    }
    /** Consulta a quantidade de vida recuperada. */
    public int getCura() { return cura; }
}
