import greenfoot.*;

/** Primeira forma do chefe. Na metade da vida, ele troca para o tanque. */
public class ChefeFinal extends Inimigo {
    private boolean entrouNoTanque;

    /** Prepara o chefao humano com sua shotgun de tres projeteis. */
    public ChefeFinal(Jogador jogador) {
        super("Chefe da Gangue", 235, 2, jogador, 170,
              new ArmaFogo("Shotgun do Chefe", "Dispara tres bagos", "revolverJogo.png",
                           300, 380, 300, 16, 24, 3),
              "novo_chefe_parado.png", "novo_chefe_andar1.png", "novo_chefe_andar2.png",
              "novo_chefe_tiro.png");
        configurarQuadrosCorrida(new String[] {"novo_chefe_andar3.png", "novo_chefe_andar1.png",
                                               "novo_chefe_andar4.png", "novo_chefe_andar2.png"});
        ajustarRaioVisao(740);
        ajustarIntervaloAtaque(80);
    }

    /** Troca para o tanque quando o proximo dano alcancaria metade da vida. */
    @Override
    public void receberDano(int dano) {
        if (entrouNoTanque || estaMorto() || getWorld() == null || dano <= 0) return;
        if (getVida() - dano <= getVidaMaxima() / 2) {
            entrouNoTanque = true;
            World mundo = getWorld();
            if (mundo instanceof Fase03) {
                ((Fase03) mundo).iniciarFaseTanque(this, obterJogador());
            }
            return;
        }
        super.receberDano(dano);
    }

    /** Define a recompensa associada a esta forma do chefao. */
    @Override
    protected int pontosPorMorte() { return 500; }
}
