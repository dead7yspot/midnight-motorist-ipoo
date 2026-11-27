import java.util.ArrayList;

/** Representa um comando e guarda suas teclas, apelido e categoria. */

public class Comando {
    private String alias;
    private String categoria;
    private ArrayList<String> teclas;

    /** Cria o comando com seu nome de exibicao, categoria e primeira tecla. */
    public Comando(String alias, String categoria, String teclaInicial) {
        this.alias = alias;
        this.categoria = categoria;

        teclas = new ArrayList<String>();
        teclas.add(teclaInicial.toLowerCase());
    }

    /** Consulta o nome apresentado ao jogador. */
    public String getAlias() {
        return alias;
    }

    /** Consulta o grupo ao qual o comando pertence. */
    public String getCategoria() {
        return categoria;
    }

    /** Devolve uma copia das teclas para proteger a lista original. */
    public ArrayList<String> getTeclas() {
        // retorna uma copia do conjunto de teclas ao inves do conjunto original
        return new ArrayList<String>(teclas);
    }
    
    /** Acrescenta uma tecla valida que ainda nao esta neste comando. */
    public boolean adicionarTecla(String tecla) {
        if (tecla == null || tecla.trim().isEmpty()) {
            return false;
        }

        tecla = tecla.toLowerCase();

        if (teclas.contains(tecla)) {
            return false;
        }

        teclas.add(tecla);
        return true;
    }

    /** Remove do comando a tecla informada, se ela existir. */
    public boolean removerTecla(String tecla) {
        
        if (tecla == null) {
            return false;
        }
        
        return teclas.remove(tecla.toLowerCase());
    }
    
    // -- altera a tecla de um comando existente
    public boolean reBind(String teclaAntiga, String teclaNova) {
    
        if (teclaAntiga == null || teclaNova == null) {
            return false;
        }
    
        teclaAntiga = teclaAntiga.toLowerCase();
        teclaNova = teclaNova.toLowerCase();
    
        if (teclaNova.trim().isEmpty()) {
            return false;
        }
    
        if (!teclas.contains(teclaAntiga)) {
            return false;
        }
    
        if (teclaAntiga.equals(teclaNova)) {
            return true;
        }
    
        if (teclas.contains(teclaNova)) {
            return false;
        }
    
        // Mantem a posicao escolhida, inclusive a tecla principal do comando.
        int indice = teclas.indexOf(teclaAntiga);
        teclas.set(indice, teclaNova);
    
        return true;
    }
    
}
