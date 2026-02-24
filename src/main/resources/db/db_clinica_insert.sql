use bd_clinica;
-- para mi usuario administrador
INSERT INTO tb_persona (nombres,apellidos,correo,activo)
VALUES ('Stefano','Gonzales','stefano@gmail.com',1);
-- contraseña es Stefano
INSERT INTO tb_usuario(id_persona,correo,passwd,rol,activo)
VALUES (1, 'stefano@gmail.com', '$2a$12$WnWvkKFInzkprf5MsNA2r.bAMPrdvJTggCZ1VCAUvTujkhdGEiuY2', 'ADMINISTRADOR',1)
