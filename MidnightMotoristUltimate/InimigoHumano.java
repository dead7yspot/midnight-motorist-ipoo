import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/** Inimigo humano que segue o comportamento de perseguicao de Inimigo. */
public class InimigoHumano extends Inimigo
{
    /** Cria um capanga com a arma e as poses informadas pela fase. */
    public InimigoHumano(String gangue, int vida, int velocidade, Jogador jogador, int distanciaParada, Armamento arma,
            String parado,      // sprites
            String corridaA,
            String corridaB,
            String ataque
    )
    {
        super(gangue, vida, velocidade, jogador, distanciaParada, arma,
            parado,
            corridaA,
            corridaB,
            ataque
        ); 
    }

    /** Permite informar mais quadros sem mudar o construtor original do capanga. */
    public InimigoHumano(String gangue, int vida, int velocidade, Jogador jogador, int distanciaParada,
                         Armamento arma, String parado, String corridaA, String corridaB,
                         String ataque, String[] corridaCompleta) {
        this(gangue, vida, velocidade, jogador, distanciaParada, arma,
             parado, corridaA, corridaB, ataque);
        configurarQuadrosCorrida(corridaCompleta);
    }
}
