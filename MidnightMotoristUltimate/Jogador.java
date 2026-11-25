import greenfoot.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Jogador original do alvo_meu: inventario, comandos remapeaveis e equipamento.
 * Movimentacao e colisao foram reconstruidas a partir da ideia do V1.
 */
public class Jogador extends Personagem {
    private final HashMap<Integer, Equipamento> inventario;
    private final GerenciadorComandos cmds;
    private final Map<String, Boolean> estadoAnterior;
    private int equipamentoSelecionado;
    private int recargaAtaque;
    private int animacaoGolpe;
    private int invulneravel;
    private int passos;
    private int recargaGranada;
    private int granadas;
    private final GreenfootImage spriteTiro;
    private final GreenfootImage spriteGolpe;
    private final GreenfootImage spriteGranada;
    private GreenfootImage spriteAtaqueAtual;

    /** Cria o inventario e carrega as poses de combate do protagonista. */
    public Jogador(String nome, int vidaInicial, int velocidade, GerenciadorComandos cmds) {
        super(nome, vidaInicial, velocidade, "novo_jogador_parado.png",
              "novo_jogador_andar1.png", "novo_jogador_andar2.png");
        configurarEscalaVisual(135);
        configurarQuadrosCorrida(new String[] {"novo_jogador_andar3.png", "novo_jogador_andar1.png",
                                               "novo_jogador_andar4.png", "novo_jogador_andar2.png"});
        inventario = new HashMap<Integer, Equipamento>();
        estadoAnterior = new HashMap<String, Boolean>();
        this.cmds = cmds;
        equipamentoSelecionado = 0;
        spriteTiro = new GreenfootImage("novo_jogador_tiro.png");
        spriteGolpe = new GreenfootImage("novo_jogador_faca.png");
        spriteGranada = new GreenfootImage("novo_jogador_granada.png");
        spriteAtaqueAtual = spriteGolpe;
    }

    /** A cada ciclo, trata controles, animacao e tempos de recarga. */
    public void act() {
        if (getWorld() == null || estaMorto()) return;
        if (recargaAtaque > 0) recargaAtaque--;
        if (animacaoGolpe > 0) animacaoGolpe--;
        if (invulneravel > 0) invulneravel--;
        if (recargaGranada > 0) recargaGranada--;
        if (getWorld() instanceof Fase03 && estaPressionado("LANCAR_GRANADA") && recargaGranada == 0) {
            Fase03 fase = (Fase03) getWorld();
            recargaGranada = 35;
            if (!fase.estaNoTanque()) fase.avisar("Guarde as granadas para a blindagem!");
            else if (granadas <= 0) fase.avisar("Sem granadas! Procure explosivos no chao e aperte "
                                                + cmds.obterTeclaExibicao("INTERAGIR") + ".");
            else {
                granadas--;
                int direcao = isOlhandoDireita() ? 1 : -1;
                getWorld().addObject(new Granada(direcao, getY()), getX() + direcao * 28, getY());
                spriteAtaqueAtual = spriteGranada;
                animacaoGolpe = 15;
                Greenfoot.playSound("faca_ataque.wav");
            }
        }

        boolean andando = movimentar();
        processarComandos();
        if (getWorld() == null) return;
        if (animacaoGolpe > 0) {
            String pose = spriteAtaqueAtual == spriteTiro ? "tiro"
                          : spriteAtaqueAtual == spriteGranada ? "granada" : "golpe";
            aplicarSprite(spriteAtaqueAtual, pose);
        }
        else atualizarAnimacao(andando);
        // A arma faz parte do sprite de ataque, sem ator solto na mao.
        removerItemMao();
        passos++;
        if (andando && passos >= 24) {
            Greenfoot.playSound("pers_passos.wav");
            passos = 0;
        }
        if (!andando) passos = 0;
    }

    /** Verifica todas as teclas associadas a um comando configuravel. */
    private boolean estaPressionado(String comando) {
        for (String tecla : cmds.obterTeclas(comando)) {
            if (Greenfoot.isKeyDown(tecla)) return true;
        }
        return false;
    }

    /** Identifica o momento em que uma tecla passou de solta para pressionada. */
    private boolean comandoAcionado(String comando) {
        boolean agora = estaPressionado(comando);
        Boolean antes = estadoAnterior.get(comando);
        estadoAnterior.put(comando, agora);
        return agora && (antes == null || !antes.booleanValue());
    }

    /** Le as direcoes e tenta andar sem atravessar os obstaculos. */
    private boolean movimentar() {
        int dx = 0, dy = 0;
        if (estaPressionado("MOVER_CIMA")) dy -= getVelocidade();
        if (estaPressionado("MOVER_BAIXO")) dy += getVelocidade();
        if (estaPressionado("MOVER_ESQUERDA")) dx -= getVelocidade();
        if (estaPressionado("MOVER_DIREITA")) dx += getVelocidade();
        atualizarDirecao(dx);
        return moverComColisao(dx, dy);
    }

