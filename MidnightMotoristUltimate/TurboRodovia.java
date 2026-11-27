/** Turbo afastando a viatura e gerando pontos. */
public class TurboRodovia extends ItemRodovia {
    /** Cria a coleta de turbo da rodovia. */
    public TurboRodovia() { super("ultimate_turbo.png"); }
    /** Da pontos e aumenta a distancia da viatura. */
    protected void aplicar(Fase02 fase) { fase.bonus(40, 125); }
}
