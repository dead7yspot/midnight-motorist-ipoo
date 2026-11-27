import greenfoot.*;
import greenfoot.core.WorldHandler;
import greenfoot.core.Simulation;
import greenfoot.platforms.ActorDelegate;
import greenfoot.platforms.GreenfootUtilDelegate;
import greenfoot.util.GreenfootUtil;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import javafx.scene.input.KeyCode;

/** Verificacoes automaticas com a API instalada, sem substituir as classes Greenfoot. */
public class TesteIntegracao {
    private static class Recursos implements GreenfootUtilDelegate {
        /** Localiza recursos do projeto e as bibliotecas da instalacao. */
        public URL getResource(String nome) {
            try {
                File arquivo = new File(nome);
                if (!arquivo.isFile()) return getClass().getClassLoader().getResource(nome);
                return arquivo.toURI().toURL();
            } catch (Exception erro) { throw new RuntimeException(erro); }
        }
        /** Retorna uma lista vazia para o cadastro opcional de sons. */
        public Iterable<String> getSoundFiles() { return new ArrayList<String>(); }
        /** Fornece uma imagem para a inicializacao dos atores. */
        public String getGreenfootLogoPath() { return "images/mm_jogador_parado.png"; }
        /** Informa que o armazenamento online nao e usado no teste. */
        public boolean isStorageSupported() { return false; }
        /** Nao ha usuario online no teste local. */
        public UserInfo getCurrentUserInfo() { return null; }
        /** Nao grava dados de usuario durante o teste. */
        public boolean storeCurrentUserInfo(UserInfo usuario) { return false; }
        /** Nao consulta ranking online no teste local. */
        public List<UserInfo> getTopUserInfo(int quantidade) { return new ArrayList<UserInfo>(); }
        /** Nao solicita imagem de usuario no teste local. */
        public GreenfootImage getUserImage(String nome) { return null; }
        /** Nao ha nome de usuario online no teste local. */
        public String getUserName() { return null; }
        /** Nao consulta usuarios proximos no teste local. */
        public List<UserInfo> getNearbyUserInfo(int quantidade) { return new ArrayList<UserInfo>(); }
    }

    private static class ImagensAtor implements ActorDelegate {
        /** Deixa cada ator carregar a imagem definida pelo proprio jogo. */
        public GreenfootImage getImage(String nome) { return null; }
    }

    /** Interrompe o teste quando uma condicao esperada nao e atendida. */
    private static void conferir(boolean condicao, String mensagem) {
        if (!condicao) throw new AssertionError(mensagem);
        System.out.println("OK: " + mensagem);
    }

    /** Simula uma digitacao de Enter nas telas de transicao. */
    private static void apertarEnter() {
        WorldHandler.getInstance().getKeyboardManager().keyPressed(KeyCode.ENTER, "\n");
        WorldHandler.getInstance().getKeyboardManager().keyReleased(KeyCode.ENTER, "\n");
    }

    /** Limpa o estado pendente de Enter depois de uma transicao. */
    private static void soltarEnter() {
        WorldHandler.getInstance().getKeyboardManager().clearLatchedKeys();
    }

    /** Simula uma tecla mantida pressionada. */
    private static void pressionar(KeyCode codigo, String letra) {
        WorldHandler.getInstance().getKeyboardManager().keyPressed(codigo, letra);
    }

    /** Libera a tecla simulada e limpa eventos pendentes. */
    private static void soltar(KeyCode codigo, String letra) {
        WorldHandler.getInstance().getKeyboardManager().keyReleased(codigo, letra);
        WorldHandler.getInstance().getKeyboardManager().clearLatchedKeys();
        Greenfoot.getKey();
    }

    /** Envia uma tecla completa para a tela atual, como na navegacao do menu. */
    private static void digitar(KeyCode codigo, String letra) {
        pressionar(codigo, letra);
        WorldHandler.getInstance().getKeyboardManager().keyReleased(codigo, letra);
        WorldHandler.getInstance().getWorld().act();
        WorldHandler.getInstance().getKeyboardManager().clearLatchedKeys();
        Greenfoot.getKey();
    }

