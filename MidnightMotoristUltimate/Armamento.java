import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Equipamento q causa dano; resistencia = quantos usos ate ficar inutilizavel
 * 
 */
public class Armamento extends Equipamento
{
    private int resistencia;
    private int alcance;
    private int danoMaximo;

    /** Guarda o alcance, o dano e a quantidade de usos da arma. */
    public Armamento(String nome, String descricao, String imagem, int resistencia, int alcance, int danoMaximo) {
        super(nome, descricao, imagem);

        this.resistencia = resistencia;
        this.alcance = alcance;
        this.danoMaximo = danoMaximo;
    }

    /** Verifica se a arma ainda possui resistencia. */
    public boolean podeUsar() {
        return resistencia > 0;
    }

    // Gasta uma resistencia e sorteia o dano, de 1 ate danoMaximo
    public int usar() {
        resistencia--;

        if (resistencia == 0) {
            System.out.println(getNome() + " ficou inutilizavel.");
        }

        return Greenfoot.getRandomNumber(danoMaximo) + 1;
    }

    /** Consulta os usos restantes da arma. */
    public int getResistencia() {
        return resistencia;
    }

    /** Consulta a distancia maxima do ataque. */
    public int getAlcance() {
        return alcance;
    }
}
