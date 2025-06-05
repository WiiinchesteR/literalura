package br.com.alura.literalura.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "idiomas")
public class Idioma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String abreviacao;

    @ManyToMany(mappedBy = "idioma")
    private List<Livro> livros;

    public Idioma() {}

    public Idioma(String idioma) {
        this.abreviacao = idioma;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Livro> getLivros() {
        return livros;
    }

    public void setLivros(List<Livro> livros) {
        this.livros = livros;
    }

    public String getAbreviacao() {
        return abreviacao;
    }

    public void setAbreviacao(String abreviacao) {
        this.abreviacao = abreviacao;
    }
}
