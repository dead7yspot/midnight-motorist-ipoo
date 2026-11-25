import greenfoot.*;

// controla musicas do menu e das fases (metodos estaticos intencionais)
public class GerenciadorAudio {
    private static GreenfootSound musica;
    private static String arquivoAtual;
    private static boolean pausada;

    // Troca a musica de fundo, preservando a preferencia de pausa
    public static void iniciarMusica(String arquivo) {
        if (arquivo == null) 
            return;
        if (musica != null && arquivo.equals(arquivoAtual)) 
            return;
        pararMusica();
        
        musica = new GreenfootSound(arquivo);
        musica.setVolume(95);
        arquivoAtual = arquivo;
        if (!pausada) 
            musica.playLoop();
    }
    
    // Pausa ou retoma a musica atual
    public static void alternarMusica() {
        if (musica == null) 
            return;
        
        if (pausada) 
            musica.playLoop();
        else musica.pause();    
        
        pausada = !pausada;
    }

    // Para a musica e libera o arquivo sem esquecer a preferencia de pausa
    public static void pararMusica() {
        if (musica != null) 
            musica.stop();
        
        musica = null;
        arquivoAtual = null;
    }

    // Informa se a musica esta pausada
    public static boolean estaPausada() { 
        return pausada;
    }
}
