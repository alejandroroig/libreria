package com.ejemplo.libreria.catalogo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public record LibroResponseDTO(
        Long id,
        String titulo,
        BigDecimal precio,
        LocalDate fechaPublicacion,
        EditorialDTO editorial,
        Map<String, Object> detallesEdicion
) {}
