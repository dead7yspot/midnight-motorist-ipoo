import greenfoot.*;

/** Placa decorativa que anuncia a saida antes do fim da perseguicao. */
public class PlacaSaidaRodovia extends Actor {
    /** Carrega a placa que anuncia o fim da rodovia. */
    public PlacaSaidaRodovia() {
        setImage(new GreenfootImage("mm_saida_rodovia.png"));
    }

    /** Move a placa para fora da tela conforme a estrada avanca. */
    public void act() {
        if (getX() <= 7) getWorld().removeObject(this);
        else setLocation(getX() - 7, getY());
    }
}
