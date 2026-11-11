package com.ejemplo.libreria.catalogo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long>, JpaSpecificationExecutor<Libro> {
    // Consulta con JPQL
    @Query("SELECT l FROM Libro l WHERE l.editorial.nombre = :nombreEditorial")
    List<Libro> buscarPorNombreDeEditorial(@Param("nombreEditorial") String nombreEditorial);
}

