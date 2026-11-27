import greenfoot.*;

/** Tela inicial com os comandos principais e acesso as instrucoes completas. */
public class TelaInicial extends World {
    private final GerenciadorComandos comandos;

    /** Abre o menu com os comandos padrao em uma partida nova. */
    public TelaInicial() { 
        this(criarComandosPadrao()); 
    }

    /** Abre o menu preservando eventuais teclas reconfiguradas. */
    public TelaInicial(GerenciadorComandos comandos) {
        super(1024, 572, 1);
        this.comandos = comandos;
        GerenciadorAudio.iniciarMusica("Relaxed Scene.mp3");
        criarImagem();
    }

    /** Cria o cadastro inicial de teclas do jogo. */
    public static GerenciadorComandos criarComandosPadrao() {
        GerenciadorComandos cmds = new GerenciadorComandos();
        criarComandos(cmds);
        return cmds;
    }

    /** Le as opcoes de jogar, instrucoes, configuracao e musica. */
    public void act() {
        String tecla = Greenfoot.getKey();
        
        if (tecla == null) return;
        String comando = comandos.obterComando(tecla);
        
        if ("ALTERNAR_MUSICA".equals(comando)) 
            GerenciadorAudio.alternarMusica();
        
        if ("INICIAR_JOGO".equals(comando)) {
            Greenfoot.setWorld(new Fase01(comandos, new EstadoPartida()));
        } else if ("INSTRUCOES".equals(comando)) {
            Greenfoot.setWorld(new TelaInstrucoes(comandos));
        } else if ("CONFIG".equals(comando)) {
            Greenfoot.setWorld(new MenuConfiguracoes(comandos));
        }
    }

    /** Monta a capa e escreve as instrucoes resumidas. */
    private void criarImagem() {
        GreenfootImage fundo = ArteTelas.criarInicio();
        setBackground(fundo);
        String movimento = comandos.obterMovimentoExibicao();
        desenhar(teclaPrincipal("INICIAR_JOGO") + " - JOGAR", 22, Color.WHITE, 41, 480);
        desenhar(teclaPrincipal("INSTRUCOES") + " - INSTRUCOES  |  "
                 + teclaPrincipal("CONFIG") + " - CONFIGURAR", 16,
                 new Color(173, 220, 220), 41, 530);
        desenhar(movimento + ": MOVER  |  " + teclaPrincipal("INTERAGIR") + ": COLETAR",
                 17, Color.WHITE, 390, 480);
        desenhar(teclaPrincipal("ATACAR") + " / CLIQUE: ATACAR  |  "
                 + teclaPrincipal("EQP_ANTERIOR") + "/" + teclaPrincipal("EQP_PROX") + ": TROCAR ARMA",
                 17, Color.WHITE, 390, 508);
        desenhar(teclaPrincipal("LANCAR_GRANADA") + ": GRANADA  |  "
                 + teclaPrincipal("ALTERNAR_MUSICA") + ": MUSICA  |  "
                 + teclaPrincipal("INSTRUCOES") + ": GUIA COMPLETO",
                 17, Color.WHITE, 390, 535);
    }

    /** Consulta a tecla principal formatada para a capa. */
    private String teclaPrincipal(String comando) {
        return comandos.obterTeclaExibicao(comando);
    }

    /** Acrescenta uma linha de texto sobre a capa. */
    private void desenhar(String texto, int tamanho, Color cor, int x, int y) {
        getBackground().drawImage(
            new GreenfootImage(texto, tamanho, cor, null), x, y
        );
    }
    /** Registra as teclas padrao e seus atalhos alternativos. */
    private static void criarComandos(GerenciadorComandos cmds) {

        // Movimentacao
        cmds.adicionarComando(
            "MOVER_CIMA",
            "w",
            "Andar para cima",
            "DIRECIONAIS"
        );
        cmds.adicionarTecla("MOVER_CIMA", "up");

        cmds.adicionarComando(
            "MOVER_BAIXO",
            "s",
            "Andar para baixo",
            "DIRECIONAIS"
        );
        cmds.adicionarTecla("MOVER_BAIXO", "down");

        cmds.adicionarComando(
            "MOVER_ESQUERDA",
            "a",
            "Andar para esquerda",
            "DIRECIONAIS"
        );
        cmds.adicionarTecla("MOVER_ESQUERDA", "left");

        cmds.adicionarComando(
            "MOVER_DIREITA",
            "d",
            "Andar para direita",
            "DIRECIONAIS"
        );
        cmds.adicionarTecla("MOVER_DIREITA", "right");

        // Acoes
        cmds.adicionarComando(
            "INTERAGIR",
            "e",
            "Interagir",
            "ACOES"
        );

        cmds.adicionarComando(
            "INICIAR_JOGO",
            "enter",
            "Iniciar",
            "ACOES"
        );

        cmds.adicionarComando(
            "CONFIG",
            "n",
            "Configuracoes",
            "ACOES"
        );

        cmds.adicionarComando(
            "ATACAR",
            "space",
            "Usar um armamento",
            "ACOES"
        );

        // Interacao com o mundo
        cmds.adicionarComando(
            "EQP_ANTERIOR",
            "q",
            "Equipamento anterior",
            "INTERACAO"
        );

        cmds.adicionarComando(
            "EQP_PROX",
            "r",
            "Equipamento a seguir",
            "INTERACAO"
        );

        cmds.adicionarComando(
            "ALTERNAR_MUSICA",
            "m",
            "Pausar musica",
            "ACOES"
        );

        cmds.adicionarComando(
            "INSTRUCOES",
            "i",
            "Abrir instrucoes",
            "ACOES"
        );
        cmds.adicionarComando(
            "LANCAR_GRANADA",
            "g",
            "Lancar granada",
            "ACOES"
        );
    }
}
