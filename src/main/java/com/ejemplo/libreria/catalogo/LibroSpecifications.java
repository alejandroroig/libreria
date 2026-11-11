package com.ejemplo.libreria.catalogo;

import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Locale;

public final class LibroSpecifications {

    private LibroSpecifications() {
    }

    public static Specification<Libro> tituloContiene(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            return Specification.unrestricted();
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("titulo")), "%" + titulo.toLowerCase() + "%");
    }

    public static Specification<Libro> precioMayorOIgualA(BigDecimal precioMin) {
        if (precioMin == null) {
            return Specification.unrestricted();
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("precio"), precioMin);
    }

    public static Specification<Libro> precioMenorOIgualA(BigDecimal precioMax) {
        if (precioMax == null) {
            return Specification.unrestricted();
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("precio"), precioMax);
    }

    public static Specification<Libro> perteneceAEditorial(Long editorialId) {
        if (editorialId == null) {
            return Specification.unrestricted();
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("editorial").get("id"), editorialId);
    }

    public static Specification<Libro> disponibleEnEdicion(String edicion) {
        if (edicion == null || edicion.isBlank()) {
            return Specification.unrestricted();
        }

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(
                        criteriaBuilder.function(
                                "jsonb_exists",
                                Boolean.class,
                                root.get("detallesEdicion"),
                                criteriaBuilder.literal(edicion.toLowerCase())
                        )
                );
    }

    /*
    public static Specification<Libro> disponibleEnFormato(String edicion, String formato) {
        if (formato == null || formato.isBlank()) {
            return Specification.unrestricted();
        }

        String formatoBuscado = formato.strip().toLowerCase(Locale.ROOT);

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        criteriaBuilder.lower(
                                criteriaBuilder.function(
                                        "jsonb_extract_path_text",
                                        String.class,
                                        root.get("detallesEdicion"),
                                        criteriaBuilder.literal(edicion.toLowerCase()),
                                        criteriaBuilder.literal("formato")
                                )
                        ),
                        formatoBuscado);
    }
    */

    // Válido tanto si en la query aparece el formato únicamente o la edición y el formato
    public static Specification<Libro> disponibleEnFormato(String edicion, String formato) {
        if (formato == null || formato.isBlank()) {
            return Specification.unrestricted();
        }

        String edicionBuscada = edicion == null ? "" : edicion.strip().toLowerCase(Locale.ROOT);
        String formatoBuscado = formato.strip().toLowerCase(Locale.ROOT);

        return (root, query, cb) -> {
            HibernateCriteriaBuilder hcb = (HibernateCriteriaBuilder) cb;

            return hcb.isTrue(hcb.sql("""
                exists (
                    select 1
                    from jsonb_each(?) as e(clave, datos)
                    where lower(e.datos ->> 'formato') = ?
                      and (? = '' or e.clave = ?)
                )
                """,
                    Boolean.class,
                    root.get("detallesEdicion"),
                    hcb.literal(formatoBuscado),
                    hcb.literal(edicionBuscada),
                    hcb.literal(edicionBuscada)
            ));
        };
    }


}
