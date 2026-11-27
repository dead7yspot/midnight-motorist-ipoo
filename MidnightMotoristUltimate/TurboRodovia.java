/** 
 * Turbo/N2o = acrescenta pontos e afasta a viatura do jogador
*/
public class TurboRodovia extends ItemRodovia {
    // Cria a coleta de turbo da rodovia
    public TurboRodovia() { 
        super("ultimate_turbo.png"); 
    }
    
    // Da pontos e aumenta a distancia da viatura
    @Override
    protected void aplicar(Fase02 fase) { 
        fase.bonus(40, 125); 
    }
}
