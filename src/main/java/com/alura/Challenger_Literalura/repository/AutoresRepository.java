package com.alura.Challenger_Literalura.repository;

import com.alura.Challenger_Literalura.model.Autores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutoresRepository extends JpaRepository<Autores, Long> {


   Optional<Autores> findAutoresByNombre(String nombre);

   @Query("SELECT a FROM Autores a WHERE a.nacimiento <= :fecha AND a.fallecimiento >= :fecha")
   List<Autores> findByFecha(int fecha);
}
