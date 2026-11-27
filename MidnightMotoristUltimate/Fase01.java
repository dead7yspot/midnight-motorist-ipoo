import greenfoot.*;

/** Primeira fase: limpar a rua e encontrar a saida. */
public class Fase01 extends FaseBase {
    /** Permite iniciar a primeira fase diretamente pelo Greenfoot. */
    public Fase01() {
        this(TelaInicial.criarComandosPadrao(), new EstadoPartida());
    }

    /** Cria a primeira fase usando os comandos e o placar da partida. */
    public Fase01(GerenciadorComandos comandos, EstadoPartida estado) {
        super(1, comandos, estado);
        prepararCenario(null);
    }

    /** Mantem a assinatura da versao antiga para facilitar uso no Greenfoot. */
    public Fase01(int largura, int altura, int escala, GerenciadorComandos comandos) {
        this(comandos, new EstadoPartida());
    }

    /** Distribui inimigos, itens, obstaculos e a saida pela rua. */
    private void prepararCenario(Jogador anterior) {
        colocarJogador(anterior, 100, 465);
        adicionarObstaculo("Carro abandonado", "noir_carro_abandonado.png", 370, 352, 147, 92, 57, 16, 27);
        adicionarObstaculo("Arvore", "noir_arvore.png", 228, 203, 104, 116, 26, 12, 43);
        adicionarObstaculo("Poste", "noir_poste.png", 610, 263, 40, 118, 10, 11, 50);
        adicionarObstaculo("Barril", "noir_barril.png", 653, 436, 58, 58, 22, 19, 12);
        adicionarObstaculo("Barril", "noir_barril.png", 490, 285, 58, 58, 22, 19, 12);
        adicionarObstaculo("Arvore", "noir_arvore.png", 848, 193, 104, 116, 26, 12, 43);

        addObject(new ArmaBranca("Estilete", "Arma branca", "estileteJogo.png", 90, 65, 18), 185, 472);
        addObject(new ArmaFogo("Revolver", "Alcance longo", "revolverJogo.png", 80, 395, 16, 22), 550, 478);
        addObject(new Suprimento("Kit medico", 35), 794, 495);
        addObject(new Colete(), 300, 487);
        addObject(new Dinheiro(70), 270, 525);
        addObject(new Dinheiro(120), 745, 504);

        addObject(new InimigoPatrulha("Vigia da Avenida", getJogador(), 410, 580), 520, 401);
        addObject(new InimigoHumano("Perseguidor", 78, 2, getJogador(), 50,
                  new ArmaBranca("Soco", "Ataque corpo a corpo", "facaJogo.png", 180, 54, 9),
                  "novo_capanga_parado.png", "novo_capanga_andar1.png", "novo_capanga_andar2.png",
                  "novo_capanga_soco.png", new String[] {"novo_capanga_andar3.png", "novo_capanga_andar1.png",
                                                         "novo_capanga_andar4.png", "novo_capanga_andar2.png"}), 803, 354);
        addObject(new InimigoPatrulha("Vigia da Rua", getJogador(), 200, 360), 300, 300);
        addObject(new MiniChefe(getJogador()), 909, 433);
        definirSaida(958, 465);
        configurarObjetivoAbates(4);
        avisar("Pegue itens com " + getComandos().obterTeclaExibicao("INTERAGIR")
               + ", derrote tres capangas e o mini-chefe e atravesse a saida!");
    }
}
