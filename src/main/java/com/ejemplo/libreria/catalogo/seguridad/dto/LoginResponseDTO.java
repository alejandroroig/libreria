package com.ejemplo.libreria.catalogo.seguridad.dto;

public record LoginResponseDTO(
        String accessToken, String tokenType, long expiresInSeconds
) {}