    /** Salva uma imagem do mundo renderizado, incluindo atores e HUD. */
    private static void salvarMundo(World mundo, File arquivo) throws Exception {
        BufferedImage imagem = new BufferedImage(mundo.getWidth(), mundo.getHeight(), BufferedImage.TYPE_INT_ARGB);
        new greenfoot.gui.WorldRenderer().renderWorld(mundo, imagem);
        ImageIO.write(imagem, "png", arquivo);
    }

    /** Compara duas poses sem depender do nome interno dos arquivos. */
    private static boolean imagensIguais(GreenfootImage primeira, GreenfootImage segunda) {
        if (primeira.getWidth() != segunda.getWidth() || primeira.getHeight() != segunda.getHeight()) return false;
        for (int y = 0; y < primeira.getHeight(); y++) {
            for (int x = 0; x < primeira.getWidth(); x++) {
                if (primeira.getAwtImage().getRGB(x, y) != segunda.getAwtImage().getRGB(x, y)) return false;
            }
        }
        return true;
    }

    /** Compara uma regiao especifica sem depender do restante da tela. */
    private static boolean regioesIguais(GreenfootImage primeira, GreenfootImage segunda,
                                         int x, int y, int largura, int altura) {
        for (int py = y; py < y + altura; py++) {
            for (int px = x; px < x + largura; px++) {
                if (primeira.getAwtImage().getRGB(px, py) != segunda.getAwtImage().getRGB(px, py))
                    return false;
            }
        }
        return true;
    }

    /** Reproduz o alinhamento esperado da pose ampliada para a verificacao visual. */
    private static GreenfootImage poseAmpliada(String arquivo) {
        GreenfootImage pose = new GreenfootImage(arquivo);
        int largura = pose.getWidth() * 135 / 100;
        int altura = pose.getHeight() * 135 / 100;
        pose.scale(largura, altura);
        GreenfootImage alinhada = new GreenfootImage(largura, altura);
        alinhada.drawImage(pose, 0, 24 * (100 - 135) / 100);
        return alinhada;
    }

    /** Resume os pixels da imagem para comparar as cores dos carros de trafego. */
    private static int assinatura(GreenfootImage imagem) {
        int resultado = 1;
        for (int y = 0; y < imagem.getHeight(); y++) {
            for (int x = 0; x < imagem.getWidth(); x++) {
                resultado = 31 * resultado + imagem.getAwtImage().getRGB(x, y);
            }
        }
        return resultado;
    }

    /** Verifica se uma etiqueta do HUD contem exatamente o texto esperado. */
    private static boolean hudContem(World mundo, String texto, int tamanho, Color cor) {
        GreenfootImage esperado = new GreenfootImage(texto, tamanho, cor, null);
        for (TextoHUD etiqueta : mundo.getObjects(TextoHUD.class))
            if (imagensIguais(etiqueta.getImage(), esperado)) return true;
        return false;
    }

    /** Conta os pixels de texto desenhados sobre a faixa superior da rodovia. */
    private static int pixelsHudRodovia(World mundo) {
        BufferedImage renderizada = new BufferedImage(mundo.getWidth(), mundo.getHeight(), BufferedImage.TYPE_INT_ARGB);
        new greenfoot.gui.WorldRenderer().renderWorld(mundo, renderizada);
        BufferedImage fundo = mundo.getBackground().getAwtImage();
        int diferentes = 0;
        for (int y = 0; y < 45; y++) {
            for (int x = 0; x < mundo.getWidth(); x++)
                if (renderizada.getRGB(x, y) != fundo.getRGB(x, y)) diferentes++;
        }
        return diferentes;
    }

