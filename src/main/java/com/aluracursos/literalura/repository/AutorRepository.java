package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    // Buscar autor por nombre
    Optional<Autor> findByName(String name);

    // Listar autores vivos en un año específico
    @Query("SELECT a FROM Autor a WHERE :year BETWEEN a.birth_day AND a.death_day")
    List<Autor> findAutoresVivosEnAno(Integer year);
}
