/**
 * Segundo comportamento de inimigo: patrulha entre dois pontos e
 * so persegue o jogador quando ele entra perto da patrulha.
 */
public class InimigoPatrulha extends Inimigo {
    private final int limiteEsquerdo;
    private final int limiteDireito;
    private int sentido = 1;

    /** Configura o trecho patrulhado e o ataque corpo a corpo. */
    public InimigoPatrulha(String nome, Jogador jogador, int esquerda, int direita) {
        super(nome, 65, 2, jogador, 42,
              new ArmaBranca("Soco", "Ataque curto", "facaJogo.png", 500, 49, 8),
              "novo_vigia_parado.png", "novo_vigia_andar1.png", "novo_vigia_andar2.png",
              "novo_vigia_soco.png");
        configurarQuadrosCorrida(new String[] {"novo_vigia_andar3.png", "novo_vigia_andar1.png",
                                               "novo_vigia_andar4.png", "novo_vigia_andar2.png"});
        limiteEsquerdo = esquerda;
        limiteDireito = direita;
        ajustarRaioVisao(90);
        ajustarIntervaloAtaque(78);
    }

    /** Patrulha entre os limites ou persegue o jogador quando ele se aproxima. */
    @Override
    protected boolean moverInimigo() {
        Jogador jogador = obterJogador();
        if (jogador != null && Math.abs(getX()-jogador.getX()) < 78
            && Math.abs(getY()-jogador.getY()) < 62) return super.moverInimigo();
        if (getX() >= limiteDireito) sentido = -1;
        if (getX() <= limiteEsquerdo) sentido = 1;
        atualizarDirecao(sentido);
        boolean andou = moverComColisao(sentido * getVelocidade(), 0);
        if (!andou) sentido = -sentido;
        return andou;
    }

    /** Define a recompensa por derrotar este vigia. */
    @Override
    protected int pontosPorMorte() { return 150; }
}
