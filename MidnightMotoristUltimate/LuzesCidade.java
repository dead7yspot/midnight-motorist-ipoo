import greenfoot.*;

/** Animacao discreta de letreiros, reflexos e luzes em cada cenario. */
public class LuzesCidade extends Actor {
    private final GreenfootImage primeira;
    private final GreenfootImage segunda;
    private int tempo;
    private boolean acesa;

    /** Prepara dois quadros transparentes sem criar obstaculos no mapa. */
    public LuzesCidade(int fase) {
        primeira = criarQuadro(fase, true);
        segunda = criarQuadro(fase, false);
        acesa = true;
        setImage(primeira);
    }

    /** Alterna as luzes periodicamente enquanto a fase esta em andamento. */
    @Override
    public void act() {
        tempo++;
        if (tempo >= 32) {
            tempo = 0;
            acesa = !acesa;
            setImage(acesa ? primeira : segunda);
        }
    }

    /** Desenha apenas pequenos detalhes, deixando os atores e o HUD livres. */
    private GreenfootImage criarQuadro(int fase, boolean forte) {
        GreenfootImage imagem = new GreenfootImage(1024, 572);
        if (fase == 1) {
            int[] letreiros = {30, 246, 486, 709, 890};
            Color[] cores = {
                new Color(246, 145, 166), new Color(255, 211, 135),
                new Color(226, 171, 240), new Color(137, 239, 220),
                new Color(255, 224, 137)
            };
            for (int i = 0; i < letreiros.length; i++) {
                if ((i % 2 == 0) == forte) {
                    imagem.setColor(cores[i]);
                    imagem.fillRect(letreiros[i] + 3, 72, 16, 2);
                    imagem.fillRect(letreiros[i] + 8, 92, 28, 1);
                }
            }
        } else if (fase == 2) {
            int inicio = forte ? 0 : 42;
            for (int x = inicio; x < 1024; x += 170) {
                imagem.setColor(new Color(255, 226, 160, 185));
                imagem.fillRect(x, 98, 12, 2);
                imagem.setColor(new Color(226, 95, 88, 160));
                imagem.fillRect(x + 20, 482, 12, 2);
            }
            imagem.setColor(forte ? new Color(164, 238, 225) : new Color(88, 160, 159));
            imagem.fillRect(352, 48, 32, 2);
        } else {
            imagem.setColor(forte ? new Color(255, 117, 88, 235) : new Color(113, 53, 45, 180));
            imagem.fillRect(324, 118, 9, 6);
            imagem.setColor(forte ? new Color(113, 53, 45, 180) : new Color(255, 117, 88, 235));
            imagem.fillRect(690, 118, 9, 6);
            imagem.setColor(new Color(250, 172, 112, forte ? 120 : 55));
            imagem.fillRect(322, 130, 12, 2);
            imagem.fillRect(688, 130, 12, 2);
        }
        return imagem;
    }
}
