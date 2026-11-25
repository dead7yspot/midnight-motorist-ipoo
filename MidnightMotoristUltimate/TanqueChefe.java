import greenfoot.*;

/** Segunda forma do chefe, blindada contra armas comuns. */
public class TanqueChefe extends Inimigo {
    /** Prepara a forma blindada, suas animacoes e seu canhao. */
    public TanqueChefe(Jogador jogador, ArmaFogo tanqueArma) {
        super("Tanque da mafia", 100, 1, jogador, 205, tanqueArma,
              "novo_tanque_parado.png", "novo_tanque_andar1.png", "novo_tanque_andar2.png",
              "novo_tanque_tiro.png");
        configurarEscalaVisual(100);
        ajustarRaioVisao(900);
        ajustarIntervaloAtaque(108);
    }

    // Ignora os danos de socos e armas comuns por causa da blindagem
    @Override
    public void receberDano(int dano) {
        // A blindagem impede que tiros e socos resolvam a segunda etapa.
    }

    /** Unico meio de diminuir a vida da blindagem: granadas coletadas. */
    public void receberGranada() { 
        super.receberDano(43); 
    }

    /** Define a recompensa por derrotar o tanque. */
    @Override
    protected int pontosPorMorte() { return 900; }
}
