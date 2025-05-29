CREATE DATABASE IF NOT EXISTS biblioteca_pbarriendos_bibliosmart CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE biblioteca_pbarriendos_bibliosmart;

CREATE TABLE IF NOT EXISTS autores (
    id_autor INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(255),
    apellidos VARCHAR(255),
    fecha_nacimiento DATE,
    nacionalidad VARCHAR(255),
    biografia TEXT,
    foto LONGBLOB
    );

-- Tabla usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario INT PRIMARY KEY AUTO_INCREMENT,
    nombre_usuario VARCHAR(255) NOT NULL,
    correo VARCHAR(255),
    password VARCHAR(255),
    tipo_usuario ENUM('ADMIN', 'EDITOR', 'CONSULTA'),
    nombre_completo VARCHAR(255),
    fecha_registro DATE,
    direccion VARCHAR(255),
    telefono VARCHAR(50)
    );

CREATE TABLE IF NOT EXISTS libros (
    id_libro INT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(255),
    descripcion TEXT,
    fecha_publicacion DATE,
    portada LONGBLOB,
    archivo_pdf LONGBLOB,
    isbn VARCHAR(50),
    id_autor INT,
    nombre_archivo_pdf VARCHAR(255),
    CONSTRAINT fk_libro_autor FOREIGN KEY (id_autor) REFERENCES autores(id_autor)
    );

CREATE TABLE IF NOT EXISTS prestamos (
    id_prestamo INT PRIMARY KEY AUTO_INCREMENT,
    fecha_inicio DATE,
    fecha_fin DATE,
    fecha_devolucion_real DATE,
    estado ENUM('ACTIVO', 'FINALIZADO'),
    observaciones TEXT,
    id_usuario INT,
    id_libro INT,
    CONSTRAINT fk_prestamo_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
    CONSTRAINT fk_prestamo_libro FOREIGN KEY (id_libro) REFERENCES libros(id_libro)
    );

