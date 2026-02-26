CREATE DATABASE bd_clinica;
USE bd_clinica;

CREATE TABLE tb_tipo_sexo(
	id_sexo INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    sexo VARCHAR(50) NOT NULL,
    activo TINYINT(1) NOT NULL DEFAULT 1
);

CREATE TABLE tb_tipo_documento(
	id_tipo_documento INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    nombre_documento VARCHAR(50) NOT NULL,
    activo TINYINT(1) NOT NULL DEFAULT 1
);
CREATE TABLE tb_estado_civil(
	id_estado_civil INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    nombre_estado VARCHAR(50) NOT NULL,
    activo TINYINT(1) NOT NULL DEFAULT 1
);
CREATE TABLE tb_persona (
    id_persona INT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    correo VARCHAR(100) NOT NULL,
	activo TINYINT(1) NOT NULL DEFAULT 1
);

CREATE TABLE tb_paciente (
    id_paciente INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    id_persona INT NOT NULL,
    id_tipo_documento INT NOT NULL,
    numero_documento VARCHAR(20) NOT NULL UNIQUE,
    fecha_nacimiento DATE NOT NULL,
    fecha_registro DATE NOT NULL,
    id_sexo INT NOT NULL,
    id_estado_civil INT NOT NULL,
    activo TINYINT(1) NOT NULL DEFAULT 1,
    token_verificacion VARCHAR(255),
    token_expiracion DATETIME,
	FOREIGN KEY (id_persona) REFERENCES tb_persona(id_persona),
    FOREIGN KEY (id_tipo_documento) REFERENCES tb_tipo_documento(id_tipo_documento),
    FOREIGN KEY (id_sexo) REFERENCES tb_tipo_sexo(id_sexo),
    FOREIGN KEY (id_estado_civil) REFERENCES tb_estado_civil(id_estado_civil)
);

CREATE TABLE tb_especialidad(
	id_especialidad INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    activo TINYINT(1) NOT NULL DEFAULT 1
);

CREATE TABLE tb_medico (
    id_medico INT AUTO_INCREMENT PRIMARY KEY,
    id_persona INT NOT NULL,
    id_especialidad INT NOT NULL,
    numero_colegiatura VARCHAR(50) UNIQUE NOT NULL,
    telefono VARCHAR(20),
    fecha_registro DATE NOT NULL,
    activo TINYINT(1) NOT NULL DEFAULT 1,
	token_verificacion VARCHAR(255),
    token_expiracion DATETIME,
	FOREIGN KEY (id_persona) REFERENCES tb_persona(id_persona),
    FOREIGN KEY (id_especialidad) REFERENCES tb_especialidad(id_especialidad)
);
CREATE TABLE tb_estado_cita(
	id_estado_cita INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    nombre_estado VARCHAR(50),
    activo TINYINT(1) NOT NULL DEFAULT 1
);

CREATE TABLE tb_cita (
    id_cita INT AUTO_INCREMENT PRIMARY KEY,
    id_paciente INT NOT NULL,
    id_medico INT NOT NULL,
    fecha_programada DATE NOT NULL,
    hora TIME NOT NULL,
    id_estado_cita INT NOT NULL,
    motivo TEXT,
    activo TINYINT(1) NOT NULL DEFAULT 1,
    FOREIGN KEY (id_estado_cita) REFERENCES tb_estado_cita(id_estado_cita),
    FOREIGN KEY (id_paciente) REFERENCES tb_paciente(id_paciente),
    FOREIGN KEY (id_medico) REFERENCES tb_medico(id_medico)
);

CREATE TABLE tb_historial_medico (
    id_historial INT AUTO_INCREMENT PRIMARY KEY,
    id_cita INT NOT NULL,
    fecha_consulta DATE NOT NULL,
    diagnostico TEXT NOT NULL,
    tratamiento TEXT,
    observaciones TEXT,
    activo TINYINT(1) NOT NULL DEFAULT 1,
    FOREIGN KEY (id_cita) REFERENCES tb_cita(id_cita)
);

CREATE TABLE tb_usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    id_persona INT NOT NULL,
    correo VARCHAR(100) NOT NULL UNIQUE,
    passwd VARCHAR(250) NOT NULL,
    rol ENUM('ADMINISTRADOR','PACIENTE','MEDICO') NOT NULL,
    activo TINYINT(1) NOT NULL DEFAULT 1,
	FOREIGN KEY (id_persona) REFERENCES tb_persona(id_persona)
)