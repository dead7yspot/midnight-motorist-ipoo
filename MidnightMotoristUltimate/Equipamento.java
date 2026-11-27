import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/** Item coletavel: identidade, descricao e representacao visual. */
public class Equipamento extends Actor
{
    private String nome;
    private String descricao;
    private String imagem;

    /** Guarda os dados do item e carrega sua imagem. */
    public Equipamento(String nome, String descricao, String imagem) {
        this.nome = nome;
        this.descricao = descricao;
        this.imagem = imagem;

        carregarImagem();
    }

    /** Aplica ao ator a imagem indicada no cadastro. */
    private void carregarImagem() {
        setImage(new GreenfootImage(imagem));
    }

    /** Consulta o nome do equipamento. */
    public String getNome() {
        return nome;
    }

    /** Consulta a descricao do equipamento. */
    public String getDescricao() {
        return descricao;
    }

    /** Consulta o nome do arquivo de imagem do equipamento. */
    public String getImagem() {
        return imagem;
    }
}