    /** Exercita controles remapeados, menu, audio e combate sem substituir a API. */
    private static void verificarAuditoria(File saida) throws Exception {
        GerenciadorComandos configurados = TelaInicial.criarComandosPadrao();
        conferir(!configurados.reBind("INTERAGIR", "e", "g")
                 && !configurados.reBind("INTERAGIR", "e", "i"),
                 "Granada e instrucoes impedem associacoes conflitantes");
        conferir(configurados.reBind("MOVER_CIMA", "w", "t")
                 && configurados.obterTeclas("MOVER_CIMA").get(0).equals("t")
                 && configurados.obterTeclas("MOVER_CIMA").get(1).equals("up"),
                 "Remapear a tecla principal preserva a ordem e a alternativa");
        ArrayList<String> copia = configurados.obterTeclas("MOVER_CIMA");
        copia.clear();
        conferir(configurados.obterTeclas("MOVER_CIMA").size() == 2,
                 "A lista de teclas e protegida contra alteracoes externas");
        conferir(configurados.reBind("INTERAGIR", "e", "f")
                 && configurados.reBind("INSTRUCOES", "i", "h")
                 && configurados.reBind("LANCAR_GRANADA", "g", "v")
                 && configurados.reBind("ALTERNAR_MUSICA", "m", "b"),
                 "Coleta, instrucoes, granada e musica podem ser remapeadas");

        TelaInicial capa = new TelaInicial(configurados);
        Greenfoot.setWorld(capa);
        salvarMundo(capa, new File(saida, "auditoria-menu-remapeado.png"));
        digitar(KeyCode.H, "h");
        conferir(WorldHandler.getInstance().getWorld() instanceof TelaInstrucoes,
                 "Tecla remapeada abre o guia de instrucoes");
        salvarMundo(WorldHandler.getInstance().getWorld(), new File(saida, "auditoria-instrucoes-remapeadas.png"));

        GerenciadorComandos paraMenu = TelaInicial.criarComandosPadrao();
        MenuConfiguracoes menu = new MenuConfiguracoes(paraMenu);
        Greenfoot.setWorld(menu);
        ArrayList<String> nomes = paraMenu.obterIdentificadores();
        Collections.sort(nomes);
        for (int i = 0; i < nomes.indexOf("MOVER_CIMA"); i++) digitar(KeyCode.DOWN, "");
        GreenfootImage selecaoPrincipal = new GreenfootImage(menu.getBackground());
        digitar(KeyCode.RIGHT, "");
        conferir(!imagensIguais(selecaoPrincipal, menu.getBackground()),
                 "Menu destaca qual das teclas alternativas esta selecionada");
        salvarMundo(menu, new File(saida, "auditoria-configuracoes.png"));
        digitar(KeyCode.ENTER, "\n");
        digitar(KeyCode.T, "t");
        conferir(paraMenu.obterTeclas("MOVER_CIMA").get(0).equals("w")
                 && paraMenu.obterTeclas("MOVER_CIMA").get(1).equals("t"),
                 "Menu altera a tecla alternativa escolhida, sem mudar a principal");

        EstadoPartida estado = new EstadoPartida();
        estado.avancarFase();
        estado.avancarFase();
        Fase03 fase = new Fase03(configurados, estado, null);
        Greenfoot.setWorld(fase);
        Jogador jogador = fase.getJogador();
        fase.getObjects(ChefeFinal.class).get(0).receberDano(118);
        fase.act();
        conferir(hudContem(fase, "TANQUE HP: 170 | F: pegar granadas  V: lancar",
                           13, new Color(175, 217, 190)),
                 "HUD da fase final acompanha as teclas remapeadas");
        GranadaColetavel coleta = fase.getObjects(GranadaColetavel.class).get(0);
        jogador.setLocation(coleta.getX(), coleta.getY());
        pressionar(KeyCode.F, "f");
        jogador.act();
        soltar(KeyCode.F, "f");
        jogador.act();
        int granadas = jogador.getGranadas();
        conferir(granadas == 2, "Coleta remapeada funciona na fase final");
        pressionar(KeyCode.G, "g");
        jogador.act();
        soltar(KeyCode.G, "g");
        conferir(jogador.getGranadas() == granadas, "Tecla antiga nao lanca granada depois do remapeamento");
        pressionar(KeyCode.V, "v");
        jogador.act();
        soltar(KeyCode.V, "v");
        conferir(jogador.getGranadas() == granadas - 1 && fase.getObjects(Granada.class).size() == 1,
                 "Tecla remapeada lanca e consome a granada");

        Fase02 estrada = new Fase02(configurados, new EstadoPartida(), jogador);
        Greenfoot.setWorld(estrada);
        conferir(pixelsHudRodovia(estrada) > 100,
                 "Rodovia mostra o placar desde o primeiro quadro");
        if (GerenciadorAudio.estaPausada()) GerenciadorAudio.alternarMusica();
        pressionar(KeyCode.B, "b");
        estrada.act();
        estrada.act();
        conferir(GerenciadorAudio.estaPausada(),
                 "Musica remapeada pausa na perseguicao sem alternar a cada ciclo");
        soltar(KeyCode.B, "b");
        estrada.act();
        GerenciadorAudio.iniciarMusica("Relaxed Scene.mp3");
        conferir(GerenciadorAudio.estaPausada(), "Pausa da musica e preservada ao trocar de tela");
        pressionar(KeyCode.B, "b");
        estrada.act();
        soltar(KeyCode.B, "b");
        conferir(!GerenciadorAudio.estaPausada(), "Musica pode ser retomada durante a perseguicao");

        Fase01 combate = new Fase01(TelaInicial.criarComandosPadrao(), new EstadoPartida());
        Greenfoot.setWorld(combate);
        Jogador lutador = combate.getJogador();
        lutador.setLocation(185, 472);
        pressionar(KeyCode.E, "e");
        lutador.act();
        soltar(KeyCode.E, "e");
        lutador.act();
        lutador.setLocation(210, 472);
        Inimigo alvo = combate.getObjects(Inimigo.class).get(0);
        alvo.setLocation(250, 472);
        int vidaAnterior = alvo.getVida();
        int pontosAnteriores = combate.getEstado().getPontos();
        for (int golpe = 0; golpe < 90 && alvo.getWorld() != null; golpe++) {
            pressionar(KeyCode.SPACE, " ");
            lutador.act();
            soltar(KeyCode.SPACE, " ");
            if (golpe == 0) conferir(alvo.getVida() < vidaAnterior, "Faca causa dano por um ataque real");
            for (int ciclo = 0; ciclo < 25; ciclo++) lutador.act();
        }
        conferir(alvo.getWorld() == null && combate.getEstado().getPontos() > pontosAnteriores,
                 "Golpes reais derrotam o inimigo e somam pontos");

        Fase03 tiros = new Fase03(TelaInicial.criarComandosPadrao(), new EstadoPartida(), null);
        Greenfoot.setWorld(tiros);
        ChefeFinal atirador = tiros.getObjects(ChefeFinal.class).get(0);
        Jogador vitima = tiros.getJogador();
        atirador.setLocation(320, 250);
        vitima.setLocation(500, 250);
        int vidaAntesTiro = vitima.getVida();
        ArmaFogo espingarda = new ArmaFogo("Teste", "", "revolverJogo.png", 20, 380, 20, 16, 24, 3);
        atirador.usarArma(espingarda);
        for (int ciclo = 0; ciclo < 30; ciclo++) {
            for (Projetil projetil : tiros.getObjects(Projetil.class)) projetil.act();
        }
        int danoTiro = vidaAntesTiro - vitima.getVida();
        conferir(danoTiro >= 16 && danoTiro <= 24,
                 "Projetil da shotgun atinge o jogador e respeita a invulnerabilidade");

        EstadoPartida estadoFinal = new EstadoPartida();
        estadoFinal.avancarFase();
        estadoFinal.avancarFase();
        Fase03 confronto = new Fase03(TelaInicial.criarComandosPadrao(), estadoFinal, null);
        Greenfoot.setWorld(confronto);
        ChefeFinal chefe = confronto.getObjects(ChefeFinal.class).get(0);
        chefe.setLocation(650, 350);
        chefe.receberDano(118);
        TanqueChefe tanque = confronto.getObjects(TanqueChefe.class).get(0);
        Jogador heroi = confronto.getJogador();
        heroi.setLocation(480, 350);
        heroi.adicionarGranadas(4);
        for (int lancamento = 0; lancamento < 4; lancamento++) {
            pressionar(KeyCode.G, "g");
            heroi.act();
            soltar(KeyCode.G, "g");
            for (int ciclo = 0; ciclo < 35 && !confronto.getObjects(Granada.class).isEmpty(); ciclo++)
                confronto.getObjects(Granada.class).get(0).act();
            if (lancamento < 3) {
                conferir(tanque.getVida() == 170 - 43 * (lancamento + 1),
                         "Granada lancada reduz a blindagem " + (lancamento + 1));
                for (int ciclo = 0; ciclo < 36; ciclo++) heroi.act();
            }
        }
        conferir(WorldHandler.getInstance().getWorld() instanceof TelaVitoria,
                 "Quatro granadas lancadas de verdade concluem o confronto");
    }

