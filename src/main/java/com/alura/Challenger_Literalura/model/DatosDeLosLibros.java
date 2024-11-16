package com.alura.Challenger_Literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosDeLosLibros(
        @JsonAlias ("count") Integer resultados,
        @JsonAlias("results" )List<DatosLibros> todosLosLibros) {
}
