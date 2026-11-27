import greenfoot.*;

/** Carros e barreiras que passam pela rodovia e devem ser evitados. */
public class ObstaculoRodovia extends Actor {
    private final int velocidade;
    /** Cria um carro ou bloqueio com imagem e velocidade adequadas. */
    public ObstaculoRodovia(boolean veiculo) {
        String nome = "ultimate_barreira.png";
        if (veiculo) {
            String[] carros = {"noir_carro_transito.png", "transito_azul.png",
                               "transito_verde.png", "transito_roxo.png"};
            nome = carros[Greenfoot.getRandomNumber(carros.length)];
        }
        GreenfootImage imagem = new GreenfootImage(nome);
        imagem.scale(veiculo ? 102 : 72, veiculo ? 60 : 53);
        setImage(imagem);
        velocidade = veiculo ? 6 : 7;
    }
    /** Desloca o obstaculo e o remove quando sai da tela. */
    public void act() {
        if (getWorld() == null) return;
        if (getX() <= velocidade + 1) getWorld().removeObject(this);
        else setLocation(getX() - velocidade, getY());
    }
}
