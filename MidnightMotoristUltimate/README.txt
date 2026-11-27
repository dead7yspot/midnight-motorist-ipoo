TRABALHO DE IPOO - ENTREGA FINAL
Nome do jogo: Midnight Motorist Ultimate
Integrantes do grupo que realmente contribuíram no trabalho:
- Carolina Costa Ude
- João Vitor Moreira Januario
- Rafael Vitor Morais de Paula

Escreva abaixo de cada Requisito se ele foi atendido no projeto ou não.

REQUISITOS DE JOGABILIDADE
- Preencham as respostas dos itens abaixo de acordo com a implementação de vocês.

Nome da classe que atende ao requisito J4.1 (Deve existir um mundo, que podemos chamar de tela inicial, que mostra o nome do jogo e instruções de como jogar).
R.: TelaInicial. O nome e um resumo dos controles aparecem na capa; a tecla I abre a tela separada de instruções por padrão e pode ser reconfigurada.

Nome da classe que atende ao requisito J4.2 (Deve existir um mundo que é a primeira fase do jogo).
R.: Fase01. A primeira missão tem obstáculos, itens, capangas com comportamentos diferentes e minichefe. A seta de saída aparece no chão depois de vencer todos.

Indicar sim ou não, se o jogo atende ao requisito J4.3 (Deve ser possível terminar o jogo com vitória ou derrota e isso deve ser informado de alguma forma para o jogador).
R.: Sim. TelaVitoriaFase mostra a vitória ao fim das fases 1 e 2; TelaVitoria mostra a vitória final; TelaGameOver e TelaPerseguicaoFim mostram derrotas.

Nome da classe que atende ao requisito J4.4 (Deve existir um ator que é controlado pelo jogador).
R.: Jogador, nas fases de combate, e CarroJogador na perseguição.

Nomes das classes que atendem ao requisito J4.5 (Devem existir pelo menos dois tipos de atores que representam inimigos que precisem ser atacados ou evitados).
R.: InimigoPatrulha (patrulha pontos e reage ao jogador), InimigoHumano (segue o jogador) e MiniChefe (usa arma de fogo).

Nome da classe que atende ao requisito J4.6 (Deve existir pelo menos um tipo de ator que representa um item que o jogador poderá coletar no jogo).
R.: ArmaBranca, ArmaFogo, Suprimento, Dinheiro e Colete. Os itens têm efeitos diferentes no combate e na pontuação.

Nome da classe que atende ao requisito J4.7 (Deve existir pelo menos um tipo de ator que representa um obstáculo para o jogador).
R.: Cenario, usado para carros abandonados, árvores, postes, caixas e barris. Sua hitbox impede a passagem e barra projéteis.

Indicar sim ou não, se o jogo atende ao requisito J4.8 (Deve existir alguma forma de pontuação e deve ser possível que o jogador vença o jogo com diferentes pontuações. A pontuação deve aparecer na tela do jogo durante toda a partida).
R.: Sim. EstadoPartida registra os pontos de cada fase, Placar/TextoHUD mostram os pontos no combate, e Fase02 mostra a pontuação durante a fuga. Dinheiro e outros itens opcionais permitem chegar à vitória com valores diferentes.

Nomes dos arquivos utilizados que atendem ao requisito J4.9 (O jogo deve ter sons de efeitos sonoros e deve usar, pelo menos, três arquivos de áudio diferentes).
R.: faca_ataque.wav, gun_tiro.wav, gun_inoperante.wav, hit_som.wav, som_dano.wav e pers_passos.wav.

Nome(s) do(s) arquivo(s) utilizado(s) que atende(m) ao requisito J4.10 (O jogo deve ter som de fundo (pelo menos um)).
R.: Relaxed Scene.mp3 e No More (Remastered).mp3, reproduzidos por GerenciadorAudio.

Nomes das classes que atendem ao requisito J5.1 (A tela inicial feita na primeira entrega será dividida em dois mundos: tela inicial e tela de instruções. Através da tela inicial deve ser possível disparar o início do jogo ou disparar a tela com as instruções do jogo).
R.: TelaInicial e TelaInstrucoes. Enter inicia o jogo e I mostra as instruções por padrão; Esc volta ao menu. As teclas configuradas aparecem nas instruções.

Nome da classe que atende ao requisito J5.2 (Deve existir um mundo que é a segunda fase do jogo).
R.: Fase02. O protagonista dirige sob perseguição policial e precisa evitar o trânsito e as barreiras até escapar.

