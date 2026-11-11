package com.ejemplo.libreria.catalogo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

/**
 * Lado "uno" de la relación 1:N Editorial - Libro.
 * Equivale a Estudio en GameVault.
 */
@Entity
@Table(name = "editorial")
@Getter
@Setter
public class Editorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String pais;

    // mappedBy: la clave foránea real vive en Libro (columna editorial_id), no aquí
    @OneToMany(mappedBy = "editorial", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Libro> libros = new ArrayList<>();
}
