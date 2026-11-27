import greenfoot.*;
/** O carro policial acompanha a faixa do jogador. */
public class CarroPolicia extends Actor {
    private final GreenfootImage sireneVermelha;
    private final GreenfootImage sireneAzul;
    private int tempoSirene;

    /** Carrega as duas imagens alternadas da sirene. */
    public CarroPolicia() {
        sireneVermelha = criarImagem("noir_carro_policia.png");
        sireneAzul = criarImagem("noir_carro_policia_alt.png");
        setImage(sireneVermelha);
    }
    /** Carrega e ajusta ao mesmo tamanho uma imagem da viatura. */
    private GreenfootImage criarImagem(String arquivo) {
        GreenfootImage imagem = new GreenfootImage(arquivo);
        imagem.scale(111, 70);
        return imagem;
    }
    /** Pisca a sirene e acompanha a faixa e a distancia do jogador. */
    public void act() {
        tempoSirene = (tempoSirene + 1) % 24;
        if (tempoSirene == 0) setImage(sireneVermelha);
        else if (tempoSirene == 12) setImage(sireneAzul);
        java.util.List<CarroJogador> carros = getWorld().getObjects(CarroJogador.class);
        if (!carros.isEmpty()) {
            int dy = carros.get(0).getY() - getY();
            int distancia = ((Fase02) getWorld()).getDistanciaPolicia();
            int x = Math.max(45, carros.get(0).getX() - Math.max(0, distancia / 3));
            setLocation(x, getY() + Integer.signum(dy) * Math.min(2, Math.abs(dy)));
        }
    }
}