INSERT INTO usuarios (nombre_usuario, correo, password, tipo_usuario, nombre_completo, fecha_registro, direccion, telefono)
SELECT * FROM (
                  SELECT 'maria_garcia', 'maria.garcia@hotmail.com', '$2a$12$w89ZGyIv6PpbZ/j4jLhuF.CC6Ewm.gyf35ch6Lca4F5Xe6mMXTZ1y', 'CONSULTA', 'María García López', CURDATE(), 'Calle Mayor 12, Madrid', '610123456' UNION ALL
                  SELECT 'juanmartinez', 'juan.martinez@gmail.com', '$2a$12$w89ZGyIv6PpbZ/j4jLhuF.CC6Ewm.gyf35ch6Lca4F5Xe6mMXTZ1y', 'EDITOR', 'Juan Martínez Sánchez', CURDATE(), 'Av. de Andalucía 45, Sevilla', '620234567' UNION ALL
                  SELECT 'ana_fernandez', 'ana.fernandez@gmail.com', '$2a$12$w89ZGyIv6PpbZ/j4jLhuF.CC6Ewm.gyf35ch6Lca4F5Xe6mMXTZ1y', 'CONSULTA', 'Ana Fernández Ruiz', CURDATE(), 'Calle Real 7, Valencia', '630345678' UNION ALL
                  SELECT 'carlosjimenez', 'carlos.jimenez@gmail.com', '$2a$12$w89ZGyIv6PpbZ/j4jLhuF.CC6Ewm.gyf35ch6Lca4F5Xe6mMXTZ1y', 'EDITOR', 'Carlos Jiménez Gómez', CURDATE(), 'Paseo del Prado 10, Madrid', '640456789' UNION ALL
                  SELECT 'laura_lopez', 'laura.lopez@gmail.com', '$2a$12$w89ZGyIv6PpbZ/j4jLhuF.CC6Ewm.gyf35ch6Lca4F5Xe6mMXTZ1y', 'CONSULTA', 'Laura López Martín', CURDATE(), 'Calle de Alcalá 23, Madrid', '650567890' UNION ALL
                  SELECT 'pablomoreno', 'pablo.moreno@hotmail.com', '$2a$12$w89ZGyIv6PpbZ/j4jLhuF.CC6Ewm.gyf35ch6Lca4F5Xe6mMXTZ1y', 'CONSULTA', 'Pablo Moreno Díaz', CURDATE(), 'Calle Cervantes 5, Barcelona', '660678901' UNION ALL
                  SELECT 'sara_gonzalez', 'sara.gonzalez@gmail.com', '$2a$12$w89ZGyIv6PpbZ/j4jLhuF.CC6Ewm.gyf35ch6Lca4F5Xe6mMXTZ1y', 'EDITOR', 'Sara González Fernández', CURDATE(), 'Plaza España 9, Zaragoza', '670789012' UNION ALL
                  SELECT 'davidruiz', 'david.ruiz@hotmail.com', '$2a$12$w89ZGyIv6PpbZ/j4jLhuF.CC6Ewm.gyf35ch6Lca4F5Xe6mMXTZ1y', 'CONSULTA', 'David Ruiz Sánchez', CURDATE(), 'Calle Luna 15, Málaga', '680890123' UNION ALL
                  SELECT 'martaalonso', 'marta.alonso@hotmail.com', '$2a$12$w89ZGyIv6PpbZ/j4jLhuF.CC6Ewm.gyf35ch6Lca4F5Xe6mMXTZ1y', 'CONSULTA', 'Marta Alonso Torres', CURDATE(), 'Av. del Puerto 4, Bilbao', '690901234' UNION ALL
                  SELECT 'jorge_martin', 'jorge.martin@gmail.com', '$2a$12$w89ZGyIv6PpbZ/j4jLhuF.CC6Ewm.gyf35ch6Lca4F5Xe6mMXTZ1y', 'EDITOR', 'Jorge Martín Ortega', CURDATE(), 'Calle Sol 11, Valladolid', '601012345' UNION ALL
                  SELECT 'elena_vazquez', 'elena.vazquez@hotmail.com', '$2a$12$w89ZGyIv6PpbZ/j4jLhuF.CC6Ewm.gyf35ch6Lca4F5Xe6mMXTZ1y', 'CONSULTA', 'Elena Vázquez Ruiz', CURDATE(), 'Calle Flores 22, Granada', '602123456' UNION ALL
                  SELECT 'raul_diaz', 'raul.diaz@gmail.com', '$2a$12$w89ZGyIv6PpbZ/j4jLhuF.CC6Ewm.gyf35ch6Lca4F5Xe6mMXTZ1y', 'CONSULTA', 'Raúl Díaz Jiménez', CURDATE(), 'Av. Libertad 8, Alicante', '603234567' UNION ALL
                  SELECT 'admin', 'admin@biblioteca.local', '$2a$12$gNKMaoS3r8sz/MctMYDoje0fdX6e4vn5xdmssoZlNHGkdW62iqnoG', 'ADMIN', 'Administrador General', CURDATE(), 'Sede Central, Madrid', '600000000'
              ) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM usuarios);

INSERT INTO autores (nombre, apellidos, fecha_nacimiento, nacionalidad, biografia)
SELECT * FROM (
                  SELECT 'Miguel', 'de Cervantes Saavedra', '1547-09-29', 'Española', 'Autor de Don Quijote de la Mancha, considerada la primera novela moderna.' UNION ALL
                  SELECT 'Federico', 'García Lorca', '1898-06-05', 'Española', 'Poeta y dramaturgo destacado de la Generación del 27.' UNION ALL
                  SELECT 'Gabriel', 'García Márquez', '1927-03-06', 'Colombiana', 'Escritor y periodista, premio Nobel de Literatura, autor de Cien años de soledad.' UNION ALL
                  SELECT 'Isabel', 'Allende', '1942-08-02', 'Chilena', 'Reconocida escritora de novelas históricas y realismo mágico.' UNION ALL
                  SELECT 'Camilo', 'José Cela', '1916-05-11', 'Española', 'Premio Nobel de Literatura, autor de La colmena.' UNION ALL
                  SELECT 'Antonio', 'Muñoz Molina', '1956-01-10', 'Española', 'Novelista y ensayista contemporáneo español.' UNION ALL
                  SELECT 'Jorge Luis', 'Borges', '1899-08-24', 'Argentina', 'Poeta, ensayista y cuentista argentino de gran influencia literaria.' UNION ALL
                  SELECT 'Rosalía', 'de Castro', '1837-02-24', 'Española', 'Poeta y novelista, una de las figuras más importantes del Romanticismo español.' UNION ALL
                  SELECT 'William', 'Shakespeare', '1564-04-23', 'Inglesa', 'Dramaturgo, poeta y actor inglés, considerado el escritor más importante en lengua inglesa.'
              ) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM autores);

