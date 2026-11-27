import greenfoot.*;

/** Projetil do alvo_meu: ignora o autor, nao atravessa obstaculos e nao atinge aliados. */
public class Projetil extends Actor {
    private final int velocidade, dano;
    private int distanciaRestante;
    private final Personagem atirador;
    private final int desvioVertical;
    private int passos;

    /** Cria um disparo comum que segue em linha reta. */
    public Projetil(int direcao, int velocidade, int dano, int alcance, Personagem atirador) {
        this(direcao, velocidade, dano, alcance, atirador, 0);
    }

    /** Um desvio pequeno separa os tres bagos da espingarda durante o voo. */
    public Projetil(int direcao, int velocidade, int dano, int alcance,
                    Personagem atirador, int desvioVertical) {
        this.velocidade = direcao * velocidade;
        this.dano = dano;
        this.distanciaRestante = alcance;
        this.atirador = atirador;
        this.desvioVertical = desvioVertical;
        GreenfootImage img = new GreenfootImage(12, 5);
        img.setColor(atirador instanceof Jogador ? Color.YELLOW : Color.RED);
        img.fillOval(0, 0, 12, 5);
        setImage(img);
    }

    /** Avanca o projetil e trata impactos, obstaculos e fim do alcance. */
    public void act() {
        if (getWorld() == null) return;
        // Avanca em pequenos passos, para nao atravessar obstaculos ou personagens.
        for (int i = 0; i < Math.abs(velocidade); i++) {
            int proximoX = getX() + Integer.compare(velocidade, 0);
            if (proximoX < 10 || proximoX > getWorld().getWidth()-10) {
                getWorld().removeObject(this);
                return;
            }
            passos++;
            int proximoY = getY() + (passos % 5 == 0 ? desvioVertical : 0);
            if (proximoY < 1 || proximoY >= getWorld().getHeight() - 1) {
                getWorld().removeObject(this);
                return;
            }
            setLocation(proximoX, proximoY);
            distanciaRestante--;
            for (Cenario obj : getWorld().getObjects(Cenario.class)) {
                if (obj.bloqueiaPosicao(getX(), getY(), 4, 4)) {
                    getWorld().removeObject(this);
                    return;
                }
            }
            Personagem atingido = procurarAlvo();
            if (atingido != null) {
                atingido.receberDano(dano);
                Greenfoot.playSound("hit_som.wav");
                getWorld().removeObject(this);
                return;
            }
            if (distanciaRestante <= 0) {
                getWorld().removeObject(this);
                return;
            }
        }
    }

    /** Procura um alvo valido, sem atingir o autor nem seus aliados. */
    private Personagem procurarAlvo() {
        for (Personagem alvo : getObjectsInRange(23, Personagem.class)) {
            if (alvo != atirador && !alvo.estaMorto()
                && ((atirador instanceof Jogador && alvo instanceof Inimigo)
                    || (atirador instanceof Inimigo && alvo instanceof Jogador))
                && Math.abs(getY() - alvo.getY()) < 28) return alvo;
        }
        return null;
    }
}
