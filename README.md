# Librería — proyecto de ejemplo del Tema 1 (Acceso a Datos)

Proyecto de la teoría (*Fundamentos de Spring Boot* y *Conectores y protocolos*).
Llega exactamente al mismo punto que la Actividad 1.1 de GameVault, con estas equivalencias:

| Librería (teoría)             | GameVault (Actividad 1.1)       |
| ----------------------------- | ------------------------------- |
| `LibreriaApplication`         | `GamevaultApplication`          |
| `Editorial`                   | `Estudio`                       |
| `Libro` (`fechaPublicacion`)  | `Videojuego` (`fechaLanzamiento`) |
| `libreria_db` / `libreria_user` | `gamevault_db` / `gamevault_user` |

Además incluye `FichaTecnica` (1:1) y `Autor` (N:M), los ejemplos de tipos de relación
de la teoría, que no forman parte de la actividad.

Stack: Spring Boot 4.1.1 · Java 21 · Maven · PostgreSQL 18 (Dev Container).
Dependencias: Spring Web (`webmvc`), Spring Data JPA, PostgreSQL Driver, Lombok.

## Arrancar

1. Abrir la carpeta en VS Code → **Reopen in Container** (o en IntelliJ: `devcontainer.json` → *Create Dev Container and Mount Sources…*).
2. Comprobar el entorno: `java -version`, `mvn -v`, `git --version`, `docker version`.
3. Arrancar con el perfil `dev`:

   ```bash
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
   ```

   (o *Run and Debug* → **LibreriaApplication (dev)** en VS Code).

En consola deben aparecer los `create table` de `editorial`, `libro`, `ficha_tecnica`, `autor` y `libro_autor`.

## Verificar en PostgreSQL

```bash
docker compose ls                                            # nombre del proyecto → <proyecto>
docker compose -f .devcontainer/docker-compose.yml -p <proyecto> ps   # nombre del contenedor postgres
docker exec -it <contenedor-postgres> psql -U libreria_user -d libreria_db -c "\d libro"
```

Qué enseñar:

- `libro.editorial_id` es la FK hacia `editorial`; `editorial` no tiene ninguna columna hacia `libro` (→ `mappedBy`).
- `precio` es `numeric`, `fecha_publicacion` es `date`.
- `\d ficha_tecnica`: `libro_id` con restricción `UNIQUE` (eso es lo que la hace 1:1).
- `\d libro_autor`: tabla intermedia con dos FK (N:M).

Desde fuera del contenedor (pgAdmin/DBeaver): `localhost:5432`, `libreria_user` / `password123`.
