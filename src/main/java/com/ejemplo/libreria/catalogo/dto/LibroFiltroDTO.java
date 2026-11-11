package com.ejemplo.libreria.catalogo.dto;

import java.math.BigDecimal;

public record LibroFiltroDTO(
        String titulo,
        BigDecimal precioMin,
        BigDecimal precioMax,
        Long editorialId,
        String edicion,
        String formato
) {}