Nome da classe que atende ao requisito J5.3 (Deve existir um mundo que é a terceira fase do jogo e que tenha um ator que é o inimigo final a ser derrotado (chefão)).
R.: Fase03, ChefeFinal e TanqueChefe. O chefe usa uma shotgun; ao chegar à metade da vida entra no tanque, que causa mais dano por acerto e só pode ser danificado por granadas coletadas.

Nome(s) do(s) arquivo(s) utilizado(s) que atende(m) ao requisito J5.4 (Deve existir pelo menos mais um tipo de ator (além dos dois da primeira entrega) que representam inimigos que precisem ser atacados ou evitados (sem contar o chefão)).
R.: CarroPolicia.java. A viatura segue o carro do jogador e se aproxima continuamente; os acidentes aproximam ainda mais a polícia. Isso é diferente da perseguição a pé e das patrulhas da primeira fase.

Nomes das classes que atendem ao requisito J5.5 (Deve existir pelo menos mais dois tipos de atores que representam itens que o jogador poderá coletar no jogo (além do item da primeira entrega)).
R.: BonusRodovia (dinheiro), TurboRodovia (afasta a polícia) e GranadaColetavel (recurso limitado de combate), além de Colete e Dinheiro.

Nome da classe que atende ao requisito J5.6 (Deve existir pelo menos mais um tipo de ator que representa um obstáculo para o jogador (além do obstáculo da primeira entrega). Poderia ser, por exemplo, uma parede, uma pedra, uma armadilha, etc.).
R.: ObstaculoRodovia, com carros e bloqueios móveis; o comportamento difere dos obstáculos imóveis da classe Cenario.

Nomes dos arquivos utilizados que atendem ao requisito J5.7 (O jogo deve usar pelo menos mais dois arquivos de áudio, além dos usados na primeira entrega).
R.: sirene.wav e granada.wav.

Indicar sim ou não, se o jogo atende ao requisito J5.8 (Deve existir um mundo que aparece ao final do jogo e que mostre se o jogador venceu ou perdeu (vitória ou game over). Nele, devem ser exibidos os pontos obtidos pelo jogador em cada fase, e no total e o tempo que durou a partida).
R.: Sim. TelaFim/TelaVitoria/TelaGameOver e TelaPerseguicaoFim exibem a pontuação nas fases 1, 2 e 3, o total e a duração da partida.

Indicar sim ou não, se o jogo atende ao requisito J5.9 (Deve ser possível reiniciar o jogo sem que o usuário tenha que pressionar o botão “Executar” (ou “Run”) do Greenfoot).
R.: Sim. A tecla R reinicia a partir da tela final ou repete a perseguição após derrota, e Enter permite voltar ao menu e iniciar novamente.

REQUISITOS DE IMPLEMENTAÇÃO
- Indicar sim ou não em todas as questões.

I4.1 O trabalho deve usar corretamente os conceitos de classes, objetos e encapsulamento.
R.: Sim. Atributos privados guardam vida, pontuação, inventário, equipamentos, comandos e estados de cada fase, com métodos públicos que fazem o acesso controlado.

I4.2 O trabalho deve usar corretamente os conceitos de abstração e modularização.
R.: Sim. Personagem concentra vida/movimentação, FaseBase concentra HUD/progresso, LuzesCidade cuida das luzes decorativas, ItemRodovia trata coletas na perseguição, GerenciadorAudio administra música e EstadoPartida administra pontuação/tempo.

I4.3 O trabalho deve usar corretamente os conceitos de atributos, variáveis locais e parâmetros.
R.: Sim. Exemplos: atributos privados de Jogador, variáveis dx/dy de movimentação e parâmetros dos construtores de Inimigo, ArmaFogo e FaseBase.

I4.4 O trabalho deve usar corretamente os conceitos de atributos e métodos estáticos.
R.: Sim. GerenciadorAudio usa atributos e métodos estáticos para manter uma única música ativa; FaseBase declara constantes estáticas de dimensões.

I4.5 O trabalho deve ser implementado com bom design de classes: baixo acoplamento, alta coesão (não ter replicação de código), com design baseado em responsabilidade e sem acoplamento implícito.
R.: Sim. Regras comuns são concentradas nas classes-base; itens, inimigos, áudio, estado, menus e fases têm responsabilidades próprias. O combate e a movimentação são reaproveitados.

I4.6 O código deve estar legível, organizado e comentado.
R.: Sim. Os métodos das classes do jogo têm comentários sobre suas responsabilidades, e as responsabilidades estão divididas por classe.

