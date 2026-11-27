import greenfoot.*;

/** indicador visual compacto da vida, que ficará no HUD; 
 *  A vida permanece sob controle do Jogador 
*/
public class BarraVida extends Actor {
    private final int vidaMaxima;
    private int ultimaVida = -1;
    
    // Cria a barra com o limite de vida do jogador
    public BarraVida(int vidaMaxima) {
        this.vidaMaxima = vidaMaxima;
        atualizar(vidaMaxima);
    }
    
    // desenha a barra (toda vez que a vida muda)
    public void atualizar(int vida) {
        if (vida == ultimaVida) 
            return;
        
        ultimaVida = vida;
        
        GreenfootImage imagem = new GreenfootImage(150, 20);
        
        imagem.fillRect(0, 0, 150, 20);
        imagem.fillRect(2, 2, 146, 16);
        
        // principal formula usada para criar a caixa de saude
        int largura = Math.max(0, 146 * vida / Math.max(1, vidaMaxima));
        
        if (vida >= 50){
            imagem.setColor(new Color(56, 180, 104)); // verde
        } else {
            imagem.setColor(new Color(180, 56, 66)); // vermelho
        }
        imagem.fillRect(2, 2, largura, 16);
        imagem.setColor(Color.WHITE);
        imagem.drawString("HP " + vida + "/" + vidaMaxima, 38, 15);
        
        setImage(imagem);
    }
}
