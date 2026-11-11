package com.ejemplo.libreria.catalogo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Lado "muchos" de la relación 1:N Editorial - Libro.
 * Equivale a Videojuego en GameVault.
 */
@Entity
@Table(name = "libro")
@Getter
@Setter
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private BigDecimal precio;            // BigDecimal, no double: es dinero
    private LocalDate fechaPublicacion;   // columna fecha_publicacion (tipo date)

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> detallesEdicion;

    // 1:N — aquí está la clave foránea real: columna editorial_id en la tabla libro
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "editorial_id")
    private Editorial editorial;

    // ---------------------------------------------------------------
    // Ejemplos extra de la teoría (tipos de relación). No están en la
    // Actividad 1.1: sirven para enseñar 1:1 y N:M en la base de datos.
    // ---------------------------------------------------------------

    // 1:1 — lado inverso; la FK (libro_id, unique) vive en ficha_tecnica
    @OneToOne(mappedBy = "libro", cascade = CascadeType.ALL)
    private FichaTecnica fichaTecnica;

    // N:M — Libro es el lado propietario: define la tabla intermedia libro_autor
    @ManyToMany
    @JoinTable(
        name = "libro_autor",
        joinColumns = @JoinColumn(name = "libro_id"),
        inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private List<Autor> autores = new ArrayList<>();
}
