use bd_clinica;
-- para mi usuario administrador
INSERT INTO tb_persona (nombres,apellidos,correo,activo)
VALUES ('Stefano','Gonzales','stefano@gmail.com',1);
-- contraseña es Stefano
INSERT INTO tb_usuario(id_persona,correo,passwd,rol,activo)
VALUES (1, 'stefano@gmail.com', '$2a$12$WnWvkKFInzkprf5MsNA2r.bAMPrdvJTggCZ1VCAUvTujkhdGEiuY2', 'ADMINISTRADOR',1);


INSERT INTO tb_tipo_sexo (sexo, activo) VALUES
('Masculino', 1),
('Femenino', 1);

INSERT INTO tb_tipo_documento (nombre_documento, activo) VALUES
('DNI', 1),
('Pasaporte', 1),
('Carnet de Extranjería', 1),
('RUC', 1);

INSERT INTO tb_estado_civil (nombre_estado, activo) VALUES
('Soltero(a)', 1),
('Casado(a)', 1),
('Divorciado(a)', 1),
('Viudo(a)', 1),
('Conviviente', 1);

INSERT INTO tb_especialidad (nombre, activo) VALUES
('Medicina General', 1),
('Pediatría', 1),
('Ginecología', 1),
('Cardiología', 1),
('Dermatología', 1),
('Neurología', 1),
('Traumatología', 1);

INSERT INTO tb_estado_cita ( nombre_estado, activo) VALUES
( 'Pendiente', 1),
( 'Atendida', 1),
( 'Cancelada', 1),
( 'Reprogramada', 1);
-- Paciente
INSERT INTO tb_persona (nombres, apellidos, correo, activo) VALUES
('Juan', 'Pérez Gómez', 'juan1@gmail.com', 1),
('María', 'Lopez Torres', 'maria2@gmail.com', 1),
('Carlos', 'Ramirez Silva', 'carlos3@gmail.com', 1),
('Ana', 'Fernandez Ruiz', 'ana4@gmail.com', 1),
('Luis', 'Castro Medina', 'luis5@gmail.com', 1),
('Rosa', 'Gonzales Vargas', 'rosa6@gmail.com', 1),
('Pedro', 'Salazar Rios', 'pedro7@gmail.com', 1),
('Lucía', 'Morales Peña', 'lucia8@gmail.com', 1),
('Diego', 'Herrera Soto', 'diego9@gmail.com', 1),
('Valeria', 'Mendoza Cruz', 'valeria10@gmail.com', 1);

INSERT INTO tb_paciente (
    id_persona,
    id_tipo_documento,
    numero_documento,
    fecha_nacimiento,
    fecha_registro,
    id_sexo,
    id_estado_civil,
    activo,
    token_verificacion,
    token_expiracion
) VALUES
(1, 1, '70000001', '1995-01-10', CURDATE(), 1, 1, 1, NULL, NULL),
(2, 1, '70000002', '1996-02-15', CURDATE(), 2, 2, 1, NULL, NULL),
(3, 1, '70000003', '1994-03-20', CURDATE(), 1, 1, 1, NULL, NULL),
(4, 1, '70000004', '1998-04-25', CURDATE(), 2, 1, 1, NULL, NULL),
(5, 1, '70000005', '1992-05-05', CURDATE(), 1, 2, 1, NULL, NULL),
(6, 1, '70000006', '1999-06-12', CURDATE(), 2, 1, 1, NULL, NULL),
(7, 1, '70000007', '1993-07-18', CURDATE(), 1, 1, 1, NULL, NULL),
(8, 1, '70000008', '1997-08-22', CURDATE(), 2, 2, 1, NULL, NULL),
(9, 1, '70000009', '1991-09-30', CURDATE(), 1, 1, 1, NULL, NULL),
(10,1, '70000010', '2000-10-11', CURDATE(), 2, 1, 1, NULL, NULL);