INSERT INTO libros (titulo, descripcion, fecha_publicacion, isbn, id_autor)
SELECT * FROM (
                  -- Miguel de Cervantes
                  SELECT 'Don Quijote de la Mancha',
                         'Novela clásica que narra las aventuras del ingenioso hidalgo Don Quijote.',
                         '1605-01-16',
                         '978-1-01-000001-1',
                         (SELECT id_autor FROM autores WHERE nombre='Miguel' AND apellidos='de Cervantes Saavedra')
                  UNION ALL
                  SELECT 'La Galatea',
                         'Primera novela pastoral de Cervantes.',
                         '1585-01-01',
                         '978-1-01-000002-2',
                         (SELECT id_autor FROM autores WHERE nombre='Miguel' AND apellidos='de Cervantes Saavedra')
                  UNION ALL
                  SELECT 'Novelas Ejemplares',
                         'Colección de doce novelas cortas.',
                         '1613-01-01',
                         '978-1-01-000003-3',
                         (SELECT id_autor FROM autores WHERE nombre='Miguel' AND apellidos='de Cervantes Saavedra')

                         -- Federico García Lorca
                  UNION ALL
                  SELECT 'Bodas de Sangre',
                         'Tragedia dramática que explora el amor y la pasión en un contexto rural andaluz.',
                         '1933-01-01',
                         '978-1-01-000004-4',
                         (SELECT id_autor FROM autores WHERE nombre='Federico' AND apellidos='García Lorca')
                  UNION ALL
                  SELECT 'La Casa de Bernarda Alba',
                         'Drama sobre la opresión social y familiar.',
                         '1936-01-01',
                         '978-1-01-000005-5',
                         (SELECT id_autor FROM autores WHERE nombre='Federico' AND apellidos='García Lorca')
                  UNION ALL
                  SELECT 'Poeta en Nueva York',
                         'Poemario que refleja su estancia en Nueva York y su visión social.',
                         '1940-01-01',
                         '978-1-01-000006-6',
                         (SELECT id_autor FROM autores WHERE nombre='Federico' AND apellidos='García Lorca')

                         -- Gabriel García Márquez
                  UNION ALL
                  SELECT 'Cien años de soledad',
                         'Novela emblemática del realismo mágico, relata la historia de la familia Buendía.',
                         '1967-05-30',
                         '978-1-01-000007-7',
                         (SELECT id_autor FROM autores WHERE nombre='Gabriel' AND apellidos='García Márquez')
                  UNION ALL
                  SELECT 'El amor en los tiempos del cólera',
                         'Historia de amor que trasciende el tiempo y las dificultades.',
                         '1985-01-01',
                         '978-1-01-000008-8',
                         (SELECT id_autor FROM autores WHERE nombre='Gabriel' AND apellidos='García Márquez')
                  UNION ALL
                  SELECT 'Crónica de una muerte anunciada',
                         'Novela corta que investiga el asesinato de Santiago Nasar.',
                         '1981-01-01',
                         '978-1-01-000009-9',
                         (SELECT id_autor FROM autores WHERE nombre='Gabriel' AND apellidos='García Márquez')

                         -- Isabel Allende
                  UNION ALL
                  SELECT 'La casa de los espíritus',
                         'Novela que mezcla la historia familiar con elementos mágicos y políticos.',
                         '1982-01-01',
                         '978-1-01-000010-1',
                         (SELECT id_autor FROM autores WHERE nombre='Isabel' AND apellidos='Allende')
                  UNION ALL
                  SELECT 'Eva Luna',
                         'Relatos de una mujer con una vida marcada por la imaginación y la rebeldía.',
                         '1987-01-01',
                         '978-1-01-000011-2',
                         (SELECT id_autor FROM autores WHERE nombre='Isabel' AND apellidos='Allende')
                  UNION ALL
                  SELECT 'Paula',
                         'Memorias dedicadas a su hija con reflexiones personales.',
                         '1994-01-01',
                         '978-1-01-000012-3',
                         (SELECT id_autor FROM autores WHERE nombre='Isabel' AND apellidos='Allende')

                         -- Camilo José Cela
                  UNION ALL
                  SELECT 'La colmena',
                         'Novela que retrata la sociedad española de la posguerra.',
                         '1951-01-01',
                         '978-1-01-000013-4',
                         (SELECT id_autor FROM autores WHERE nombre='Camilo' AND apellidos='José Cela')
                  UNION ALL
                  SELECT 'La familia de Pascual Duarte',
                         'Novela que explora la violencia y la fatalidad en la España rural.',
                         '1942-01-01',
                         '978-1-01-000014-5',
                         (SELECT id_autor FROM autores WHERE nombre='Camilo' AND apellidos='José Cela')

                         -- Antonio Muñoz Molina
                  UNION ALL
                  SELECT 'El jinete polaco',
                         'Novela que trata temas de memoria histórica y pérdida.',
                         '1991-01-01',
                         '978-1-01-000015-6',
                         (SELECT id_autor FROM autores WHERE nombre='Antonio' AND apellidos='Muñoz Molina')
                  UNION ALL
                  SELECT 'Sefarad',
                         'Narrativa sobre la historia de la expulsión de los judíos en España.',
                         '2001-01-01',
                         '978-1-01-000016-7',
                         (SELECT id_autor FROM autores WHERE nombre='Antonio' AND apellidos='Muñoz Molina')

                         -- Jorge Luis Borges
                  UNION ALL
                  SELECT 'Ficciones',
                         'Colección de cuentos que exploran la realidad y la ficción.',
                         '1944-01-01',
                         '978-1-01-000017-8',
                         (SELECT id_autor FROM autores WHERE nombre='Jorge Luis' AND apellidos='Borges')
                  UNION ALL
                  SELECT 'El Aleph',
                         'Cuentos que exploran la infinitud y el tiempo.',
                         '1949-01-01',
                         '978-1-01-000018-9',
                         (SELECT id_autor FROM autores WHERE nombre='Jorge Luis' AND apellidos='Borges')

                         -- Rosalía de Castro
                  UNION ALL
                  SELECT 'Cantares Gallegos',
                         'Poemario que recoge la cultura y tradición gallega.',
                         '1863-01-01',
                         '978-1-01-000019-0',
                         (SELECT id_autor FROM autores WHERE nombre='Rosalía' AND apellidos='de Castro')
                  UNION ALL
                  SELECT 'Follas Novas',
                         'Segundo poemario que sigue el estilo del primero, con temas sociales.',
                         '1880-01-01',
                         '978-1-01-000020-1',
                         (SELECT id_autor FROM autores WHERE nombre='Rosalía' AND apellidos='de Castro')

                         -- William Shakespeare
                  UNION ALL
                  SELECT 'Hamlet',
                         'Tragedia sobre la venganza, el poder y la locura del príncipe Hamlet.',
                         '1603-01-01',
                         '978-1-01-000021-2',
                         (SELECT id_autor FROM autores WHERE nombre='William' AND apellidos='Shakespeare')
                  UNION ALL
                  SELECT 'Romeo y Julieta',
                         'Historia trágica de amor entre dos jóvenes de familias enemistadas.',
                         '1597-01-01',
                         '978-1-01-000022-3',
                         (SELECT id_autor FROM autores WHERE nombre='William' AND apellidos='Shakespeare')
                  UNION ALL
                  SELECT 'Macbeth',
                         'Tragedia sobre la ambición desmedida y la culpa.',
                         '1606-01-01',
                         '978-1-01-000023-4',
                         (SELECT id_autor FROM autores WHERE nombre='William' AND apellidos='Shakespeare')
              ) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM libros);


