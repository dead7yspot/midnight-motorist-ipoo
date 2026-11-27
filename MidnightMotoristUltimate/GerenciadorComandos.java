import java.util.HashMap;
import java.util.ArrayList;

/** Centraliza cadastro, consulta e remapeamento dos comandos do jogo. */
public class GerenciadorComandos {
    private final HashMap<String, Comando> comandos;

    /** Inicia o cadastro que relaciona identificadores aos comandos. */
    public GerenciadorComandos() {
        comandos = new HashMap<>();
    }

    // -- Adiciona um novo comando
     public boolean adicionarComando(String nome, String tecla, String alias, String categoria) {
        if (nome == null || tecla == null ||
            alias == null || categoria == null) {
            return false;
        }

        if (nome.trim().isEmpty() || tecla.trim().isEmpty() ||
            alias.trim().isEmpty() || categoria.trim().isEmpty()) {
            return false;
        }

        nome = nome.toUpperCase();
        tecla = tecla.toLowerCase();

        if (comandos.containsKey(nome) || teclaEmUso(tecla)) {
            return false;
        }

        Comando novo = new Comando(alias, categoria, tecla);

        comandos.put(nome, novo);

        return true;
    }

    // verificar se tecla esta associada a um determinado comando
    public boolean verificarTecla(String nome, String tecla) {

        if (!comandos.containsKey(nome) || tecla == null) {
            return false;
        }
        
        return comandos.get(nome).getTeclas().contains(tecla.toLowerCase());
    }

    // Associa uma tecla livre a um comando existente
    public boolean adicionarTecla(String nome, String tecla) {
        if (nome == null || tecla == null) {
            return false;
        }
    
        Comando comando = comandos.get(nome);
    
        if (comando == null || teclaEmUso(tecla)) {
            return false;
        }
    
        return comando.adicionarTecla(tecla);
    }
    
    // para checar se alguma tecla ja foi pegada pra alguma outra coisa
    private boolean teclaEmUso(String tecla) {
        
        if (tecla == null) 
            return false;

        for (Comando cmd : comandos.values()) {
            if (cmd.getTeclas().contains(tecla.toLowerCase())) {
                return true;
            }
        }

        return false;
    }
    
    // consulta as teclas do comando sem expor a lista interna
    public ArrayList<String> obterTeclas(String nome) {

        if (!comandos.containsKey(nome)) {
            return new ArrayList<String>();
        }
        
        // retorna uma copia para não ser alteravel por fora
        return comandos.get(nome).getTeclas();
    }

    // formata a primeira tecla para as instrucoes e os avisos do jogo
    public String obterTeclaExibicao(String nome) {
        ArrayList<String> teclas = obterTeclas(nome);
        if (teclas.isEmpty()) 
            return "?";
        
        String tecla = teclas.get(0);
        
        if ("space".equals(tecla)) 
            return "ESPACO";
        if ("up".equals(tecla)) 
            return "CIMA";
        if ("down".equals(tecla)) 
            return "BAIXO";
        if ("left".equals(tecla)) 
            return "ESQ";
        if ("right".equals(tecla)) 
            return "DIR";
            
        return tecla.toUpperCase();
    }

    // Resume as teclas principais de movimento e indica as setas quando disponiveis
    public String obterMovimentoExibicao() {
        String movimento = obterTeclaExibicao("MOVER_CIMA") + "/" + obterTeclaExibicao("MOVER_ESQUERDA")
                         + "/" + obterTeclaExibicao("MOVER_BAIXO") + "/" + obterTeclaExibicao("MOVER_DIREITA");
        if (obterTeclas("MOVER_CIMA").contains("up")
            && obterTeclas("MOVER_BAIXO").contains("down")
            && obterTeclas("MOVER_ESQUERDA").contains("left")
            && obterTeclas("MOVER_DIREITA").contains("right")) movimento += " / SETAS";
        return movimento;
    }

    // -- valida o remapeio da tecla
    public boolean reBind(String nome, String teclaAntiga, String teclaNova) {

        if (nome == null || teclaAntiga == null || teclaNova == null) {
            return false;
        }
    
        nome = nome.toUpperCase();
        teclaAntiga = teclaAntiga.toLowerCase();
        teclaNova = teclaNova.toLowerCase();
    
        if (teclaNova.trim().isEmpty()) {
            return false;
        }
    
        Comando comando = comandos.get(nome);
    
        if (comando == null) {
            return false;
        }
    
        if (!comando.getTeclas().contains(teclaAntiga)) {
            return false;
        }
    
        if (teclaAntiga.equals(teclaNova)) {
            return true;
        }
    
        if (teclaEmUso(teclaNova)) {
            return false;
        }
    
        return comando.reBind(teclaAntiga, teclaNova);
    }
    
    // retorna TIPO_IDENTIFICADOR por padrão (DIRECIONAIS, ACOES)
    public String obterComando(String tecla) {
        return obterComando(tecla, FormatoComando.TIPO_IDENTIFICADOR    );
    }
    
    /** Metodo sobrecarregado que permite
     *  exibir comandos de outras formas
     * 
     * @param tipo Alterna o formato valor retornado de 4 formas, conforme definido no enum FormatoComando.
     */
    public String obterComando(String tecla, FormatoComando tipo) {
        
        if (tecla == null || tipo == null) {
            return null;
        }
    
        tecla = tecla.toLowerCase();
        
        for (String nome : comandos.keySet()) {
    
            Comando comando = comandos.get(nome);
    
            if (comando.getTeclas().contains(tecla)) {
    
                switch(tipo) {
    
                    case TIPO_IDENTIFICADOR:
                        return nome;
    
                    case TIPO_TECLA:
                        return tecla;
    
                    case TIPO_ALIAS:
                        return comando.getAlias();
    
                    case TIPO_GRUPO:
                        return comando.getCategoria();
                }
            }
        }
        return null;
    }
    
    /** Retorna os identificadores cadastrados para montar o menu. */
    public ArrayList<String> obterIdentificadores() {
        return new ArrayList<String>(comandos.keySet());
    }
    
    /** Consulta o texto de exibicao de um comando. */
    public String obterAlias(String nome) {
        if (!comandos.containsKey(nome)) {
            return null;
        }

        return comandos.get(nome).getAlias();
    }

    
    /** Consulta a categoria de um comando cadastrado. */
    public String obterCategoria(String nome) {
        if (!comandos.containsKey(nome)) {
            return null;
        }

        return comandos.get(nome).getCategoria();
    }
    
    
    /** Monta uma listagem textual dos comandos e suas teclas. */
    public String exibirComandos() {
        String str = "";
    
        for (String tecla : comandos.keySet()) {
            str += comandos.get(tecla).getAlias() + " -> " + comandos.get(tecla).getTeclas() + "\n";
        }
    
        return str;
    }
}
