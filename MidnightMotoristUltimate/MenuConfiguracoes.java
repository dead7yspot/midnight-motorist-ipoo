import greenfoot.*;
import java.util.ArrayList;
import java.util.Collections;

/** Menu de remapeamento original do alvo_meu, adaptado ao tamanho do jogo. */
public class MenuConfiguracoes extends World {
    private final GerenciadorComandos comandos;
    private final ArrayList<String> identificadores;
    private int indiceComando;
    private int indiceTecla;
    private boolean aguardandoTecla;
    private String mensagem = "";

    /** Abre o menu com a lista ordenada dos comandos configuraveis. */
    public MenuConfiguracoes(GerenciadorComandos comandos) {
        super(1024, 572, 1);
        this.comandos = comandos;
        identificadores = comandos.obterIdentificadores();
        Collections.sort(identificadores); // ordem estavel, sem mudar o gerenciador
        desenhar();
    }

    /** Navega pela lista e valida a nova tecla escolhida pelo jogador. */
    public void act() {
        String tecla = Greenfoot.getKey();
        if (tecla == null) return;
        if (aguardandoTecla) {
            aguardandoTecla = false;
            if ("escape".equals(tecla)) {
                mensagem = "Alteracao cancelada.";
            } else {
                String comando = identificadores.get(indiceComando);
                ArrayList<String> teclas = comandos.obterTeclas(comando);
                if (comandos.reBind(comando, teclas.get(indiceTecla), tecla))
                    mensagem = "Tecla atualizada.";
                else mensagem = "Tecla indisponivel (ja usada).";
            }
            desenhar();
            return;
        }
        if ("escape".equals(tecla)) {
            Greenfoot.setWorld(new TelaInicial(comandos));
            return;
        }
        if ("up".equals(tecla)) {
            indiceComando = (indiceComando - 1 + identificadores.size()) % identificadores.size();
            indiceTecla = 0;
        } else if ("down".equals(tecla)) {
            indiceComando = (indiceComando + 1) % identificadores.size();
            indiceTecla = 0;
        } else if ("left".equals(tecla) || "right".equals(tecla)) {
            ArrayList<String> teclas = comandos.obterTeclas(identificadores.get(indiceComando));
            if (!teclas.isEmpty()) {
                int passo = "right".equals(tecla) ? 1 : -1;
                indiceTecla = (indiceTecla + passo + teclas.size()) % teclas.size();
            }
        } else if ("enter".equals(tecla)) {
            aguardandoTecla = true;
            mensagem = "Pressione a nova tecla (ESC cancela).";
        } else if ("ALTERNAR_MUSICA".equals(comandos.obterComando(tecla))) {
            GerenciadorAudio.alternarMusica();
            mensagem = GerenciadorAudio.estaPausada() ? "Musica pausada." : "Musica retomada.";
        }
        desenhar();
    }

    /** Redesenha as opcoes, a selecao e a mensagem de retorno. */
    private void desenhar() {
        GreenfootImage fundo = new GreenfootImage(1024, 572);
        fundo.setColor(new Color(12, 18, 34));
        fundo.fill();
        fundo.setColor(new Color(66, 49, 113));
        fundo.fillRect(40, 90, 940, 392);
        fundo.setColor(new Color(14, 25, 46));
        fundo.fillRect(44, 94, 932, 385);
        setBackground(fundo);
        texto("CONFIGURACOES DE COMANDOS", 37, Color.WHITE, 200, 35);
        String teclaMusica = comandos.obterTeclas("ALTERNAR_MUSICA").get(0);
        String ajuda = "SETAS: navegar  |  ENTER: trocar  |  ESC: menu";
        if (!"up".equals(teclaMusica) && !"down".equals(teclaMusica)
            && !"left".equals(teclaMusica) && !"right".equals(teclaMusica)
            && !"enter".equals(teclaMusica) && !"escape".equals(teclaMusica))
            ajuda += "  |  " + comandos.obterTeclaExibicao("ALTERNAR_MUSICA") + ": musica";
        texto(ajuda, 19,
              new Color(183, 205, 236), 110, 525);
        for (int i = 0; i < identificadores.size(); i++) {
            String nome = identificadores.get(i);
            String linha = comandos.obterAlias(nome) + "  :  " + formatarTeclas(nome, i == indiceComando);
            int coluna = i / 7;
            int linhaIndice = i % 7;
            int x = 73 + coluna * 473;
            int y = 108 + linhaIndice * 48;
            if (i == indiceComando) {
                getBackground().setColor(new Color(75, 84, 125));
                getBackground().fillRect(x-10, y-4, 439, 42);
            }
            texto(linha, 19, i == indiceComando ? Color.YELLOW : Color.WHITE, x, y);
        }
        texto(mensagem.length() > 0 ? mensagem : "SETAS esquerda/direita escolhem a tecla entre [colchetes].",
              20, aguardandoTecla ? Color.YELLOW : new Color(100, 231, 174), 88, 475);
    }

    /** Destaca a tecla que sera alterada ao pressionar Enter. */
    private String formatarTeclas(String nome, boolean selecionado) {
        ArrayList<String> teclas = comandos.obterTeclas(nome);
        String resultado = "";
        for (int i = 0; i < teclas.size(); i++) {
            if (i > 0) resultado += " / ";
            String tecla = teclas.get(i).toUpperCase();
            if (selecionado && i == indiceTecla) tecla = "[" + tecla + "]";
            resultado += tecla;
        }
        return resultado;
    }

    /** Escreve uma linha na imagem do menu. */
    private void texto(String texto, int tamanho, Color cor, int x, int y) {
        getBackground().drawImage(new GreenfootImage(texto, tamanho, cor, null), x, y);
    }
}
