package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    // Buscar autor por nombre
    Optional<Autor> findByName(String name);

    // Listar autores vivos en un año específico
    @Query("SELECT a FROM Autor a WHERE " +
            "(a.birth_day IS NOT NULL AND a.birth_day <= :year) AND " +
            "(a.death_day IS NULL OR a.death_day >= :year)")
    List<Autor> findAuthorsAliveInYear(@Param("year") Integer year);

    boolean existsByName(String name);
}
