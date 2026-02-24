use bd_clinica;

DELIMITER //

CREATE PROCEDURE registrar_medico(
    IN p_nombres VARCHAR(100),
    IN p_apellidos VARCHAR(100),
    IN p_correo VARCHAR(100),
    IN p_numero_colegiatura VARCHAR(50),
    IN p_telefono VARCHAR(20),
    IN p_id_especialidad INT,
    IN p_password VARCHAR(100),
    IN p_token_verificacion VARCHAR(255),
    IN p_token_expiracion DATETIME
)
BEGIN
    DECLARE v_id_persona INT;
    DECLARE v_id_medico INT;
    DECLARE v_existe INT DEFAULT 0;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    SELECT COUNT(*)
    INTO v_existe
    FROM tb_usuario
    WHERE correo = p_correo AND activo = 1;

    IF v_existe > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El correo ya está registrado';
    END IF;

    INSERT INTO tb_persona(nombres,apellidos,correo,activo)
    VALUES (p_nombres,p_apellidos,p_correo,0);

    SET v_id_persona = LAST_INSERT_ID();

    INSERT INTO tb_medico(id_persona,id_especialidad,numero_colegiatura,telefono,fecha_registro,activo,token_verificacion,token_expiracion)
    VALUES (v_id_persona,p_id_especialidad,p_numero_colegiatura,p_telefono,CURDATE(),0,p_token_verificacion,p_token_expiracion);

    SET v_id_medico = LAST_INSERT_ID();

    INSERT INTO tb_usuario(id_persona,correo,passwd,rol,activo)
    VALUES (v_id_persona,p_correo,p_password,'MEDICO',0);

    COMMIT;

    -- Retornar el medico recién creado
    SELECT 
        m.id_medico,
        p.nombres,
        p.apellidos,
        u.correo,
        m.numero_colegiatura,
        m.telefono,
        m.id_especialidad,
        e.nombre,
        m.fecha_registro
    FROM tb_medico m
    INNER JOIN tb_persona p ON p.id_persona = m.id_persona
    INNER JOIN tb_usuario u ON u.id_persona = m.id_persona
    INNER JOIN tb_especialidad e ON m.id_especialidad = e.id_especialidad
    WHERE m.id_medico = v_id_medico;

END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE activar_medico (
    IN p_token_verificacion VARCHAR(255),
    OUT p_activado BOOLEAN
)
BEGIN
    DECLARE v_id_persona INT;
    DECLARE v_count INT;

    SET p_activado = FALSE;

    START TRANSACTION;

    SELECT id_persona
    INTO v_id_persona
    FROM tb_medico
    WHERE token_verificacion = p_token_verificacion
      AND activo = 0
    LIMIT 1;

    -- Si no se encontró ningún médico pendiente de activación, salimos
   IF v_id_persona IS NULL THEN
        ROLLBACK;
      
    ELSE

		UPDATE tb_medico
		SET activo = 1,
			token_verificacion = NULL,
			token_expiracion = NULL
		WHERE token_verificacion = p_token_verificacion;

		UPDATE tb_usuario
		SET activo = 1
		WHERE id_persona = v_id_persona
		  AND rol = 'MEDICO';

		UPDATE tb_persona
		SET activo = 1
		WHERE id_persona = v_id_persona;

		COMMIT;

		SET p_activado = TRUE;
	END IF;
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE buscar_medico_id(
	IN p_id_medico INT
)
BEGIN
	SELECT 
        m.id_medico,
        p.nombres,
        p.apellidos,
        u.correo,
        m.numero_colegiatura,
        m.telefono,
        m.id_especialidad,
        e.nombre,
        m.fecha_registro
    FROM tb_medico m
    INNER JOIN tb_persona p ON p.id_persona = m.id_persona
    INNER JOIN tb_usuario u ON u.id_persona = m.id_persona
    INNER JOIN tb_especialidad e ON m.id_especialidad = e.id_especialidad
    WHERE m.id_medico = p_id_medico;
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE actualizar_medico_id(
	IN p_id_medico INT,
	IN p_nombres VARCHAR(100),
    IN p_apellidos VARCHAR(100),
    IN p_numero_colegiatura VARCHAR(50),
    IN p_telefono VARCHAR(20),
    IN p_id_especialidad INT
)
BEGIN
    DECLARE v_id_persona INT;
	
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;
    
    SELECT id_persona
    INTO v_id_persona
    FROM tb_medico WHERE id_medico = p_id_medico AND activo = 1 LIMIT 1;
    
	UPDATE tb_medico
		SET numero_colegiatura = p_numero_colegiatura,
			telefono = p_telefono,
			id_especialidad = p_id_especialidad
	WHERE id_medico = p_id_medico AND activo = 1 ;
    
    UPDATE tb_persona
		SET nombres = p_nombres,
			apellidos = p_apellidos
	WHERE id_persona = v_id_persona;
    
	
    COMMIT;
    
    SELECT 
        m.id_medico,
        p.nombres,
        p.apellidos,
        u.correo,
        m.numero_colegiatura,
        m.telefono,
        m.id_especialidad,
        e.nombre,
        m.fecha_registro
    FROM tb_medico m
    INNER JOIN tb_persona p ON p.id_persona = m.id_persona
    INNER JOIN tb_usuario u ON u.id_persona = m.id_persona
    INNER JOIN tb_especialidad e ON m.id_especialidad = e.id_especialidad
    WHERE m.id_medico = p_id_medico;        
END //

DELIMITER ;