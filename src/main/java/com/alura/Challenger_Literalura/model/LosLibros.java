package com.alura.Challenger_Literalura.model;

import java.util.List;

public class LosLibros {
    private Integer resultados;
    private List<DatosLibros> todosLosLibros;

    public LosLibros(DatosLibros datosLibros){}

    public LosLibros(DatosDeLosLibros datosLibros){
        this.resultados = datosLibros.resultados();
        this.todosLosLibros = datosLibros.todosLosLibros();
    }
}
