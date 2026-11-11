package com.ejemplo.libreria.catalogo.seguridad;

import com.ejemplo.libreria.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@NullMarked
@Component
@RequiredArgsConstructor
public class ErrorResponseAccessDeniedHandler implements AccessDeniedHandler {

    private final JsonMapper jsonMapper;
    private final BearerTokenAccessDeniedHandler bearerHandler =
            new BearerTokenAccessDeniedHandler();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException exception) throws IOException {
        bearerHandler.handle(request, response, exception);

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now().toString(),
                HttpStatus.FORBIDDEN.value(),
                "Acceso denegado",
                "No tienes permisos para realizar esta operación",
                request.getRequestURI()
        );

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(jsonMapper.writeValueAsString(error));
    }
}
