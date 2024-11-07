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
