package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.Autor;
import com.aluracursos.literalura.model.DatosAutor;
import com.aluracursos.literalura.model.DatosLibro;
import com.aluracursos.literalura.model.Libro;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String apiurl = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<Autor> autores;
    private List<Libro> libros;
    private List<DatosAutor> datosAutores = new ArrayList<>();
    private List<DatosLibro> datosLibros = new ArrayList<>();

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion !=0) {
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
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1 -> buscarLibroPoritulo();
                case 2 -> listarLibrosRegistrados();
                case 3 -> listarAutoresRegistrados();
                case 4 -> listarAutoresVivosPorFecha();
                case 5 -> buscarLibrosPorIdioma();
            }
        }
    }
}

