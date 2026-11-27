import greenfoot.*;

/** Marca decorativa que se move com o asfalto, sem causar colisoes. */
public class FaixaRodovia extends Actor {
    private static final int VELOCIDADE = 7;

    /** Desenha um trecho da marca divisoria da rodovia. */
    public FaixaRodovia() {
        GreenfootImage imagem = new GreenfootImage(70, 4);
        imagem.setColor(new Color(195, 192, 167));
        imagem.fill();
        setImage(imagem);
    }

    /** Move a marca junto com a estrada e a remove ao sair da tela. */
    public void act() {
        if (getX() <= VELOCIDADE) getWorld().removeObject(this);
        else setLocation(getX() - VELOCIDADE, getY());
    }
}
