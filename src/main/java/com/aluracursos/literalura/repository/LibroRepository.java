package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    // Buscar libro por título
    Optional<Libro> findByTitle(String title);

    // Listar libros por idioma
    List<Libro> findByLanguage(String language);
}