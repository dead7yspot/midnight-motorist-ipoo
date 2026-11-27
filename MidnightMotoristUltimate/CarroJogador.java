import greenfoot.*;

/** Carro controlado pelas mesmas teclas configuraveis do personagem. */
public class CarroJogador extends Actor {
    private final GerenciadorComandos comandos;
    private int protecao;
    /** Prepara o carro e reaproveita as teclas configuradas. */
    public CarroJogador(GerenciadorComandos comandos) {
        this.comandos = comandos;
        GreenfootImage img = new GreenfootImage("noir_carro_jogador.png");
        img.scale(118, 72);
        setImage(img);
    }
    /** Verifica se alguma tecla do comando esta pressionada. */
    private boolean pressionado(String nome) {
        for (String tecla : comandos.obterTeclas(nome)) if (Greenfoot.isKeyDown(tecla)) return true;
        return false;
    }
    /** Move o carro na pista e comunica colisoes a fase. */
    public void act() {
        int x = getX(), y = getY();
        if (pressionado("MOVER_CIMA")) y -= 5;
        if (pressionado("MOVER_BAIXO")) y += 5;
        if (pressionado("MOVER_ESQUERDA")) x -= 4;
        if (pressionado("MOVER_DIREITA")) x += 4;
        setLocation(Math.max(105, Math.min(500, x)), Math.max(125, Math.min(450, y)));
        if (protecao > 0) protecao--;
        ObstaculoRodovia obstaculo = (ObstaculoRodovia) getOneIntersectingObject(ObstaculoRodovia.class);
        if (obstaculo != null && protecao == 0) {
            getWorld().removeObject(obstaculo);
            ((Fase02) getWorld()).colisao();
            protecao = 45;
        }
    }
}
