package com.ejemplo.libreria.catalogo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

/**
 * Ejemplo extra de la teoría: relación N:M con Libro.
 */
@Entity
@Table(name = "autor")
@Getter
@Setter
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    // Lado inverso: la tabla intermedia libro_autor la define Libro
    @ManyToMany(mappedBy = "autores")
    private List<Libro> libros = new ArrayList<>();
}
