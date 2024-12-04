CREATE TABLE Persona (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo_documento VARCHAR(20) NOT NULL,
    numero_documento VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    direccion VARCHAR(150),
    telefono_fijo VARCHAR(15),
    celular_principal VARCHAR(15) NOT NULL,
    celular_secundario VARCHAR(15),
    ciudad_residencia VARCHAR(50),
    pais_residencia VARCHAR(50),
    ocupacion VARCHAR(50),
    telefono_empresa VARCHAR(15),
    fecha_nacimiento DATE
);

-- Crear la tabla habitaciones
CREATE TABLE habitaciones (
    id SERIAL PRIMARY KEY,            -- ID único para cada habitación
    numero_habitacion VARCHAR(10),    -- Número de habitación (por ejemplo, "101", "202")
    piso INT,                         -- Piso de la habitación
    tipo_habitacion VARCHAR(20),      -- Tipo de habitación (Familiar, Doble, Sencilla)
    costo_por_noche DECIMAL(10, 2),   -- Costo por noche por persona
    max_personas INT,                 -- Máximo de personas permitido
    min_personas INT,                 -- Mínimo de personas permitido
    ocupada BOOLEAN DEFAULT FALSE    -- Estado de ocupación (true = ocupada, false = libre)
);

-- Insertar las 64 habitaciones
INSERT INTO habitaciones (numero_habitacion, piso, tipo_habitacion, costo_por_noche, max_personas, min_personas)
VALUES
-- Piso 1: Habitaciones familiares
('101', 1, 'Familiar', 80000, 6, 3),
('102', 1, 'Familiar', 80000, 6, 3),
('103', 1, 'Familiar', 80000, 6, 3),
('104', 1, 'Familiar', 80000, 6, 3),
('105', 1, 'Familiar', 80000, 6, 3),
('106', 1, 'Familiar', 80000, 6, 3),
('107', 1, 'Familiar', 80000, 6, 3),
('108', 1, 'Familiar', 80000, 6, 3),
('109', 1, 'Familiar', 80000, 6, 3),
('110', 1, 'Familiar', 80000, 6, 3),
('111', 1, 'Familiar', 80000, 6, 3),
('112', 1, 'Familiar', 80000, 6, 3),
('113', 1, 'Familiar', 80000, 6, 3),
('114', 1, 'Familiar', 80000, 6, 3),
('115', 1, 'Familiar', 80000, 6, 3),
('116', 1, 'Familiar', 80000, 6, 3),
-- Piso 2: Habitaciones dobles
('201', 2, 'Doble', 100000, 3, 1),
('202', 2, 'Doble', 100000, 3, 1),
('203', 2, 'Doble', 100000, 3, 1),
('204', 2, 'Doble', 100000, 3, 1),
('205', 2, 'Doble', 100000, 3, 1),
('206', 2, 'Doble', 100000, 3, 1),
('207', 2, 'Doble', 100000, 3, 1),
('208', 2, 'Doble', 100000, 3, 1),
('209', 2, 'Doble', 100000, 3, 1),
('210', 2, 'Doble', 100000, 3, 1),
('211', 2, 'Doble', 100000, 3, 1),
('212', 2, 'Doble', 100000, 3, 1),
('213', 2, 'Doble', 100000, 3, 1),
('214', 2, 'Doble', 100000, 3, 1),
('215', 2, 'Doble', 100000, 3, 1),
('216', 2, 'Doble', 100000, 3, 1),
-- Piso 3: Habitaciones dobles
('301', 3, 'Doble', 100000, 3, 1),
('302', 3, 'Doble', 100000, 3, 1),
('303', 3, 'Doble', 100000, 3, 1),
('304', 3, 'Doble', 100000, 3, 1),
('305', 3, 'Doble', 100000, 3, 1),
('306', 3, 'Doble', 100000, 3, 1),
('307', 3, 'Doble', 100000, 3, 1),
('308', 3, 'Doble', 100000, 3, 1),
('309', 3, 'Doble', 100000, 3, 1),
('310', 3, 'Doble', 100000, 3, 1),
('311', 3, 'Doble', 100000, 3, 1),
('312', 3, 'Doble', 100000, 3, 1),
('313', 3, 'Doble', 100000, 3, 1),
('314', 3, 'Doble', 100000, 3, 1),
('315', 3, 'Doble', 100000, 3, 1),
('316', 3, 'Doble', 100000, 3, 1),
-- Piso 4: Habitaciones sencillas
('401', 4, 'Sencilla', 120000, 2, 1),
('402', 4, 'Sencilla', 120000, 2, 1),
('403', 4, 'Sencilla', 120000, 2, 1),
('404', 4, 'Sencilla', 120000, 2, 1),
('405', 4, 'Sencilla', 120000, 2, 1),
('406', 4, 'Sencilla', 120000, 2, 1),
('407', 4, 'Sencilla', 120000, 2, 1),
('408', 4, 'Sencilla', 120000, 2, 1),
('409', 4, 'Sencilla', 120000, 2, 1),
('410', 4, 'Sencilla', 120000, 2, 1),
('411', 4, 'Sencilla', 120000, 2, 1),
('412', 4, 'Sencilla', 120000, 2, 1),
('413', 4, 'Sencilla', 120000, 2, 1),
('414', 4, 'Sencilla', 120000, 2, 1),
('415', 4, 'Sencilla', 120000, 2, 1),
('416', 4, 'Sencilla', 120000, 2, 1);


SELECT id, piso, numero_habitacion, ocupada FROM habitaciones WHERE ocupada = true;