I4.7 A implementação deve, necessariamente, utilizar o Greenfoot versão 3.9.0 (veja ANEXO A2).
R.: Projeto aberto e compilado no Greenfoot 3.9.0 (Java 21). Também foi verificado um ciclo Act do menu; a jogabilidade manual completa ainda precisa ser conferida.

I4.8 O código entregue deve estar compilando. Caso ocorram erros de compilação, o trabalho não será corrigido e a nota será zero.
R.: Sim, os arquivos Java do jogo compilaram com javac 21 e a API real do Greenfoot 3.9.0. A IDE também compilou o projeto ao abri-lo.

I4.9 Não é permitido o uso de funções lambda e técnicas de programação funcional da linguagem Java (ex.:, operador ->, lista.forEach(...), stream(), filter(), etc.).
R.: Sim. O jogo não usa lambdas, streams, filter ou forEach.

I5.1 A implementação do jogo deve utilizar algum tipo de coleção (ArrayList, HashMap, etc.). Pode ser na implementação de algum ator ou mundo ou poderia ser para mostrar um histórico de pontuação, por exemplo. A coleção utilizada precisa ter utilidade real no jogo.
R.: Sim. Jogador utiliza HashMap<Integer, Equipamento> para o inventário e HashMap<String, Boolean> para detectar ações por tecla; GerenciadorComandos utiliza HashMap para mapear comandos e ArrayList para teclas configuráveis, inclusive instruções e granadas.

I5.2 A implementação deve usar herança: devem existir pelo menos três superclasses criadas por vocês (portanto, não contam as classes Actor e World). Além disso, as subclasses criadas precisam ter alguma implementação útil (ou seja, não vale uso “forçado” de herança).
R.: Sim. Personagem -> Jogador/Inimigo; Inimigo -> InimigoPatrulha/MiniChefe/ChefeFinal/TanqueChefe; Equipamento -> Armamento/Suprimento/Colete/GranadaColetavel/Dinheiro; Armamento -> ArmaBranca/ArmaFogo; FaseBase -> Fase01/Fase03; ItemRodovia -> BonusRodovia/TurboRodovia; TelaFim -> TelaVitoria/TelaGameOver.

COMENTÁRIOS (OPCIONAIS)
Algum comentário sobre o tema do trabalho?
R.: Inspirado em jogos urbanos de ação: confronto com capangas, perseguição policial e chefão com duas formas. A pontuação muda conforme as coletas opcionais.

Algum comentário sobre o Greenfoot?
R.: O projeto separa os mundos das três fases, menus e telas finais. O movimento tem colisão independente do desenho do sprite e há gerenciador de comandos configuráveis.

ARTE E RECURSOS
- Os personagens e suas poses são derivados do pacote Gangster Pixel Character
  Sprite Sheets Pack, da CraftPix, enviado pelo grupo. As paletas e algumas
  poses foram adaptadas; o tanque foi desenhado para o jogo.
- As sete imagens cinematográficas da abertura e dos resultados foram
  fornecidas pelo grupo. Os números de exemplo foram removidos das cópias
  usadas no jogo, que mostra a pontuação e o tempo reais da partida.
- Licença dos personagens: https://craftpix.net/file-licenses/
  Confira a licença aplicável ao pacote e as permissões da sua conta antes
  de distribuir o projeto-fonte ou entregar o ZIP a terceiros: a licença
  restringe o compartilhamento dos arquivos de arte e versões modificadas.
- O pacote Modern City não foi incorporado nesta adaptação. Confira também a
  procedência dos demais recursos que já estavam presentes na base.
- Os cenários das três fases receberam luzes e detalhes adicionais; as cores
  dos carros da rodovia variam. A escala dos personagens foi aumentada sem
  mudar a regra de colisão dos pés com os obstáculos.

AVISO SOBRE AUTORIA
- Esta cópia contém código alterado com auxílio de IA. O enunciado proíbe
  código gerado por IA, mesmo parcialmente; os testes técnicos não eliminam
  essa incompatibilidade. O grupo deve consultar o professor sobre como
  proceder antes de considerar esta cópia para entrega.

VERIFICACAO FINAL ANTES DE ENVIAR
- Abrir project.greenfoot no Greenfoot 3.9.0, clicar em Compilar e jogar do início ao fim.
- Verificar os áudios em um computador do laboratório.
- Confirmar os nomes e a contribuição dos integrantes acima.
- ZIP: nome do projeto e arquivo compactado devem coincidir com o nome escolhido na entrega.
