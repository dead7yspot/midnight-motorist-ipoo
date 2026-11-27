import greenfoot.*;

/** Efeito de explosao, sem causar dano repetido. */
public class Explosao extends Actor {
    private int tempo;

    /** Inicia o efeito visual em tamanho pequeno. */
    public Explosao() {
        desenhar(10);
    }

    /** Expande a explosao por alguns ciclos e depois a remove. */
    @Override
    public void act() {
        tempo++;
        if (tempo >= 15) {
            getWorld().removeObject(this);
            return;
        }
        desenhar(12 + tempo * 4);
    }

    /** Desenha os circulos coloridos do efeito. */
    private void desenhar(int raio) {
        GreenfootImage imagem = new GreenfootImage(132, 132);
        imagem.setColor(new Color(245, 109 + (tempo % 3) * 20, 37, 180));
        imagem.fillOval(66-raio/2, 66-raio/2, raio, raio);
        imagem.setColor(new Color(255, 224, 104, 210));
        imagem.fillOval(66-raio/3, 66-raio/3, raio*2/3, raio*2/3);
        setImage(imagem);
    }
}
