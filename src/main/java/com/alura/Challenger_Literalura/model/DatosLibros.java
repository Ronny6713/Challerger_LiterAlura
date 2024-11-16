package com.alura.Challenger_Literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibros(@JsonAlias ("title") String titulo,
                          @JsonAlias ("languages") List<String> idioma ,
                          @JsonAlias ("download_count") Integer numeroDescargas,
                          @JsonAlias ("authors") List<DatosAutor> autores){
}