-- passwd : Paciente
INSERT INTO tb_usuario (
    id_persona,
    correo,
    passwd,
    rol,
    activo
) VALUES
(1, 'juan1@gmail.com', '$2a$10$Q9vTzJH3Q1x7vG8uK1wO3eFjK7HnLwR9YzVb2XcPq4Rt6Yu8MiN2W', 'PACIENTE', 1),
(2, 'maria2@gmail.com', '$2a$10$Q9vTzJH3Q1x7vG8uK1wO3eFjK7HnLwR9YzVb2XcPq4Rt6Yu8MiN2W', 'PACIENTE', 1),
(3, 'carlos3@gmail.com', '$2a$10$Q9vTzJH3Q1x7vG8uK1wO3eFjK7HnLwR9YzVb2XcPq4Rt6Yu8MiN2W', 'PACIENTE', 1),
(4, 'ana4@gmail.com', '$2a$10$Q9vTzJH3Q1x7vG8uK1wO3eFjK7HnLwR9YzVb2XcPq4Rt6Yu8MiN2W', 'PACIENTE', 1),
(5, 'luis5@gmail.com', '$2a$10$Q9vTzJH3Q1x7vG8uK1wO3eFjK7HnLwR9YzVb2XcPq4Rt6Yu8MiN2W', 'PACIENTE', 1),
(6, 'rosa6@gmail.com', '$2a$10$Q9vTzJH3Q1x7vG8uK1wO3eFjK7HnLwR9YzVb2XcPq4Rt6Yu8MiN2W', 'PACIENTE', 1),
(7, 'pedro7@gmail.com', '$2a$10$Q9vTzJH3Q1x7vG8uK1wO3eFjK7HnLwR9YzVb2XcPq4Rt6Yu8MiN2W', 'PACIENTE', 1),
(8, 'lucia8@gmail.com', '$2a$10$Q9vTzJH3Q1x7vG8uK1wO3eFjK7HnLwR9YzVb2XcPq4Rt6Yu8MiN2W', 'PACIENTE', 1),
(9, 'diego9@gmail.com', '$2a$10$Q9vTzJH3Q1x7vG8uK1wO3eFjK7HnLwR9YzVb2XcPq4Rt6Yu8MiN2W', 'PACIENTE', 1),
(10,'valeria10@gmail.com', '$2a$10$Q9vTzJH3Q1x7vG8uK1wO3eFjK7HnLwR9YzVb2XcPq4Rt6Yu8MiN2W', 'PACIENTE', 1);



-- Medico

INSERT INTO tb_persona (nombres, apellidos, correo, activo) VALUES
('Roberto', 'Sánchez Molina', 'medico1@gmail.com', 1),
('Elena', 'Torres Díaz', 'medico2@gmail.com', 1),
('Miguel', 'Rojas Castro', 'medico3@gmail.com', 1),
('Patricia', 'Vargas León', 'medico4@gmail.com', 1),
('Fernando', 'Paredes Soto', 'medico5@gmail.com', 1),
('Claudia', 'Jiménez Ruiz', 'medico6@gmail.com', 1),
('Jorge', 'Navarro Silva', 'medico7@gmail.com', 1),
('Andrea', 'Luna Morales', 'medico8@gmail.com', 1),
('Ricardo', 'Mendoza Pérez', 'medico9@gmail.com', 1),
('Gabriela', 'Flores Herrera', 'medico10@gmail.com', 1);


INSERT INTO tb_medico (
    id_persona,
    id_especialidad,
    numero_colegiatura,
    telefono,
    fecha_registro,
    activo,
    token_verificacion,
    token_expiracion
) VALUES
(11, 1, 'CMP10001', '999111001', CURDATE(), 1, NULL, NULL),
(12, 2, 'CMP10002', '999111002', CURDATE(), 1, NULL, NULL),
(13, 3, 'CMP10003', '999111003', CURDATE(), 1, NULL, NULL),
(14, 4, 'CMP10004', '999111004', CURDATE(), 1, NULL, NULL),
(15, 5, 'CMP10005', '999111005', CURDATE(), 1, NULL, NULL),
(16, 6, 'CMP10006', '999111006', CURDATE(), 1, NULL, NULL),
(17, 7, 'CMP10007', '999111007', CURDATE(), 1, NULL, NULL),
(18, 1, 'CMP10008', '999111008', CURDATE(), 1, NULL, NULL),
(19, 2, 'CMP10009', '999111009', CURDATE(), 1, NULL, NULL),
(20, 3, 'CMP10010', '999111010', CURDATE(), 1, NULL, NULL);

-- passwd es Medico
INSERT INTO tb_usuario (
    id_persona,
    correo,
    passwd,
    rol,
    activo
) VALUES
(11, 'medico1@gmail.com', '$2a$10$K8nR2VxL7QwZcJpT4mY9HeBzW1uFaGdR3sT6yUiOpQrStUvWxYz12', 'MEDICO', 1),
(12, 'medico2@gmail.com', '$2a$10$K8nR2VxL7QwZcJpT4mY9HeBzW1uFaGdR3sT6yUiOpQrStUvWxYz12', 'MEDICO', 1),
(13, 'medico3@gmail.com', '$2a$10$K8nR2VxL7QwZcJpT4mY9HeBzW1uFaGdR3sT6yUiOpQrStUvWxYz12', 'MEDICO', 1),
(14, 'medico4@gmail.com', '$2a$10$K8nR2VxL7QwZcJpT4mY9HeBzW1uFaGdR3sT6yUiOpQrStUvWxYz12', 'MEDICO', 1),
(15, 'medico5@gmail.com', '$2a$10$K8nR2VxL7QwZcJpT4mY9HeBzW1uFaGdR3sT6yUiOpQrStUvWxYz12', 'MEDICO', 1),
(16, 'medico6@gmail.com', '$2a$10$K8nR2VxL7QwZcJpT4mY9HeBzW1uFaGdR3sT6yUiOpQrStUvWxYz12', 'MEDICO', 1),
(17, 'medico7@gmail.com', '$2a$10$K8nR2VxL7QwZcJpT4mY9HeBzW1uFaGdR3sT6yUiOpQrStUvWxYz12', 'MEDICO', 1),
(18, 'medico8@gmail.com', '$2a$10$K8nR2VxL7QwZcJpT4mY9HeBzW1uFaGdR3sT6yUiOpQrStUvWxYz12', 'MEDICO', 1),
(19, 'medico9@gmail.com', '$2a$10$K8nR2VxL7QwZcJpT4mY9HeBzW1uFaGdR3sT6yUiOpQrStUvWxYz12', 'MEDICO', 1),
(20, 'medico10@gmail.com', '$2a$10$K8nR2VxL7QwZcJpT4mY9HeBzW1uFaGdR3sT6yUiOpQrStUvWxYz12', 'MEDICO', 1);

