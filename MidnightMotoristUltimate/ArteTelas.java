import greenfoot.*;

/** Carrega as sete artes de tela enviadas pelo grupo. */
public class ArteTelas {
    // Carrega a capa com espaco para os controles atuais. 
    public static GreenfootImage criarInicio() {
        return carregar("cinema_inicio.png");
    }

    //  Escolhe a arte correspondente a vitoria ou derrota de cada fase.
    public static GreenfootImage criarResultado(int fase, boolean vitoria) {
        String resultado = vitoria ? "vitoria" : "derrota";
        return carregar("cinema_" + resultado + fase + ".png");
    }

    // Ajusta a arte ao tamanho do mundo sem repetir titulo nem placar fixo.
    private static GreenfootImage carregar(String arquivo) {
        GreenfootImage imagem = new GreenfootImage(arquivo);
        imagem.scale(1024, 572);
        return imagem;
    }

    // Coloca um texto legivel sobre a arte da tela. 
    public static void texto(GreenfootImage fundo, String frase, int tamanho,
                             Color cor, int x, int y) {
        fundo.drawImage(new GreenfootImage(frase, tamanho, cor, null), x, y);
    }
}
