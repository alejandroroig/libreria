package com.ejemplo.libreria.catalogo.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public record LibroCreateDTO(
        @NotBlank(message = "El título no puede estar vacío")
        @Size(max = 150, message = "El título no puede superar los 150 caracteres")
        String titulo,

        @NotNull(message = "El precio es obligatorio")
        @PositiveOrZero(message = "El precio no puede ser negativo")
        BigDecimal precio,

        @PastOrPresent(message = "La fecha de publicación no puede ser futura")
        @NotNull(message = "La fecha de publicación es obligatoria")
        LocalDate fechaPublicacion,

        @NotNull(message = "La editorial es obligatoria")
        @Positive(message = "La editorial debe tener un identificador válido")
        Long editorialId,

        Map<String, Object> detallesEdicion

) {}