INSERT INTO prestamos (fecha_inicio, fecha_fin, fecha_devolucion_real, estado, observaciones, id_usuario, id_libro)
SELECT * FROM (
                  -- Usuario 1: 1 préstamo activo
                  SELECT '2025-04-01', '2025-04-15', NULL, 'ACTIVO', 'Préstamo inicial',
                         (SELECT id_usuario FROM usuarios WHERE nombre_usuario = 'sara_gonzalez'),
                         (SELECT id_libro FROM libros WHERE titulo = 'Don Quijote de la Mancha')
                  UNION ALL
                  -- Usuario 2: 3 préstamos, 2 finalizados y 1 activo
                  SELECT '2025-03-01', '2025-03-15', '2025-03-14', 'FINALIZADO', 'Devuelto a tiempo',
                         (SELECT id_usuario FROM usuarios WHERE nombre_usuario = 'raul_diaz'),
                         (SELECT id_libro FROM libros WHERE titulo = 'La casa de los espíritus')
                  UNION ALL
                  SELECT '2025-03-20', '2025-04-05', '2025-04-06', 'FINALIZADO', 'Un día de retraso',
                         (SELECT id_usuario FROM usuarios WHERE nombre_usuario = 'sara_gonzalez'),
                         (SELECT id_libro FROM libros WHERE titulo = 'Cien años de soledad')
                  UNION ALL
                  SELECT '2025-04-10', '2025-04-25', NULL, 'ACTIVO', 'Préstamo reciente',
                         (SELECT id_usuario FROM usuarios WHERE nombre_usuario = 'maria_garcia'),
                         (SELECT id_libro FROM libros WHERE titulo = 'Hamlet')
                  UNION ALL
                  -- Usuario 3: 5 préstamos, variados
                  SELECT '2025-01-05', '2025-01-20', '2025-01-19', 'FINALIZADO', 'Devuelto temprano',
                         (SELECT id_usuario FROM usuarios WHERE nombre_usuario = 'pablomoreno'),
                         (SELECT id_libro FROM libros WHERE titulo = 'Bodas de Sangre')
                  UNION ALL
                  SELECT '2025-02-01', '2025-02-15', '2025-02-15', 'FINALIZADO', 'A tiempo',
                         (SELECT id_usuario FROM usuarios WHERE nombre_usuario = 'raul_diaz'),
                         (SELECT id_libro FROM libros WHERE titulo = 'El amor en los tiempos del cólera')
                  UNION ALL
                  SELECT '2025-02-20', '2025-03-05', '2025-03-04', 'FINALIZADO', 'Sin problemas',
                         (SELECT id_usuario FROM usuarios WHERE nombre_usuario = 'raul_diaz'),
                         (SELECT id_libro FROM libros WHERE titulo = 'Novelas Ejemplares')
                  UNION ALL
                  SELECT '2025-03-15', '2025-03-30', '2025-03-29', 'FINALIZADO', 'Devuelto justo a tiempo',
                         (SELECT id_usuario FROM usuarios WHERE nombre_usuario = 'pablomoreno'),
                         (SELECT id_libro FROM libros WHERE titulo = 'La colmena')
                  UNION ALL
                  SELECT '2025-04-01', '2025-04-15', NULL, 'ACTIVO', 'Préstamo actual',
                         (SELECT id_usuario FROM usuarios WHERE nombre_usuario = 'raul_diaz'),
                         (SELECT id_libro FROM libros WHERE titulo = 'Ficciones')
                  UNION ALL
                  -- Usuario 4: 2 préstamos, 1 activo y 1 finalizado
                  SELECT '2025-04-05', '2025-04-20', NULL, 'ACTIVO', 'Préstamo activo',
                         (SELECT id_usuario FROM usuarios WHERE nombre_usuario = 'martaalonso'),
                         (SELECT id_libro FROM libros WHERE titulo = 'Macbeth')
                  UNION ALL
                  SELECT '2025-02-10', '2025-02-25', '2025-02-24', 'FINALIZADO', 'Devuelto antes',
                         (SELECT id_usuario FROM usuarios WHERE nombre_usuario = 'martaalonso'),
                         (SELECT id_libro FROM libros WHERE titulo = 'Follas Novas')
                  UNION ALL
                  -- Usuario 5: 1 préstamo finalizado
                  SELECT '2025-03-01', '2025-03-15', '2025-03-14', 'FINALIZADO', 'Sin incidencias',
                         (SELECT id_usuario FROM usuarios WHERE nombre_usuario = 'jorge_martin'),
                         (SELECT id_libro FROM libros WHERE titulo = 'Paula')
              ) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM prestamos);
