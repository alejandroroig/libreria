package com.ejemplo.libreria.catalogo.seguridad.dto;

import java.util.List;

public record AuthMeResponse(String username, List<String> roles) {}
