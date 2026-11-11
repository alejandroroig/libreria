TRUNCATE TABLE libro, editorial RESTART IDENTITY CASCADE;

INSERT INTO editorial (nombre, pais) VALUES
                                         ('Planeta', 'España'),
                                         ('Anagrama', 'España'),
                                         ('Penguin Random House', 'España'),
                                         ('Alfaguara', 'España');

INSERT INTO libro (titulo, precio, fecha_publicacion, editorial_id, detalles_edicion) VALUES
                                                                                          ('El nombre del viento', 19.95, '2007-03-27', 1,
                                                                                           '{"tapa": "dura", "ebook": {"formato": "EPUB"}}'::jsonb),
                                                                                          ('El temor de un hombre sabio', 22.50, '2011-03-01', 1,
                                                                                           '{"tapa": "blanda", "ebook": {"formato": "EPUB"}}'::jsonb),
                                                                                          ('1984', 12.90, '1949-06-08', 2,
                                                                                           '{"tapa": "blanda", "idioma_original": "inglés", "ebook": {"formato": "EPUB"}}'::jsonb),
                                                                                          ('Rebelión en la granja', 10.50, '1945-08-17', 2,
                                                                                           '{"tapa": "blanda", "idioma_original": "inglés"}'::jsonb),
                                                                                          ('Crónica de una muerte anunciada', 11.95, '1981-01-01', 3,
                                                                                           '{"tapa": "blanda", "ebook": {"formato": "PDF"}}'::jsonb),
                                                                                          ('Cien años de soledad', 18.75, '1967-05-30', 3,
                                                                                           '{"tapa": "dura", "ebook": {"formato": "EPUB"}}'::jsonb),
                                                                                          ('Rayuela', 16.40, '1963-06-28', 4,
                                                                                           '{"tapa": "blanda", "ebook": null}'::jsonb),
                                                                                          ('La sombra del viento', 20.90, '2001-04-12', 1,
                                                                                           '{"tapa": "dura", "audiolibro": {"formato": "MP3", "duracion_min": 840}}'::jsonb),
                                                                                          ('Patria', 21.50, '2016-09-06', 2,
                                                                                           '{"tapa": "dura", "ebook": {"formato": "EPUB"}}'::jsonb),
                                                                                          ('El infinito en un junco', 17.95, '2019-09-18', 3,
                                                                                           '{"tapa": "blanda", "ebook": {"formato": "EPUB"}, "audiolibro": {"formato": "MP3"}}'::jsonb),
                                                                                          ('Nada', 13.50, '1945-01-01', 4, '{}'::jsonb),
                                                                                          ('La colmena', 14.95, '1951-01-01', 4, NULL);