package br.com.alura.literalura.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Autor autor;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "livro_linguagens",
            joinColumns = @JoinColumn(name = "livro_id"),
            inverseJoinColumns = @JoinColumn(name = "linguagem_id")
    )
    private List<Idioma> idioma;

    private Integer numeroDownloads;

    public Livro() {}

    public Livro(String titulo, Autor autor, List<Idioma> idioma, Integer numeroDownloads) {
        this.titulo = titulo;
        this.autor = autor;
        this.idioma = idioma;
        this.numeroDownloads = numeroDownloads;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public List<Idioma> getIdioma() {
        return idioma;
    }

    public void setIdioma(List<Idioma> idioma) {
        this.idioma = idioma;
    }

    public Integer getNumeroDownloads() {
        return numeroDownloads;
    }

    public void setNumeroDownloads(Integer numeroDownloads) {
        this.numeroDownloads = numeroDownloads;
    }

    @Override
    public String toString() {
        return  "\n-=-=-= LIVRO ENCONTRADO -=-=-=" + "\n" +
                "Título: " + this.titulo + "\n" +
                "Autor: " + this.autor.getNome() + "\n" +
                "Idioma: " + this.idioma.stream().map(Idioma::getAbreviacao).toList() + "\n" +
                "Downloads: " + this.numeroDownloads + "\n" +
                "-------------------------------" + "\n";
    }
}
