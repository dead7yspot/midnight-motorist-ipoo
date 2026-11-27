import greenfoot.*;
/** Mundo de instrucoes separado do menu inicial. */
public class TelaInstrucoes extends World {
    private final GerenciadorComandos comandos;
    /** Prepara o guia completo preservando os comandos da partida. */
    public TelaInstrucoes(GerenciadorComandos comandos) {
        super(1024, 572, 1);
        this.comandos = comandos;
        GreenfootImage img = new GreenfootImage("cidade_noite.jpg");
        img.scale(1024, 572);
        img.setColor(new Color(5, 10, 20, 220)); img.fillRect(50, 40, 924, 490);
        setBackground(img);
        showText("MIDNIGHT MOTORIST - INSTRUCOES", 512, 85);
        String coleta = comandos.obterTeclaExibicao("INTERAGIR");
        String granada = comandos.obterTeclaExibicao("LANCAR_GRANADA");
        showText(comandos.obterMovimentoExibicao() + ": mover   |   "
                 + coleta + ": coletar ou atravessar a saida", 512, 145);
        showText(comandos.obterTeclaExibicao("ATACAR") + " / CLIQUE: atacar   |   "
                 + comandos.obterTeclaExibicao("EQP_ANTERIOR") + "/"
                 + comandos.obterTeclaExibicao("EQP_PROX") + ": trocar arma   |   "
                 + comandos.obterTeclaExibicao("ALTERNAR_MUSICA") + ": musica", 512, 190);
        showText("FASE 1: derrote os inimigos; va ate a direita e pressione " + coleta + ".", 512, 245);
        showText("FASE 2: fuga policial. Pegue dinheiro e turbo; evite carros e barreiras.", 512, 290);
        showText("FASE 3: boss ate metade do HP; colete com " + coleta + " e lance com " + granada + ".", 512, 335);
        String iniciar = comandos.obterTeclaExibicao("INICIAR_JOGO");
        if (!"ENTER".equals(iniciar)) iniciar = "ENTER / " + iniciar;
        showText(iniciar + ": JOGAR   |   ESC: VOLTAR AO MENU", 512, 440);
    }
    /** Permite iniciar o jogo ou voltar ao menu. */
    public void act() {
        String tecla = Greenfoot.getKey();
        if ("escape".equals(tecla)) Greenfoot.setWorld(new TelaInicial(comandos));
        else if ("enter".equals(tecla) || "INICIAR_JOGO".equals(comandos.obterComando(tecla)))
            Greenfoot.setWorld(new Fase01(comandos, new EstadoPartida()));
    }
}
