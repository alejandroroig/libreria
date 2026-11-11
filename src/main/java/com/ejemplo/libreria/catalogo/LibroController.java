package com.ejemplo.libreria.catalogo;

import com.ejemplo.libreria.catalogo.dto.LibroCreateDTO;
import com.ejemplo.libreria.catalogo.dto.LibroResponseDTO;
import com.ejemplo.libreria.catalogo.dto.LibroFiltroDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/libros")
@RequiredArgsConstructor
public class LibroController {
    private final LibroService libroService;

    @GetMapping
    public ResponseEntity<Page<LibroResponseDTO>> getAll(
            // @ParameterObject hace que SpringDoc genere la documentación de los parámetros de filtro y paginación correctamente
            @ParameterObject @ModelAttribute LibroFiltroDTO filtro,
            @ParameterObject @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(libroService.findAllPaginated(filtro, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(libroService.findById(id));
    }

    @Operation(summary = "Crear un libro nuevo")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Libro creado correctamente"),
            @ApiResponse(
                    responseCode = "400",
                    description = "El cuerpo de la petición no supera las validaciones"),
            @ApiResponse(
                    responseCode = "404",
                    description = "La editorial indicada no existe")
    })
    @PostMapping
    public ResponseEntity<LibroResponseDTO> create(
            @Valid @RequestBody LibroCreateDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(libroService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody LibroCreateDTO dto) {
        return ResponseEntity.ok(libroService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        libroService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/editorial/{nombre}")
    public ResponseEntity<List<LibroResponseDTO>> getPorEditorial(@PathVariable String nombre) {
        return ResponseEntity.ok(libroService.buscarPorEditorial(nombre));
    }
}