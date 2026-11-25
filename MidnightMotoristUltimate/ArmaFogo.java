import greenfoot.*;

/** Armamento do alvo_meu: usa municao alem da resistencia. */
public class ArmaFogo extends Armamento {
    private int municao;
    private final int danoMinimo;
    private final int quantidadeProjetis;

    // Cria uma arma comum com um projetil e dano a partir de um.
    public ArmaFogo(String nome, String descricao, String imagem,
                    int resistencia, int alcance, int capacidadeMunicao, int danoMaximo) {
        this(nome, descricao, imagem, resistencia, alcance, capacidadeMunicao, 1, danoMaximo, 1);
    }

    // Permite configurar armas pesadas sem alterar as armas comuns
    public ArmaFogo(String nome, String descricao, String imagem,
                    int resistencia, int alcance, int capacidadeMunicao,
                    int danoMinimo, int danoMaximo, int quantidadeProjetis) {
        super(nome, descricao, imagem, resistencia, alcance, danoMaximo - danoMinimo + 1);
        municao = capacidadeMunicao;
        this.danoMinimo = danoMinimo;
        this.quantidadeProjetis = quantidadeProjetis;
    }

    // Sorteia o dano e desconta a resistencia como nas outras armas
    @Override
    public int usar() {
        return super.usar() + danoMinimo - 1; 
    }

    /** Informa se ainda ha tiros disponiveis. */
    public boolean temMunicao() { 
        return municao > 0; 
    }
    
    /** Exige tanto resistencia quanto municao para disparar. */
    @Override
    public boolean podeUsar() { 
        return super.podeUsar() && temMunicao(); 
    }
    
    /** Consome um tiro e reproduz o efeito sonoro. */
    public void gastarMunicao() {
        if (municao > 0) {
            municao--;
            Greenfoot.playSound("gun_tiro.wav");
        }
    }
    /** Consulta a municao restante. */
    public int getMunicao() { 
        return municao; 
    }
    /** Consulta quantos projeteis saem em cada disparo. */
    public int getQuantidadeProjetis() { 
        return quantidadeProjetis; 
    }
}
