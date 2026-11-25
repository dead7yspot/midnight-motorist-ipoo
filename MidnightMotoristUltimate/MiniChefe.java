/** Minichefe da unica fase exigida na primeira entrega. */
public class MiniChefe extends Inimigo {
    /** Prepara o inimigo de elite e sua arma de fogo. */
    public MiniChefe(Jogador jogador) {
        super("Capanga de Elite", 50, 1, jogador, 170,
              new ArmaFogo("Revolver de Elite", "Tiros em intervalos", "revolverJogo.png",
                           70, 350, 20, 11),
              "novo_elite_parado.png", "novo_elite_andar1.png", "novo_elite_andar2.png",
              "novo_elite_tiro.png");
        configurarQuadrosCorrida(new String[] {"novo_elite_andar3.png", "novo_elite_andar1.png",
                                               "novo_elite_andar4.png", "novo_elite_andar2.png"});
        ajustarRaioVisao(510);
        ajustarIntervaloAtaque(115);
    }
    /** Define a recompensa por derrotar o minichefe. */
    @Override
    protected int pontosPorMorte() { return 300; }
}