    /** Encaminha as acoes de ataque, interacao, troca de item e musica. */
    private void processarComandos() {
        boolean interagir = comandoAcionado("INTERAGIR");
        boolean anterior = comandoAcionado("EQP_ANTERIOR");
        boolean proximo = comandoAcionado("EQP_PROX");
        boolean atacar = comandoAcionado("ATACAR");
        boolean musica = comandoAcionado("ALTERNAR_MUSICA");
        boolean mouse = Greenfoot.mouseClicked(null);
        if (musica) GerenciadorAudio.alternarMusica();
        if (anterior) trocarSelecao(-1);
        if (proximo) trocarSelecao(1);
        if (atacar || mouse) atacar();
        if (interagir) interagir();
    }

    // Usa o armamento selecionado e inicia a pose e a recarga adequadas
    private void atacar() {
        if (recargaAtaque > 0) return;
        Equipamento selecionado = getEquipamentoSelecionado();
        if (selecionado instanceof Armamento) {
            if (usarArma((Armamento) selecionado)) {
                recargaAtaque = selecionado instanceof ArmaFogo ? 15 : 24;
                spriteAtaqueAtual = selecionado instanceof ArmaFogo ? spriteTiro : spriteGolpe;
                animacaoGolpe = 12;
            }
        }
    }

    /** Percorre os espacos do inventario, incluindo o estado desarmado. */
    private void trocarSelecao(int passo) {
        int total = inventario.size() + 1;
        equipamentoSelecionado = (equipamentoSelecionado + passo + total) % total;
    }

    /** Devolve o item do espaco selecionado ou null quando estiver desarmado. */
    public Equipamento getEquipamentoSelecionado() {
        return inventario.get(equipamentoSelecionado);
    }

    // resume equipamento, municao, granadas e colete como string
    public String obterEquipamentoTexto() {
        Equipamento item = getEquipamentoSelecionado();
        if (item == null) return "Desarmado  |  Granadas: " + granadas;
        String texto = item.getNome();
        if (item instanceof ArmaFogo) texto += " / municao: " + ((ArmaFogo) item).getMunicao();
        if (item instanceof Armamento) texto += " / usos: " + ((Armamento) item).getResistencia();
        return texto + "  |  Granadas: " + granadas;
    }

    /** O jogador (nao o mundo) possui e administra o inventario. */
    private void interagir() {
        List<Equipamento> itens = getObjectsInRange(55, Equipamento.class);
        for (Equipamento item : itens) {
            if (item instanceof GranadaColetavel) {
                adicionarGranadas(((GranadaColetavel) item).getQuantidade());
                getWorld().removeObject(item);
                Greenfoot.playSound("som_equip.wav");
                ((FaseBase) getWorld()).coletouItem(30);
                ((FaseBase) getWorld()).avisar("Granadas coletadas: " + granadas + ". Lance com "
                                              + cmds.obterTeclaExibicao("LANCAR_GRANADA") + "!");
                return;
            }
            
            if (item instanceof Dinheiro) {
                int valor = ((Dinheiro) item).getValor();
                getWorld().removeObject(item);
                Greenfoot.playSound("hit_som.wav");
                ((FaseBase) getWorld()).coletouItem(valor);
                ((FaseBase) getWorld()).avisar("Dinheiro coletado: +" + valor + " pontos!");
                return;
            }
            if (item instanceof Suprimento) {
                Suprimento kit = (Suprimento) item;
                if (getVida() == getVidaMaxima()) {
                    if (getWorld() instanceof FaseBase) ((FaseBase) getWorld()).avisar("Vida ja esta cheia.");
                    return;
                }
                recuperarVida(kit.getCura());
                getWorld().removeObject(item);
                Greenfoot.playSound("hit_som.wav");
                if (getWorld() instanceof FaseBase) ((FaseBase) getWorld()).coletouItem(40);
                return;
            }
            int slot = 1;
            
            while (inventario.containsKey(slot))
                slot++;
            
            inventario.put(slot, item);
            equipamentoSelecionado = slot;
            
            getWorld().removeObject(item);
            Greenfoot.playSound("som_equip.wav");
            
            if (getWorld() instanceof FaseBase) {
                ((FaseBase) getWorld()).coletouItem(60);
                ((FaseBase) getWorld()).avisar("Coletado: " + item.getNome());
            }
            return;
        }
        if (getWorld() instanceof FaseBase) ((FaseBase) getWorld()).tentarSair(this);
    }

    // Desconta primeiro o colete e aplica um curto periodo de protecao
    @Override
    public void receberDano(int quantidade) {
        if (invulneravel > 0 || estaMorto()) return;
        int bloqueado = Math.min(0, quantidade);
        super.receberDano(quantidade - bloqueado);
        invulneravel = 36;
        Greenfoot.playSound("som_dano.wav");
        if (getWorld() instanceof FaseBase) ((FaseBase) getWorld()).avisar("CUIDADO! Voce sofreu dano.");
    }

    // acrescenta as granadas obtidas numa coleta
    public void adicionarGranadas(int quantidade) {
        if (quantidade > 0) 
            granadas += quantidade;
    }

    /** Consulta a quantidade de granadas disponivel. */
    public int getGranadas() { 
        return granadas; 
    }

    // Avisa a fase para abrir a tela de derrota
    @Override
    protected void aoMorrer() {
        super.aoMorrer();
        if (getWorld() instanceof FaseBase) 
            ((FaseBase) getWorld()).jogadorMorreu();
    }
}
