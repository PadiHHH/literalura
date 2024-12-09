package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.Autor;
import com.aluracursos.literalura.model.DatosAutor;
import com.aluracursos.literalura.model.DatosLibro;
import com.aluracursos.literalura.model.Libro;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class Principal {
    @Autowired
    private Scanner teclado;
    @Autowired
    private ConsumoAPI consumoAPI;
    @Autowired
    private ConvierteDatos conversor;
    @Autowired
    private LibroRepository libroRepository;
    @Autowired
    private AutorRepository autorRepository;
    private final String apiurl = "https://gutendex.com/books/";
    private List<DatosLibro> datosLibros = new ArrayList<>();
    private List<DatosAutor> datosAutores = new ArrayList<>();
    private List<Libro> libros;
    private List<Autor> autores;

    public void muestraElMenu() {
        int opcion = -1;
        while (opcion != 0) {
            var menu = """ 
                    ********** Elija la opción a través de su número **********
                    1 - Buscar Libro por Título
                    2 - Listar Libros Registrados
                    3 - Listar Autores Registrados
                    4 - Listar Autores Vivos en un Determinado Año
                    5 - Listar Libros por Idioma
                    0 - Salir
                    *********************************************************** 
                    """;
            System.out.println(menu);
            try {
                opcion = teclado.nextInt();
                teclado.nextLine();
                switch (opcion) {
                    case 1:
                        buscarLibroPorTitulo();
                        break;
                    case 2:
                        listarLibrosRegistrados();
                        break;
                    case 3:
                        listarAutoresRegistrados();
                        break;
                    case 4:
                        listarAutoresVivosPorFecha();
                        break;
                    case 5:
                        buscarLibrosPorIdioma();
                        break;
                    case 0:
                        System.out.println("Saliendo del programa...");
                        // Cerrar recursos si es necesario
                        teclado.close(); // Cerrar el scanner
                        System.exit(0); // Terminar la aplicación
                        break;
                    default:
                        System.out.println("Opción invalida. Ingrese una opción válida.");
                }
            } catch (Exception e) {
                System.out.println("Error: Ingrese un número válido");
                // Limpiar el buffer del scanner
                teclado.nextLine();
                opcion = -1;
            }
        }
    }

    private void buscarLibroPorTitulo() {
        System.out.println("Introduce el título del libro que deseas encontrar:");
        String tituloBuscado = teclado.nextLine();

        try {
            String urlConsulta = apiurl + "?search=" + URLEncoder.encode(tituloBuscado, StandardCharsets.UTF_8);
            String respuestaJson = consumoAPI.obtenerDatos(urlConsulta);

            var rootNode = conversor.obtenerDatos(respuestaJson, Map.class);
            var resultados = (List<Map<String, Object>>) rootNode.get("results");

            if (resultados == null || resultados.isEmpty()) {
                System.out.println("No se encontraron libros con ese título.");
                return;
            }

            resultados.forEach(resultado -> {
                try {
                    var datosLibro = conversor.obtenerDatos(new ObjectMapper().writeValueAsString(resultado), DatosLibro.class);
                    Libro libro = new Libro(datosLibro);

                    // Guardar autores asociados
                    datosLibro.authors().forEach(datosAutor -> {
                        Autor autor = new Autor(datosAutor);
                        if (!autorRepository.existsByName(autor.getName())) {
                            autorRepository.save(autor);
                            System.out.println("Nuevo autor registrado: " + autor.getName());
                        }
                    });

                    // Guardar libro
                    libroRepository.save(libro);
                    System.out.println("Libro guardado: " + libro.getTitle());

                } catch (Exception e) {
                    System.err.println("Error al procesar libro: " + e.getMessage());
                }
            });

        } catch (Exception e) {
            System.err.println("Ups! Algo salió mal en la búsqueda: " + e.getMessage());
        }
    }

    private void listarLibrosRegistrados() {
        List<Libro> librosEncontrados = libroRepository.findAll();

        if (librosEncontrados.isEmpty()) {
            System.out.println("Estantería vacía. Aún no hay libros registrados.");
            return;
        }

        System.out.println("\n--- Catálogo de Libros ---");
        librosEncontrados.forEach(libro -> {
            System.out.printf("""
                            %s
                            Autor: %s
                            Idioma: %s
                            Descargas: %d
                            --------------------
                            %n""",
                    libro.getTitle(),
                    libro.getAuthor(),
                    libro.getLanguage(),
                    libro.getDownload_count());
        });
    }

    private void listarAutoresRegistrados() {
        List<Autor> autoresEncontrados = autorRepository.findAll();

        if (autoresEncontrados.isEmpty()) {
            System.out.println("Registro de autores está vacío.");
            return;
        }

        System.out.println("\n--- Galería de Autores ---");
        autoresEncontrados.forEach(autor -> {
            System.out.printf("""
                            %s
                            Nacimiento: %s
                            Fallecimiento: %s
                            --------------------
                            %n""",
                    autor.getName(),
                    autor.getBirth_day() != null ? autor.getBirth_day() : "Desconocido",
                    autor.getDeath_day() != null ? autor.getDeath_day() : "Continúa");
        });
    }

    private void listarAutoresVivosPorFecha() {
        System.out.println("Ingresa el año para encontrar autores activos:");

        try {
            int añoConsulta = teclado.nextInt();
            teclado.nextLine();

            List<Autor> autoresVivos = autorRepository.findAuthorsAliveInYear(añoConsulta);

            if (autoresVivos.isEmpty()) {
                System.out.println("Ningún autor registrado vivía en " + añoConsulta);
                return;
            }

            System.out.println("\n--- Autores Contemporáneos en " + añoConsulta + " ---");
            autoresVivos.forEach(autor -> {
                System.out.printf("""
                                %s
                                Período activo: %d - %s
                                --------------------
                                %n""",
                        autor.getName(),
                        autor.getBirth_day(),
                        autor.getDeath_day() != null ? autor.getDeath_day() : "Actualidad");
            });

        } catch (Exception e) {
            System.err.println("Error en la búsqueda de autores: " + e.getMessage());
            teclado.nextLine();
        }
    }

    private void buscarLibrosPorIdioma() {
        Map<Integer, String> idiomasDisponibles = Map.of(
                1, "en", 2, "es", 3, "fr",
                4, "de", 5, "it"
        );

        System.out.println("\n--- Biblioteca Multilingüe ---");
        idiomasDisponibles.forEach((key, value) ->
                System.out.println(key + ": " + obtenerNombreIdioma(value))
        );
        System.out.println("6: Otro idioma");

        try {
            int seleccion = teclado.nextInt();
            teclado.nextLine();

            String idioma = (seleccion == 6)
                    ? solicitarIdiomaPersonalizado()
                    : idiomasDisponibles.getOrDefault(seleccion, null);

            if (idioma == null) {
                System.out.println("Selección inválida.");
                return;
            }

            List<Libro> librosPorIdioma = libroRepository.findByLanguage(idioma);

            mostrarLibrosPorIdioma(idioma, librosPorIdioma);

        } catch (Exception e) {
            System.err.println("Problema al buscar libros: " + e.getMessage());
            teclado.nextLine();
        }
    }

    private String obtenerNombreIdioma(String codigoIdioma) {
        Map<String, String> nombreIdiomas = Map.of(
                "en", "Inglés", "es", "Español",
                "fr", "Francés", "de", "Alemán",
                "it", "Italiano"
        );
        return nombreIdiomas.getOrDefault(codigoIdioma, "Desconocido");
    }

    private String solicitarIdiomaPersonalizado() {
        System.out.println("Ingresa código de idioma (ej. en, es, fr):");
        return teclado.nextLine().trim().toLowerCase();
    }

    private void mostrarLibrosPorIdioma(String idioma, List<Libro> libros) {
        if (libros.isEmpty()) {
            System.out.println("No hay libros en " + obtenerNombreIdioma(idioma));
            return;
        }

        System.out.println("\n--- Libros en " + obtenerNombreIdioma(idioma) + " ---");
        libros.forEach(libro -> {
            System.out.printf("""
                            %s
                            Autor: %s
                            Descargas: %d
                            --------------------
                            %n""",
                    libro.getTitle(),
                    libro.getAuthor(),
                    libro.getDownload_count());
        });
    }
}