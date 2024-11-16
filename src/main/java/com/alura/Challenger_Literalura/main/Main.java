package com.alura.Challenger_Literalura.main;

import com.alura.Challenger_Literalura.model.*;
import com.alura.Challenger_Literalura.repository.AutoresRepository;
import com.alura.Challenger_Literalura.repository.LibroRepository;
import com.alura.Challenger_Literalura.service.API;
import com.alura.Challenger_Literalura.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
@Component
public class Main {
    private Scanner keyboard = new Scanner(System.in);
    private API api = new API();
    private ConvierteDatos conversor = new ConvierteDatos();
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    @Autowired
    private AutoresRepository autoresRepository;
    @Autowired
    private LibroRepository libroRepository;
    private List<Autores> listaAutores;
    private List<Libro> listaLibros;



    public void mostrarMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    BIENVENIDO: 
                    1- Buscar Libro Por Titulo.
                    2- Listar De Libros Registrados.
                    3- Listar Autores Registrados.
                    4- Listar Autores Vivos En Un Determinado Año.
                    5- Listar Libros Por Idioma.
                    
                    0- Para Salir
                    """;
            System.out.println(menu);
            opcion = keyboard.nextInt();
            keyboard.nextLine();
            switch (opcion) {
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    librosDisponibles();
                    break;
                case 3:
                    consultarAutoresRegistrados();
                    break;
                case 4:
                    consultarAutoresVivosPorFecha();
                    break;
                case 5:
                    consultarLibrosPorIdioma();
                    break;
                default:
                    System.out.println("La Opcion Ingresada no es Valida");
            }
        }
    }
    //CASE 1
    private void buscarLibro() {
        System.out.println("Introduce el libro que quiere buscar:");
        var busqueda = keyboard.nextLine();
        String json = null;
        try {
            json = api.obtenerDatos(URL_BASE + busqueda.replace(" ", "+"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        var datos = conversor.obtenerDatos(json, DatosDeLosLibros.class);

        if (datos.resultados() != 0) {
            DatosLibros datosLibros = datos.todosLosLibros().stream()
                    .filter(l -> l.titulo().toUpperCase().contains(busqueda.toUpperCase()))
                    .findFirst()
                    .orElse(null);
            if (datosLibros != null) {
                Libro libro = new Libro(datosLibros);
                List<DatosAutor> autoresDatos = datosLibros.autores();
                Autores autor = new Autores();
                autor.setNombre(autoresDatos.get(0).nombre());
                autor.setNacimiento(autoresDatos.get(0).nacimiento());
                autor.setFallecimiento(autoresDatos.get(0).fallecimiento());
                libro.setAutores(autor);
                for (DatosAutor datosAutor : datosLibros.autores()) {
                    Autores autores;
                    Optional<Autores> autoresExistentes = autoresRepository.findAutoresByNombre(datosAutor.nombre());

                    if (autoresExistentes.isPresent()) {
                        libro.setAutores(autoresExistentes.get());
                        Optional<Libro> libroExistente = libroRepository.findByTitulo(libro.getTitulo());
                        if (libroExistente.isPresent()) {
                            System.out.println("***** NO ES POSIBLE GUARDAR EL LIBRO, YA EXISTE EN LA BASE DE DATOS *****" );
                            System.out.printf("""
                                |||||||||||||||||||||||||||||||||||||||||
                                libro: %s
                                Autor: %s
                                Idioma: %s
                                Descargas: %s
                                |||||||||||||||||||||||||||||||||||||||||
                                %n""",libro.getTitulo(), libro.getAutores().getNombre(), libro.getIdioma(), libro.getNumeroDescargas().toString());
                            return;
                        }
                    } else {
                        autoresRepository.save(autor);
                        libro.setAutores(autor);
                        libroRepository.save(libro);
                        System.out.printf("""
                                |||||||||||||||||||||||||||||||||||||||||
                                libro: %s
                                Autor: %s
                                Idioma: %s
                                Descargas: %s
                                |||||||||||||||||||||||||||||||||||||||||
                                %n""",libro.getTitulo(), libro.getAutores().getNombre(), libro.getIdioma(), libro.getNumeroDescargas().toString());
                    }
                }
            }
        }
    }
    //CASE 2
    private void consultarAutoresRegistrados() {
        listaAutores = autoresRepository.findAll();
        try {
            if (listaAutores.isEmpty()) {
                System.out.println("No hay libros registrados...");
                System.out.println("Presione Enter para continuar...");
                keyboard.nextLine();
            } else {
                listaAutores.forEach(a -> {
                    System.out.printf("""
                                |||||||||||||||||||||||||||||||||||||||||
                                Autor: %s
                                Nacimiento: Año:  %s , Fallecimiento: Año:%s
                                libros: [ %s ]       
                                |||||||||||||||||||||||||||||||||||||||||
                                %n""", a.getNombre(),a.getNacimiento(),a.getFallecimiento(),a.getLibros().stream()
                            .map(libro -> libro.getTitulo())
                            .collect(Collectors.joining(",")));
                });
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    //CASE 3
    private void librosDisponibles() {
        listaLibros = libroRepository.findAll();
        try {
            if (listaLibros.isEmpty()) {
                System.out.println("NO HAY LIBROS");
            } else {
                listaLibros.forEach(a -> {
                    System.out.printf("""
                            |||||||||||||||||||||||||||||||||||||||||
                            libro: %s
                            Autor: %s
                            Idioma: %s
                            Descargas: %s
                            |||||||||||||||||||||||||||||||||||||||||
                            %n""", a.getTitulo(),a.getAutores().getNombre(),a.getIdioma(),a.getNumeroDescargas());
                });
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    //CASE 4
    private void consultarAutoresVivosPorFecha() {
        System.out.println("ingrese una ficha");
        var fecha = keyboard.nextInt();
        listaAutores = autoresRepository.findByFecha(fecha);
        try {
            if (listaAutores.isEmpty()) {
                System.out.println("NINGUN RESULTADO DISPONIBLE");
            } else {
                System.out.println("Autores Vivos en el año "+fecha +": ");
                listaAutores.forEach(a -> {
                    String libros =  a.getLibros().stream()
                            .map(libro ->   libro.getTitulo())
                            .collect(Collectors.joining(","));

                    System.out.printf("""
                            |||||||||||||||||||||||||||||||||||||||||
                            Autor: %s
                            Libros: [%s ] 
                            |||||||||||||||||||||||||||||||||||||||||
                           %n""", a.getNombre(), libros);
                });
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    //CASE 5
    private void consultarLibrosPorIdioma() {
        listaLibros = libroRepository.findAll();
        System.out.println("""
                INGRESE EL NUMERO CORRESPONDIENTE AL IDIOMA: %n
                1- Libros en Ingles
                2- Libros en Español
                3- Libros en Chino
                4- Libros en Frances
                """);
        String bandera = keyboard.nextLine();
        if (bandera.equals("1")) {
            bandera = "en";}
        else if (bandera.equals("2")) {
            bandera = "es";
        }else if (bandera.equals("3")) {
            bandera = "zh";
        }else if (bandera.equals("4")) {
            bandera = "fr";
        }else {
            System.out.println("Opcion no valida");
        }
        final String opcion = bandera;
        try {listaLibros.forEach(a -> {
                    String idiomas = a.getIdioma().toString();
                    if (idiomas.toUpperCase().contains(opcion.toUpperCase())) {
                        System.out.printf("""
                                     |||||||||||||||||||||||||||||||||||||||||
                                     Autor: %s
                                     Libros: [%s ]
                                     Idiomas Disponibles: %s
                                     |||||||||||||||||||||||||||||||||||||||||
                                    %n""", a.getAutores().getNombre(), a.getTitulo(), a.getIdioma());
                    }
                });
              if (listaLibros.isEmpty()) {
                System.out.println("No hay libros registrados...");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
