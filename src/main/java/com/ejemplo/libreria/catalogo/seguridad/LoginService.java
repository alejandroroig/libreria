package com.ejemplo.libreria.catalogo.seguridad;

import com.ejemplo.libreria.catalogo.seguridad.dto.LoginRequestDTO;
import com.ejemplo.libreria.catalogo.seguridad.dto.LoginResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginResponseDTO iniciarSesion(LoginRequestDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.username(), dto.password())
        );

        String token = jwtService.generarToken(authentication);
        return new LoginResponseDTO(token, "Bearer", jwtService.getExpiresInSeconds());
    }
}
