-- ============================================================
--  SpringEduManager — Datos de prueba
--  Ejecutar DESPUÉS de que Spring Boot haya creado las tablas
--  (ddl-auto=update genera las tablas al arrancar la app)
-- ============================================================

-- Limpia datos existentes respetando foreign keys
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE notas;
TRUNCATE TABLE inscripciones;
TRUNCATE TABLE estudiantes;
TRUNCATE TABLE cursos;
TRUNCATE TABLE usuarios;
SET FOREIGN_KEY_CHECKS = 1;

-- ── USUARIOS ─────────────────────────────────────────────────
-- Contraseña de todos los usuarios: 1234
-- Hash BCrypt de "1234": $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVyc2ULSNG

INSERT INTO usuarios (id, username, password, role, email, nombre) VALUES
(1, 'admin',          '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVyc2ULSNG', 'ADMIN', 'admin@springedu.com',   'Administrador'),
(2, 'ana.garcia',     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVyc2ULSNG', 'USER',  'ana@springedu.com',     'Ana García'),
(3, 'carlos.lopez',   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVyc2ULSNG', 'USER',  'carlos@springedu.com',  'Carlos López'),
(4, 'maria.perez',    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVyc2ULSNG', 'USER',  'maria@springedu.com',   'María Pérez');

-- ── CURSOS ───────────────────────────────────────────────────
INSERT INTO cursos (id, nombre) VALUES
(1, 'Matemáticas'),
(2, 'Programación Java'),
(3, 'Base de Datos'),
(4, 'Inglés Técnico');

-- ── ESTUDIANTES ──────────────────────────────────────────────
-- Vinculados a los usuarios con rol USER
INSERT INTO estudiantes (id, nombre, correo, usuario_id) VALUES
(1, 'Ana García',   'ana@springedu.com',    2),
(2, 'Carlos López', 'carlos@springedu.com', 3),
(3, 'María Pérez',  'maria@springedu.com',  4);

-- ── INSCRIPCIONES ────────────────────────────────────────────
INSERT INTO inscripciones (id, estudiante_id, curso_id, fecha_inscripcion) VALUES
(1, 1, 1, '2026-03-01'),  -- Ana en Matemáticas
(2, 1, 2, '2026-03-01'),  -- Ana en Programación Java
(3, 2, 2, '2026-03-05'),  -- Carlos en Programación Java
(4, 2, 3, '2026-03-05'),  -- Carlos en Base de Datos
(5, 3, 1, '2026-03-10'),  -- María en Matemáticas
(6, 3, 4, '2026-03-10');  -- María en Inglés Técnico

-- ── EVALUACIONES ─────────────────────────────────────────────
INSERT INTO notas (id, nombre, puntuacion, inscripcion_id) VALUES
-- Ana - Matemáticas
(1,  'Prueba 1',      85.0, 1),
(2,  'Prueba 2',      90.0, 1),
(3,  'Examen Final',  78.5, 1),
-- Ana - Programación Java
(4,  'Prueba 1',      95.0, 2),
(5,  'Proyecto',      88.0, 2),
-- Carlos - Programación Java
(6,  'Prueba 1',      70.0, 3),
(7,  'Proyecto',      65.5, 3),
-- Carlos - Base de Datos
(8,  'Prueba 1',      82.0, 4),
(9,  'Prueba 2',      45.0, 4),
(10, 'Examen Final',  60.0, 4),
-- María - Matemáticas
(11, 'Prueba 1',      55.0, 5),
(12, 'Prueba 2',      72.0, 5),
-- María - Inglés Técnico
(13, 'Oral 1',        91.0, 6),
(14, 'Written Test',  88.5, 6);

-- ── Reestablece AUTO_INCREMENT ────────────────────────────────
ALTER TABLE usuarios     AUTO_INCREMENT = 5;
ALTER TABLE cursos       AUTO_INCREMENT = 5;
ALTER TABLE estudiantes  AUTO_INCREMENT = 4;
ALTER TABLE inscripciones AUTO_INCREMENT = 7;
ALTER TABLE notas        AUTO_INCREMENT = 15;