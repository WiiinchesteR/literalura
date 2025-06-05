package br.com.alura.literalura.principal;

import br.com.alura.literalura.model.*;
import br.com.alura.literalura.repository.AutorRepository;
import br.com.alura.literalura.repository.LivroRepository;
import br.com.alura.literalura.service.ConsumoAPI;
import br.com.alura.literalura.service.ConverteDados;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private Scanner scanner = new Scanner(System.in);

    private ConsumoAPI consumoAPI = new ConsumoAPI();

    private ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "http://gutendex.com/books/?search=";

    private LivroRepository livroRepository;

    private AutorRepository autorRepository;

    public Principal(LivroRepository livroRepository, AutorRepository autorRepository) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
    }

    public void exibeMenu() {
        var opcao = -1;

        while (opcao != 0) {

            System.out.println("""
                
                ********** LITERALURA **********
                
                [1] BUSCAR LIVRO
                [2] LISTAR TODOS OS LIVROS
                [3] LISTAR AUTORES
                [4] LISTAR AUTORES VIVOS EM DETERMINADO ANO
                [5] LISTAR LIVROS POR IDIOMA
                
                [0] PARA SAIR
                """);
            System.out.print("Escolha a opção ==> ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 0:
                    System.out.println("\nSaindo da aplicação...");
                    break;
                case 1:
                    buscarLivro();
                    break;
                case 2:
                    listarLivros();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    listarAutoresVivos();
                    break;
                case 5:
                    listarLivrosPorIdioma();
                    break;
                default:
                    System.out.println("\nOpção inválida!\n");
                    break;
            }
        }
    }

    private void buscarLivro() {
        System.out.print("\nInforme o título do livro: ");
        var nome = scanner.nextLine();

        var respostaJson = consumoAPI.consumo(ENDERECO + nome.replace(" ", "%20")).toLowerCase();

        try {
            var json = conversor.objeto(respostaJson);

            DadosLivro dados = conversor.obterDados(String.valueOf(json.get("results").get(0)), DadosLivro.class);

            System.out.println("\n========== DADOS DO LIVRO ==========");
            System.out.println("Título: " + dados.titulo());
            dados.autor().forEach(a -> System.out.println("Autor: " + a.nome()));
            System.out.println("Idioma: " + dados.idioma().get(0));
            System.out.println("Total Downloads: " + dados.numeroDownloads());
            System.out.println("====================================\n");

            salvar(dados);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void salvar(DadosLivro dados) {
        Optional<Autor> autorArmazenado = autorRepository.findByNome(dados.autor().get(0).nome());
        List<Livro> colecaoLivro = new ArrayList<>();

        if (autorArmazenado.isPresent()) {
            Livro livro = new Livro();
            var autor = autorArmazenado.get();

            livro.setTitulo(dados.titulo());
            livro.setIdioma(dados.idioma().stream().map(Idioma::new)
                    .collect(Collectors.toList()));
            livro.setNumeroDownloads(dados.numeroDownloads());

            colecaoLivro.add(livro);
            autor.setLivro(colecaoLivro);

            autorRepository.save(autor);
        } else {
            Autor autor = new Autor();

            for (DadosAutor dAutor : dados.autor()) {
                autor.setNome(dAutor.nome());
                autor.setDataNascimento(dAutor.dataNascimento());
                autor.setDataMorte(dAutor.dataMorte());
            }

            Livro livro = new Livro(
                    dados.titulo(), autor, dados.idioma().stream().map(Idioma::new).collect(Collectors.toList()),
                    dados.numeroDownloads());

            livroRepository.save(livro);

        }
    }

    private void listarLivros() {
        List<Livro> livros = livroRepository.findAll();
        livros.forEach(System.out::println);
    }

    private void listarAutores() {
        List<Autor> autores = autorRepository.findAll();
        autores.forEach(System.out::println);
    }

    private void listarAutoresVivos() {
        System.out.print("\nInforme o ano para pesquisa: ");
        var ano = scanner.nextLine();

        List<Autor> autoresVivos = autorRepository.findByAutorVivo(ano);

        System.out.println("Autores vivos em " + ano);
        autoresVivos.forEach(System.out::println);
    }

    private void listarLivrosPorIdioma() {
        System.out.println("""
                
                Idiomas disponíveis:
                [EN] Inglês
                [ES] Espanhol
                [FR] Francês
                [PT] Português
                """);
        System.out.print("Digite o idioma para pesquisa: ");
        String linguagem = scanner.nextLine().toLowerCase();

        List<Livro> livros = livroRepository.findByIdioma(linguagem);

        if (livros.isEmpty()) {
            System.out.println("\nIdioma não encontrado ou sem livros cadastrados para o idioma " + linguagem);
        } else {
            System.out.println("\nLivros com o idioma " + linguagem);
            livros.forEach(System.out::println);
        }
    }

}
