import greenfoot.*;

/** arma Granada com alcance limitado e efeito visual
 * funciona diferente de um armamento normal, então herda de Actor ao inves de Armamento
*/
public class Granada extends Actor {
    private final int direcao;
    private final int alturaInicial;
    private int distanciaRestante = 590;
    private int passos;

    /** 
     *  Prepara uma granada com direcao e altura iniciais 
    */
    public Granada(int direcao, int yInicial) {
        this.direcao = direcao;
        this.alturaInicial = yInicial;
        GreenfootImage img = new GreenfootImage("ultimate_granada.png");
        img.scale(23, 24);
        setImage(img);
    }

    // Move a granada em arco e aplica o impacto uma vez no tanque
    @Override
    public void act() {
        if (getWorld() == null) 
            return;
        passos++;
        
        int proximoX = getX() + direcao * 9;
        distanciaRestante -= 9;
        int arco = (int) Math.round(20 * Math.sin(Math.PI * Math.min(passos, 45) / 45.0));
        
        if (proximoX < 10 || proximoX > getWorld().getWidth() - 10 || distanciaRestante <= 0) {
            getWorld().removeObject(this);
            return;
        }
        
        setLocation(proximoX, alturaInicial - arco);
        TanqueChefe tanque = (TanqueChefe) getOneIntersectingObject(TanqueChefe.class);
        
        if (tanque != null) {
            int x = getX(), y = getY();
            World mundo = getWorld();
        
            // o dano e aplicado uma unica vez
            tanque.receberGranada(27);
            
            mundo.addObject(new Explosao(), x, y);
            Greenfoot.playSound("granada.wav");
            mundo.removeObject(this);
        }
    }
}
