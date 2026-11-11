package com.ejemplo.libreria.catalogo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Ejemplo extra de la teoría: relación 1:1 con Libro.
 */
@Entity
@Table(name = "ficha_tecnica")
@Getter
@Setter
public class FichaTecnica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer numeroPaginas;
    private String idioma;

    // unique = true es lo que convierte esta FK en un "uno a uno"
    @OneToOne
    @JoinColumn(name = "libro_id", unique = true)
    private Libro libro;
}
