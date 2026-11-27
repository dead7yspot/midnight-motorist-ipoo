import greenfoot.*;

/** Superclasse dos itens da perseguicao, coletados pelo carro. */
public abstract class ItemRodovia extends Actor {
    /** Carrega e dimensiona a imagem compartilhada pelos itens da pista. */
    public ItemRodovia(String imagem) {
        GreenfootImage sprite = new GreenfootImage(imagem);
        sprite.scale(54, 42);
        setImage(sprite);
    }

    /** Desloca o item e aplica seu efeito quando o carro o coleta. */
    public void act() {
        if (getWorld() == null) return;
        if (isTouching(CarroJogador.class)) {
            aplicar((Fase02) getWorld());
            getWorld().removeObject(this);
        } else if (getX() < 8) getWorld().removeObject(this);
        else setLocation(getX() - 6, getY());
    }

    /** Cada tipo de item define seu proprio efeito na perseguicao. */
    protected abstract void aplicar(Fase02 fase);
}
