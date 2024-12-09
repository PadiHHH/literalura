<img src="https://img.shields.io/badge/STATUS-EN%20DESAROLLO-green">

# Literalura - Biblioteca Digital

## Descripción del Proyecto
Aplicación de gestión de biblioteca digital desarrollada como parte del Challenge de Java por Alura LATAM y Oracle ONE. El proyecto permite buscar, registrar y gestionar libros y autores utilizando la API de Gutendex, con persistencia de datos en PostgreSQL.

## Tecnologías Utilizadas
- Java 17
- Spring Boot
- PostgreSQL
- [Gutendex API](https://gutendex.com/) - Catálogo de libros gratuitos
- Maven - Gestión de dependencias
- Jackson - Procesamiento de JSON
- Spring Data JPA

## Características
- Búsqueda de libros por título
- Registro automático de libros y autores
- Listado de libros registrados
- Listado de autores registrados
- Filtrado de autores por año de actividad
- Búsqueda de libros por idioma
- Interfaz de consola interactiva
- Persistencia de datos en base de datos

## Prerrequisitos
- Java JDK 17 o superior
- Maven
- PostgreSQL
- Conexión a Internet

## Configuración

1. Clonar el repositorio:
```bash
git clone https://github.com/tu-usuario/literalura.git
```
2. Configurar variables de entorno en application.properties:
```properties
DB_HOST=localhost
DB_USER=tu_usuario
DB_PASSWORD=tu_contraseña
```
3. Instalar dependencias:
```bash
mvn clean install
```

## Estructura del Proyecto
```
literalura/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── aluracursos/
│       │           └── literalura/
│       │               ├── model/
│       │               │   ├── Autor.java
│       │               │   ├── Libro.java
│       │               │   ├── DatosAutor.java
│       │               │   └── DatosLibro.java
│       │               ├── repository/
│       │               │   ├── AutorRepository.java
│       │               │   └── LibroRepository.java
│       │               ├── service/
│       │               │   ├── ConsumoAPI.java
│       │               │   └── ConvierteDatos.java
│       │               └── principal/
│       │                   └── Principal.java
│       └── resources/
│           └── application.properties
├── pom.xml
└── README.md
```

## Uso
```java
********** Elija la opción a través de su número **********
1 - Buscar Libro por Título
2 - Listar Libros Registrados
3 - Listar Autores Registrados
4 - Listar Autores Vivos en un Determinado Año
5 - Listar Libros por Idioma
0 - Salir
***********************************************************
```
