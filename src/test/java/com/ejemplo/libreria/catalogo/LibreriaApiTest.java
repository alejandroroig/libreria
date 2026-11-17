package com.ejemplo.libreria.catalogo;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import static org.hamcrest.Matchers.equalTo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
class LibreriaApiTest {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer("postgres:18-alpine");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EditorialRepository editorialRepository;

    private Long editorialId;

    @BeforeEach
    void prepararEditorial() {
        Editorial editorial = new Editorial();
        editorial.setNombre("Editorial de integración");
        editorial.setPais("España");
        editorialId = editorialRepository.save(editorial).getId();
    }

    @Test
    void adminPuedeCrearYRecuperarUnLibroConJsonb() throws Exception {
        String token = login("admin", "admintest123");

        String respuesta = mockMvc.perform(post("/api/v1/libros")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(libroJson("Rayuela demo")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andReturn().getResponse().getContentAsString();

        Number id = JsonPath.read(respuesta, "$.id");
        mockMvc.perform(get("/api/v1/libros/{id}", id.longValue()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Rayuela demo"))
                .andExpect(jsonPath("$.detallesEdicion.ebook.formato").value("EPUB"));

        mockMvc.perform(get("/api/v1/libros")
                        .param("titulo", "Rayuela demo")
                        .param("edicion", "ebook")
                        .param("formato", "epub"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].id").value(equalTo(id.longValue()), Long.class))
                .andExpect(jsonPath("$.content[0].detallesEdicion.ebook.formato").value("EPUB"));

        mockMvc.perform(get("/api/v1/libros")
                        .param("titulo", "Rayuela demo")
                        .param("edicion", "ebook")
                        .param("formato", "pdf"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    void userRecibeUn403ConElFormatoErrorResponse() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(credenciales("user-test", "usertest123")))
                .andExpect(status().isCreated());
        String token = login("user-test", "usertest123");

        mockMvc.perform(post("/api/v1/libros")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(libroJson("Libro que USER no puede crear")))
                .andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.error").value("Acceso denegado"))
                .andExpect(jsonPath("$.path").value("/api/v1/libros"));
    }

    private String login(String username, String password) throws Exception {
        String respuesta = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(credenciales(username, password)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return JsonPath.read(respuesta, "$.accessToken");
    }

    private String credenciales(String username, String password) {
        return """
                {"username":"%s","password":"%s"}
                """.formatted(username, password);
    }

    private String libroJson(String titulo) {
        return """
                {
                  "titulo": "%s",
                  "precio": 16.40,
                  "fechaPublicacion": "1963-06-28",
                  "editorialId": %d,
                  "detallesEdicion": {"ebook": {"formato": "EPUB"}}
                }
                """.formatted(titulo, editorialId);
    }
}
