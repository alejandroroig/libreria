package com.ejemplo.libreria.catalogo;

import com.ejemplo.libreria.catalogo.dto.EditorialDTO;
import com.ejemplo.libreria.catalogo.dto.LibroResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LibroController.class)
@AutoConfigureMockMvc(addFilters = false)
class LibroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LibroService libroService;

    @BeforeEach
    void setUp() {
        // Configuración inicial si es necesario
    }

/*    @Test
    void getAll_DebeDevolverListaDeLibros() throws Exception {
        var dto = new LibroResponseDTO(
                1L,
                "El nombre del viento",
                new BigDecimal("19.95"),
                LocalDate.of(2007, 3, 27),
                new EditorialDTO(1L, "Plaza & Janés")
        );

        // Arrange: Configurar el comportamiento del servicio simulado
        when(libroService.findAll())
                .thenReturn(List.of(dto));

        // Act: Realizar la solicitud al endpoint
        mockMvc.perform(get("/api/v1/libros"))
                // Assert: Verificar la respuesta
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo")
                        .value("El nombre del viento"));
    }*/

    @Test
    void getAll_DebeDevolverLibrosPaginados() throws Exception {

        var dto = new LibroResponseDTO(
                1L,
                "El nombre del viento",
                new BigDecimal("19.95"),
                LocalDate.of(2007, 3, 27),
                new EditorialDTO(1L, "Plaza & Janés")
        );

        Page<LibroResponseDTO> pagina =
                new PageImpl<>(List.of(dto));

        when(libroService.findAllPaginated(any(), any()))
                .thenReturn(pagina);

        mockMvc.perform(get("/api/v1/libros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].titulo")
                        .value("El nombre del viento"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void create_DebeDevolver400_CuandoElDtoNoEsValido() throws Exception {
        String body = """
            {
              "titulo": "",
              "precio": -5,
              "fechaPublicacion": null,
              "editorialId": -1
            }
            """;

        mockMvc.perform(post("/api/v1/libros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getById_DebeDevolver404_CuandoNoExiste() throws Exception {
        when(libroService.findById(999L))
                .thenThrow(new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Libro no encontrado"));

        mockMvc.perform(get("/api/v1/libros/999"))
                .andExpect(status().isNotFound());
    }
}