    /** Executa o percurso e as verificacoes usando a API instalada do Greenfoot. */
    public static void main(String[] args) throws Exception {
        bluej.Config.initializeStandalone(new bluej.BlueJPropStringSource() {
            /** Devolve o valor padrao das preferencias durante o teste. */
            public String getBlueJPropertyString(String nome, String padrao) { return padrao; }
            /** Usa o proprio identificador para textos internos da plataforma. */
            public String getLabel(String nome) { return nome; }
        });
        GreenfootUtil.initialise(new Recursos());
        ActorVisitor.setDelegate(new ImagensAtor());
        WorldHandler.initialise();
        Simulation.initialize();
        File saida = new File(System.getProperty("java.io.tmpdir"), "midnight-telas-teste");
        saida.mkdirs();
        ImageIO.write(ArteTelas.criarInicio().getAwtImage(), "png", new File(saida, "inicio.png"));
        for (int fase = 1; fase <= 3; fase++) {
            ImageIO.write(ArteTelas.criarResultado(fase, true).getAwtImage(), "png",
                          new File(saida, "vitoria" + fase + ".png"));
            ImageIO.write(ArteTelas.criarResultado(fase, false).getAwtImage(), "png",
                          new File(saida, "derrota" + fase + ".png"));
            GreenfootImage vitoria = new GreenfootImage("cinema_vitoria" + fase + ".png");
            GreenfootImage derrota = new GreenfootImage("cinema_derrota" + fase + ".png");
            vitoria.scale(1024, 572);
            derrota.scale(1024, 572);
            conferir(imagensIguais(ArteTelas.criarResultado(fase, true), vitoria)
                     && imagensIguais(ArteTelas.criarResultado(fase, false), derrota),
                     "Cada resultado da fase " + fase + " usa a arte cinematografica correspondente");
        }
        conferir(true, "Sete telas cinematograficas carregadas com a API Greenfoot");

        ArmaFogo shotgun = new ArmaFogo("Shotgun", "", "revolverJogo.png", 1000, 380, 1000, 16, 24, 3);
        ArmaFogo canhao = new ArmaFogo("Canhao", "", "revolverJogo.png", 1000, 600, 1000, 32, 40, 1);
        for (int i = 0; i < 500; i++) {
            int tiro = shotgun.usar();
            int explosao = canhao.usar();
            if (tiro < 16 || tiro > 24 || explosao < 32 || explosao > 40 || explosao <= tiro)
                throw new AssertionError("Faixas de dano inesperadas");
        }
        conferir(shotgun.getQuantidadeProjetis() == 3 && canhao.getQuantidadeProjetis() == 1,
                 "Shotgun dispara tres projeteis e canhao tem sempre mais dano por acerto");

        GerenciadorComandos comandos = TelaInicial.criarComandosPadrao();
        TelaInicial menu = new TelaInicial(comandos);
        GreenfootImage capaEsperada = ArteTelas.criarInicio();
        GreenfootImage controlePadrao = new GreenfootImage(
            "W/A/S/D / SETAS: MOVER  |  E: COLETAR", 17, Color.WHITE, null);
        capaEsperada.drawImage(controlePadrao, 390, 480);
        conferir(regioesIguais(menu.getBackground(), capaEsperada, 390, 480,
                              controlePadrao.getWidth(), controlePadrao.getHeight()),
                 "Capa mostra E para coletar, conforme o comando padrao");
        GerenciadorComandos remapeados = TelaInicial.criarComandosPadrao();
        conferir(remapeados.reBind("INTERAGIR", "e", "f"), "Comando de coleta pode ser remapeado");
        TelaInicial menuRemapeado = new TelaInicial(remapeados);
        GreenfootImage capaRemapeada = ArteTelas.criarInicio();
        GreenfootImage controleRemapeado = new GreenfootImage(
            "W/A/S/D / SETAS: MOVER  |  F: COLETAR", 17, Color.WHITE, null);
        capaRemapeada.drawImage(controleRemapeado, 390, 480);
        conferir(regioesIguais(menuRemapeado.getBackground(), capaRemapeada, 390, 480,
                              controleRemapeado.getWidth(), controleRemapeado.getHeight()),
                 "Capa acompanha a tecla de coleta remapeada");
        salvarMundo(menuRemapeado, new File(saida, "menu-remapeado.png"));
        EstadoPartida estado = new EstadoPartida();
        Fase01 primeira = new Fase01(comandos, estado);
        Greenfoot.setWorld(primeira);
        salvarMundo(menu, new File(saida, "menu-completo.png"));
        salvarMundo(primeira, new File(saida, "hud-fase1.png"));
        conferir(primeira.getObjects(Inimigo.class).size() == 4, "Fase 1 tem tres capangas e minichefe");
        Jogador protagonista = primeira.getJogador();
        conferir(protagonista.getImage().getWidth() == 108 && protagonista.getImage().getHeight() == 108,
                 "Protagonista aparece 35% maior");
        for (Inimigo inimigo : primeira.getObjects(Inimigo.class))
            conferir(inimigo.getImage().getWidth() == 108, "Inimigo humano aparece maior");
        LuzesCidade luzes = primeira.getObjects(LuzesCidade.class).get(0);
        GreenfootImage luzAnterior = luzes.getImage();
        for (int i = 0; i < 32; i++) luzes.act();
        conferir(luzes.getImage() != luzAnterior, "Luzes da cidade alternam os quadros");
        Cenario saidaRua = null;
        for (Cenario cenario : primeira.getObjects(Cenario.class))
            if (cenario.acessar()) saidaRua = cenario;
        conferir(saidaRua != null && "Saida da rua".equals(saidaRua.getNome())
                 && saidaRua.getImage().getWidth() == 1,
                 "Saida nao tem porta nem marcador visivel antes dos abates");
        protagonista.setLocation(615, 425);
        protagonista.moverComColisao(20, 0);
        conferir(protagonista.getX() == 619, "Colisao de movimento continua usando a base dos pes");
        protagonista.setLocation(958, 465);
        primeira.tentarSair(protagonista);
        conferir(WorldHandler.getInstance().getWorld() == primeira,
                 "Saida continua bloqueada enquanto ha inimigos");
        protagonista.setLocation(100, 465);
        Set<GreenfootImage> poses = new HashSet<GreenfootImage>();
        pressionar(KeyCode.D, "d");
        for (int i = 0; i < 28; i++) {
            protagonista.act();
            poses.add(protagonista.getImage());
        }
        soltar(KeyCode.D, "d");
        protagonista.act();
        conferir(poses.size() >= 4, "Corrida do protagonista alterna quatro quadros");
        protagonista.setLocation(185, 472);
        pressionar(KeyCode.E, "e");
        protagonista.act();
        soltar(KeyCode.E, "e");
        protagonista.act();
        conferir(protagonista.getEquipamentoSelecionado() instanceof ArmaBranca,
                 "Protagonista coleta a faca da primeira fase");
        pressionar(KeyCode.SPACE, " ");
        protagonista.act();
        soltar(KeyCode.SPACE, " ");
        conferir(imagensIguais(protagonista.getImage(), poseAmpliada("novo_jogador_faca.png")),
                 "Ataque com faca exibe a pose correspondente");
        for (Inimigo inimigo : primeira.getObjects(Inimigo.class)) inimigo.receberDano(9999);
        conferir(saidaRua.getImage().getWidth() == 86
                 && ((saidaRua.getImage().getAwtImage().getRGB(43, 15) >>> 24) == 0),
                 "Seta discreta aparece no chao depois dos abates");
        primeira.act();
        salvarMundo(primeira, new File(saida, "saida-fase1-liberada.png"));
        primeira.getJogador().setLocation(958, 465);
        pressionar(KeyCode.E, "e");
        protagonista.act();
        soltar(KeyCode.E, "e");
        conferir(WorldHandler.getInstance().getWorld() instanceof TelaVitoriaFase,
                 "Vitoria da fase 1 abre a tela correspondente");
        salvarMundo(WorldHandler.getInstance().getWorld(), new File(saida, "vitoria-fase1-completa.png"));
        apertarEnter();
        WorldHandler.getInstance().getWorld().act();
        soltarEnter();
        conferir(WorldHandler.getInstance().getWorld() instanceof Fase02, "Tela de vitoria avanca para a fase 2");
        Fase02 segunda = (Fase02) WorldHandler.getInstance().getWorld();
        segunda.act();
        conferir(!segunda.getObjects(LuzesCidade.class).isEmpty(), "Rodovia possui detalhes luminosos");
        int distancia = segunda.getDistanciaPolicia();
        CarroJogador carro = segunda.getObjects(CarroJogador.class).get(0);
        segunda.addObject(new ObstaculoRodovia(true), carro.getX(), carro.getY());
        carro.act();
        conferir(segunda.getDistanciaPolicia() == distancia - 110, "Colisao aproxima a policia");
        Set<Integer> coresTrafego = new HashSet<Integer>();
        for (int i = 0; i < 40; i++) coresTrafego.add(assinatura(new ObstaculoRodovia(true).getImage()));
        conferir(coresTrafego.size() > 1, "Carros de trafego aparecem em cores variadas");
        for (int i = 0; i < 190; i++) {
            segunda.act();
            for (ObstaculoRodovia obstaculo : segunda.getObjects(ObstaculoRodovia.class)) obstaculo.act();
            for (FaixaRodovia faixa : segunda.getObjects(FaixaRodovia.class)) faixa.act();
            for (LuzesCidade luz : segunda.getObjects(LuzesCidade.class)) luz.act();
        }
        conferir(segunda.getObjects(ObstaculoRodovia.class).size() >= 2,
                 "Rodovia gera trafego durante a fuga");
        salvarMundo(segunda, new File(saida, "hud-fase2.png"));
        for (int i = 0; i < Fase02.DURACAO; i++) segunda.act();
        conferir(WorldHandler.getInstance().getWorld() instanceof TelaVitoriaFase,
                 "Fuga concluida abre a tela de vitoria da fase 2");
        salvarMundo(WorldHandler.getInstance().getWorld(), new File(saida, "vitoria-fase2-completa.png"));
        apertarEnter();
        WorldHandler.getInstance().getWorld().act();
        soltarEnter();
        conferir(WorldHandler.getInstance().getWorld() instanceof Fase03, "Tela de vitoria avanca para a fase 3");

        Fase03 terceira = (Fase03) WorldHandler.getInstance().getWorld();
        terceira.act();
        conferir(terceira.getJogador() == protagonista
                 && terceira.getJogador().getEquipamentoSelecionado() instanceof ArmaBranca,
                 "Jogador e equipamento continuam os mesmos ao atravessar as fases");
        conferir(!terceira.getObjects(LuzesCidade.class).isEmpty(), "Deposito possui luzes de alerta");
        salvarMundo(terceira, new File(saida, "hud-fase3.png"));
        ChefeFinal chefe = terceira.getObjects(ChefeFinal.class).get(0);
        chefe.usarArma(shotgun);
        conferir(terceira.getObjects(Projetil.class).size() == 3, "Shotgun cria tres projeteis no mundo");
        terceira.removeObjects(terceira.getObjects(Projetil.class));
        chefe.receberDano(117);
        conferir(!terceira.estaNoTanque(), "Chefe continua humano acima da metade da vida");
        chefe.receberDano(1);
        conferir(terceira.estaNoTanque() && terceira.getObjects(TanqueChefe.class).size() == 1,
                 "Chefe entra no tanque ao chegar a metade da vida");
        conferir(terceira.getObjects(GranadaColetavel.class).size() == 4,
                 "Granadas coletaveis surgem no chao");
        TanqueChefe tanque = terceira.getObjects(TanqueChefe.class).get(0);
        conferir(tanque.getImage().getWidth() == 170, "Tanque preserva seu tamanho original");
        int vida = tanque.getVida();
        tanque.receberDano(999);
        conferir(tanque.getVida() == vida, "Tanque ignora dano de armas comuns");
        Jogador jogador = terceira.getJogador();
        jogador.act();
        GranadaColetavel coleta = terceira.getObjects(GranadaColetavel.class).get(0);
        jogador.setLocation(coleta.getX(), coleta.getY());
        pressionar(KeyCode.E, "e");
        jogador.act();
        soltar(KeyCode.E, "e");
        conferir(jogador.getGranadas() >= 2, "Tecla E coleta granadas do chao");
        jogador.setLocation(tanque.getX() - 170, tanque.getY());
        pressionar(KeyCode.D, "d");
        jogador.act();
        soltar(KeyCode.D, "d");
        int granadasAntes = jogador.getGranadas();
        pressionar(KeyCode.G, "g");
        jogador.act();
        soltar(KeyCode.G, "g");
        conferir(jogador.getGranadas() == granadasAntes - 1 && terceira.getObjects(Granada.class).size() == 1,
                 "Tecla G lanca e consome uma granada coletada");
        for (int i = 0; i < 80 && !terceira.getObjects(Granada.class).isEmpty(); i++)
            terceira.getObjects(Granada.class).get(0).act();
        conferir(tanque.getVida() == vida - 43, "Granada arremessada atinge e danifica o tanque");
        for (int i = 0; i < 3; i++) tanque.receberGranada();
        conferir(WorldHandler.getInstance().getWorld() instanceof TelaVitoria,
                 "Quatro granadas derrotam o tanque e abrem a vitoria final");
        salvarMundo(WorldHandler.getInstance().getWorld(), new File(saida, "vitoria-final-completa.png"));
        conferir(estado.getPontosFase(1) > 0 && estado.getPontosFase(2) > 0 && estado.getPontosFase(3) > 0,
                 "Pontos sao guardados separadamente nas tres fases");
        GreenfootImage placarEsperado = ArteTelas.criarResultado(3, true);
        GreenfootImage textoPlacar = new GreenfootImage(
            "FASE 1: " + estado.getPontosFase(1) + "     FASE 2: " + estado.getPontosFase(2)
            + "     FASE 3: " + estado.getPontosFase(3), 18, Color.WHITE, null);
        placarEsperado.drawImage(textoPlacar, 60, 465);
        conferir(regioesIguais(WorldHandler.getInstance().getWorld().getBackground(), placarEsperado,
                              60, 465, textoPlacar.getWidth(), textoPlacar.getHeight()),
                 "Tela final desenha os pontos reais de cada fase");

        pressionar(KeyCode.R, "r");
        soltar(KeyCode.R, "r");
        WorldHandler.getInstance().getKeyboardManager().keyTyped(KeyCode.R, "r");
        WorldHandler.getInstance().getWorld().act();
        conferir(WorldHandler.getInstance().getWorld() instanceof Fase01,
                 "R reinicia a partida depois da vitoria sem usar Run");

        EstadoPartida checkpoint = new EstadoPartida();
        checkpoint.adicionarPontos(131);
        checkpoint.avancarFase();
        checkpoint.adicionarPontos(79);
        Fase02 fuga = new Fase02(comandos, checkpoint, new Jogador("Motorist", 110, 4, comandos));
        Greenfoot.setWorld(fuga);
        for (int i = 0; i < 7; i++) fuga.colisao();
        fuga.act();
        conferir(WorldHandler.getInstance().getWorld() instanceof TelaPerseguicaoFim,
                 "Policia alcanca o jogador apos colisoes suficientes");
        salvarMundo(WorldHandler.getInstance().getWorld(), new File(saida, "derrota-fase2-completa.png"));
        WorldHandler.getInstance().getKeyboardManager().keyTyped(KeyCode.R, "r");
        WorldHandler.getInstance().getWorld().act();
        conferir(WorldHandler.getInstance().getWorld() instanceof Fase02
                 && checkpoint.getPontosFase(1) == 131 && checkpoint.getPontosFase(2) == 0,
                 "R repete a perseguicao e preserva os pontos anteriores");

        Fase01 derrota = new Fase01(comandos, new EstadoPartida());
        Greenfoot.setWorld(derrota);
        derrota.getJogador().receberDano(999);
        conferir(WorldHandler.getInstance().getWorld() instanceof TelaGameOver,
                 "Morte na fase 1 abre tela de derrota");
        salvarMundo(WorldHandler.getInstance().getWorld(), new File(saida, "derrota-fase1-completa.png"));
        EstadoPartida estadoFinal = new EstadoPartida();
        estadoFinal.adicionarPontos(155);
        estadoFinal.avancarFase();
        estadoFinal.adicionarPontos(276);
        estadoFinal.avancarFase();
        estadoFinal.adicionarPontos(385);
        Fase03 derrotaFinal = new Fase03(comandos, estadoFinal, null);
        Greenfoot.setWorld(derrotaFinal);
        derrotaFinal.getJogador().receberDano(999);
        conferir(WorldHandler.getInstance().getWorld() instanceof TelaGameOver,
                 "Morte na fase 3 abre tela de derrota correspondente");
        salvarMundo(WorldHandler.getInstance().getWorld(), new File(saida, "derrota-fase3-completa.png"));
        verificarAuditoria(saida);
        System.out.println("Telas geradas em: " + saida.getAbsolutePath());
        System.exit(0);
    }
}
