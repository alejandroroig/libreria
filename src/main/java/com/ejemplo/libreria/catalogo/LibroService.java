package com.ejemplo.libreria.catalogo;

import com.ejemplo.libreria.catalogo.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LibroService {
    private final LibroRepository libroRepository;
    private final EditorialRepository editorialRepository;

    @Transactional(readOnly = true)
    public LibroResponseDTO findById(Long id) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado"));
        return mapToDTO(libro);
    }

    @Transactional(readOnly = true)
    public List<LibroResponseDTO> findAll() {
        return libroRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    private LibroResponseDTO mapToDTO(Libro libro) {
        EditorialDTO editorialDTO = new EditorialDTO(
                libro.getEditorial().getId(),
                libro.getEditorial().getNombre()
        );

        return new LibroResponseDTO(
                libro.getId(),
                libro.getTitulo(),
                libro.getPrecio(),
                libro.getFechaPublicacion(),
                editorialDTO,
                libro.getDetallesEdicion()
        );
    }

    @Transactional
    public LibroResponseDTO create(LibroCreateDTO dto) {
        Editorial editorial = editorialRepository.findById(dto.editorialId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Editorial no encontrada"));

        Libro libro = new Libro();
        libro.setTitulo(dto.titulo());
        libro.setPrecio(dto.precio());
        libro.setFechaPublicacion(dto.fechaPublicacion());
        libro.setEditorial(editorial);
        libro.setDetallesEdicion(dto.detallesEdicion());

        return mapToDTO(libroRepository.save(libro));
    }

    @Transactional
    public LibroResponseDTO update(Long id, LibroCreateDTO dto) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado"));

        Editorial editorial = editorialRepository.findById(dto.editorialId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Editorial no encontrada"));

        libro.setTitulo(dto.titulo());
        libro.setPrecio(dto.precio());
        libro.setFechaPublicacion(dto.fechaPublicacion());
        libro.setEditorial(editorial);
        libro.setDetallesEdicion(dto.detallesEdicion());

        return mapToDTO(libroRepository.save(libro));
    }

    @Transactional
    public void delete(Long id) {
        if (!libroRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado");
        }
        libroRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<LibroResponseDTO> findByFiltro(LibroFiltroDTO filtro) {
        Specification<Libro> spec = Specification
                .where(LibroSpecifications.tituloContiene(filtro.titulo()))
                .and(LibroSpecifications.precioMayorOIgualA(filtro.precioMin()))
                .and(LibroSpecifications.precioMenorOIgualA(filtro.precioMax()))
                .and(LibroSpecifications.perteneceAEditorial(filtro.editorialId()))
                .and(LibroSpecifications.disponibleEnEdicion(filtro.edicion()))
                .and(LibroSpecifications.disponibleEnFormato(filtro.edicion(), filtro.formato()));

        return libroRepository.findAll(spec)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<LibroResponseDTO> findAllPaginated(LibroFiltroDTO filtro, Pageable pageable) {
        Specification<Libro> spec = Specification
                .where(LibroSpecifications.tituloContiene(filtro.titulo()))
                .and(LibroSpecifications.precioMayorOIgualA(filtro.precioMin()))
                .and(LibroSpecifications.precioMenorOIgualA(filtro.precioMax()))
                .and(LibroSpecifications.perteneceAEditorial(filtro.editorialId()))
                .and(LibroSpecifications.disponibleEnEdicion(filtro.edicion()))
                .and(LibroSpecifications.disponibleEnFormato(filtro.edicion(), filtro.formato()));

        return libroRepository.findAll(spec, pageable)
                .map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public List<LibroResponseDTO> buscarPorEditorial(String nombreEditorial) {
        return libroRepository.buscarPorNombreDeEditorial(nombreEditorial).stream()
                .map(this::mapToDTO)
                .toList();
    }
}
