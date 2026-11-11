package com.ejemplo.libreria.catalogo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface EditorialRepository extends JpaRepository<Editorial, Long> {

    // Consulta con JPQL
    @Query("SELECT e FROM Editorial e LEFT JOIN e.libros l GROUP BY e ORDER BY COUNT(l) DESC, e.nombre ASC")
    List<Editorial> rankingPorNumeroDeLibros();

    // Consulta con SQL nativo
    @Query(value = """
        SELECT e.*, ROW_NUMBER() OVER (ORDER BY COUNT(l.id) DESC) AS posicion
        FROM editorial e LEFT JOIN libro l ON l.editorial_id = e.id
        GROUP BY e.id
        """, nativeQuery = true)
    List<Object[]> rankingConPosicion();
}