-- Cita

INSERT INTO tb_cita (
    id_paciente,
    id_medico,
    fecha_programada,
    hora,
    id_estado_cita,
    motivo,
    activo
) VALUES
(1, 1, '2026-03-01', '09:00:00', 2, 'Dolor de cabeza persistente', 1),
(2, 2, '2026-03-02', '10:00:00', 2, 'Control general', 1),
(3, 3, '2026-03-03', '11:00:00', 2, 'Dolor abdominal', 1),
(4, 4, '2026-03-04', '12:00:00', 2, 'Consulta dermatológica', 1),
(5, 5, '2026-03-05', '13:00:00', 2, 'Chequeo cardiológico', 1),
(6, 6, '2026-03-06', '14:00:00', 2, 'Consulta neurológica', 1),
(7, 7, '2026-03-07', '15:00:00', 2, 'Dolor de rodilla', 1),
(8, 8, '2026-03-08', '16:00:00', 2, 'Revisión médica', 1),
(9, 9, '2026-03-09', '17:00:00', 2, 'Fiebre alta', 1),
(10,10,'2026-03-10', '08:30:00', 2, 'Consulta preventiva', 1),
(1, 3, '2026-03-11', '09:30:00', 2, 'Dolor estomacal', 1),
(2, 4, '2026-03-12', '10:30:00', 2, 'Problema en la piel', 1),
(3, 5, '2026-03-13', '11:30:00', 2, 'Control cardiológico', 1),
(4, 6, '2026-03-14', '12:30:00', 2, 'Migraña frecuente', 1),
(5, 7, '2026-03-15', '13:30:00', 2, 'Dolor muscular', 1);

-- Historial Medico

INSERT INTO tb_historial_medico (
    id_cita,
    fecha_consulta,
    diagnostico,
    tratamiento,
    observaciones,
    activo
) VALUES
(1, '2026-03-01', 'Migraña leve', 'Analgésicos por 5 días', 'Paciente estable', 1),
(2, '2026-03-02', 'Chequeo sin hallazgos', 'Ninguno', 'Paciente saludable', 1),
(3, '2026-03-03', 'Gastritis aguda', 'Dieta blanda y antiácidos', 'Evitar comidas irritantes', 1),
(4, '2026-03-04', 'Dermatitis leve', 'Crema tópica', 'No exposición al sol', 1),
(5, '2026-03-05', 'Hipertensión leve', 'Control de presión y dieta', 'Seguimiento en 1 mes', 1),
(6, '2026-03-06', 'Migraña crónica', 'Tratamiento preventivo', 'Evaluar evolución', 1),
(7, '2026-03-07', 'Inflamación articular', 'Antiinflamatorios', 'Reposo por 3 días', 1),
(8, '2026-03-08', 'Revisión normal', 'Ninguno', 'Paciente estable', 1),
(9, '2026-03-09', 'Infección viral', 'Paracetamol e hidratación', 'Control en 3 días', 1),
(10,'2026-03-10', 'Consulta preventiva normal', 'Vitaminas', 'Sin complicaciones', 1),
(11,'2026-03-11', 'Indigestión', 'Protector gástrico', 'Evitar grasas', 1),
(12,'2026-03-12', 'Alergia leve', 'Antihistamínico', 'Seguimiento si persiste', 1),
(13,'2026-03-13', 'Control cardiológico estable', 'Continuar tratamiento', 'Revisión en 6 meses', 1),
(14,'2026-03-14', 'Migraña recurrente', 'Analgésicos', 'Control en 1 mes', 1),
(15,'2026-03-15', 'Contractura muscular', 'Relajante muscular', 'Evitar esfuerzo físico', 1);