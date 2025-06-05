package br.com.alura.literalura.repository;

import br.com.alura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    Optional<Autor> findByNome(String name);

    @Query("SELECT a FROM Autor a WHERE a.dataMorte > :ano AND a.dataNascimento <= :ano")
    List<Autor> findByAutorVivo(String ano);
}
