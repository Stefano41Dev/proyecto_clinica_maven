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
    WHERE m.id_medico = p_id_medico AND m.activo = 1;
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE actualizar_medico_id(
	IN p_id_medico INT,
	IN p_nombres VARCHAR(100),
    IN p_apellidos VARCHAR(100),
    IN p_numero_colegiatura VARCHAR(50),
    IN p_telefono VARCHAR(20),
    IN p_id_especialidad INT,
    IN p_correo VARCHAR(100),
    IN p_password VARCHAR(250)
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
			apellidos = p_apellidos,
            correo = p_correo
	WHERE id_persona = v_id_persona AND activo = 1 ;
    
	UPDATE tb_usuario
		SET correo = p_correo,
			passwd = p_password
	WHERE id_persona = v_id_persona AND activo = 1 ; 
    
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

DELIMITER //

CREATE PROCEDURE listar_medico_paginacion(
	IN p_limit INT,
    IN p_offset INT
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
    WHERE m.activo = 1
    LIMIT p_limit OFFSET p_offset;
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE eliminar_medico_por_id(
	IN p_id_medico INT
)
BEGIN
	 DECLARE v_id_persona INT;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SELECT 'ERROR AL ELIMINAR EL MEDICO' AS mensaje;
    END;

    START TRANSACTION;

    SELECT id_persona
    INTO v_id_persona
    FROM tb_medico 
    WHERE id_medico = p_id_medico AND activo = 1
    LIMIT 1;

    IF v_id_persona IS NULL THEN
        ROLLBACK;
        SELECT 'MEDICO NO ENCONTRADO O YA INACTIVO' AS mensaje;
    ELSE

        UPDATE tb_medico
        SET activo = 0
        WHERE id_medico = p_id_medico AND activo = 1;

        UPDATE tb_persona
        SET activo = 0
        WHERE id_persona = v_id_persona AND activo = 1;

        UPDATE tb_usuario
        SET activo = 0
        WHERE id_persona = v_id_persona AND activo = 1;

        COMMIT;

        SELECT 'SE ELIMINO EL MEDICO CORRECTAMENTE' AS mensaje;
    END IF;
END //
DELIMITER ;
-- Paciente

DELIMITER //

CREATE PROCEDURE registrar_paciente(
	IN p_nombres VARCHAR(100),
    IN p_apellidos VARCHAR(100),
    IN p_correo VARCHAR(100),
    IN p_password VARCHAR(250),
	IN p_id_tipo_documento INT,
    IN p_numero_documento VARCHAR(20),
    IN p_fecha_nacimiento DATE,
    IN p_id_sexo INT,
    IN p_estado_civil INT,
    IN p_token_verificacion VARCHAR(255),
    IN p_token_expiracion DATETIME
)
BEGIN
	DECLARE v_id_persona INT;
    DECLARE v_id_paciente INT;
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

    INSERT INTO tb_paciente(id_persona,id_tipo_documento,numero_documento,fecha_nacimiento,fecha_registro,id_sexo,id_estado_civil,activo,token_verificacion,token_expiracion)
    VALUES (v_id_persona,p_id_tipo_documento,p_numero_documento,p_fecha_nacimiento,CURDATE(),p_id_sexo,p_estado_civil,0,p_token_verificacion,p_token_expiracion);

    SET v_id_paciente = LAST_INSERT_ID();

    INSERT INTO tb_usuario(id_persona,correo,passwd,rol,activo)
    VALUES (v_id_persona,p_correo,p_password,'PACIENTE',0);

    COMMIT;
    
    SELECT 
    pa.id_paciente,
    pe.nombres,
    pe.apellidos,
    pe.correo,
    td.nombre_documento,
    pa.numero_documento,
    pa.fecha_nacimiento,
    pa.fecha_registro,
    ts.sexo,
    tec.nombre_estado
    FROM tb_paciente pa
    INNER JOIN tb_persona pe ON pe.id_persona = pa.id_persona
    INNER JOIN tb_tipo_documento td ON td.id_tipo_documento = pa.id_tipo_documento
    INNER JOIN tb_tipo_sexo ts ON ts.id_sexo = pa.id_sexo
    INNER JOIN tb_estado_civil tec ON tec.id_estado_civil = pa.id_estado_civil
    WHERE pa.id_paciente = v_id_paciente ;
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE activar_paciente (
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
    FROM tb_paciente
    WHERE token_verificacion = p_token_verificacion
      AND activo = 0
    LIMIT 1;

    -- Si no se encontró ningún médico pendiente de activación, salimos
   IF v_id_persona IS NULL THEN
        ROLLBACK;
      
    ELSE

		UPDATE tb_paciente
		SET activo = 1,
			token_verificacion = NULL,
			token_expiracion = NULL
		WHERE token_verificacion = p_token_verificacion;

		UPDATE tb_usuario
		SET activo = 1
		WHERE id_persona = v_id_persona
		  AND rol = 'PACIENTE';

		UPDATE tb_persona
		SET activo = 1
		WHERE id_persona = v_id_persona;

		COMMIT;

		SET p_activado = TRUE;
	END IF;
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE listar_paciente_paginado(
	IN p_limit INT,
    IN p_offset INT
)
BEGIN
 SELECT 
    pa.id_paciente,
    pe.nombres,
    pe.apellidos,
    pe.correo,
    pa.id_tipo_documento,
    td.nombre_documento,
    pa.numero_documento,
    pa.fecha_nacimiento,
    pa.fecha_registro,
    pa.id_sexo,
    ts.sexo,
    pa.id_estado_civil,
    tec.nombre_estado
    FROM tb_paciente pa
    INNER JOIN tb_persona pe ON pe.id_persona = pa.id_persona
    INNER JOIN tb_tipo_documento td ON td.id_tipo_documento = pa.id_tipo_documento
    INNER JOIN tb_tipo_sexo ts ON ts.id_sexo = pa.id_sexo
    INNER JOIN tb_estado_civil tec ON tec.id_estado_civil = pa.id_estado_civil
    WHERE pa.activo = 1
	LIMIT p_limit OFFSET p_offset;

END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE buscar_paciente_id(
	IN p_id_paciente INT
)
BEGIN
	 SELECT 
    pa.id_paciente,
    pe.nombres,
    pe.apellidos,
    pe.correo,
    pa.id_tipo_documento,
    td.nombre_documento,
    pa.numero_documento,
    pa.fecha_nacimiento,
    pa.fecha_registro,
    pa.id_sexo,
    ts.sexo,
    pa.id_estado_civil,
    tec.nombre_estado
    FROM tb_paciente pa
    INNER JOIN tb_persona pe ON pe.id_persona = pa.id_persona
    INNER JOIN tb_tipo_documento td ON td.id_tipo_documento = pa.id_tipo_documento
    INNER JOIN tb_tipo_sexo ts ON ts.id_sexo = pa.id_sexo
    INNER JOIN tb_estado_civil tec ON tec.id_estado_civil = pa.id_estado_civil
    WHERE pa.id_paciente = p_id_paciente AND pa.activo = 1;
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE eliminar_paciente_por_id(
	IN p_id_paciente INT
)
BEGIN
	DECLARE v_id_persona INT;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SELECT 'ERROR AL ELIMINAR AL PACIENTE' AS mensaje;
    END;

    START TRANSACTION;

    SELECT id_persona
    INTO v_id_persona
    FROM tb_paciente
    WHERE id_paciente = p_id_paciente AND activo = 1
    LIMIT 1;

    IF v_id_persona IS NULL THEN
        ROLLBACK;
        SELECT 'PACIENTE NO ENCONTRADO O YA INACTIVO' AS mensaje;
    ELSE

        UPDATE tb_paciente
        SET activo = 0
        WHERE id_paciente = p_id_paciente AND activo = 1;

        UPDATE tb_persona
        SET activo = 0
        WHERE id_persona = v_id_persona AND activo = 1;

        UPDATE tb_usuario
        SET activo = 0
        WHERE id_persona = v_id_persona AND activo = 1;

        COMMIT;

        SELECT 'SE ELIMINO EL PACIENTE CORRECTAMENTE' AS mensaje;
    END IF;
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE actualizar_paciente_id(
	IN p_id_paciente INT,
	IN p_nombres VARCHAR(100),
    IN p_apellidos VARCHAR(100),
    IN p_correo VARCHAR(100),
    IN p_password VARCHAR(250),
	IN p_id_tipo_documento INT,
    IN p_numero_documento VARCHAR(20),
    IN p_fecha_nacimiento DATE,
    IN p_id_sexo INT,
    IN p_id_estado_civil INT
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
    FROM tb_paciente WHERE id_paciente = p_id_paciente AND activo = 1 LIMIT 1;
    
	UPDATE tb_paciente
		SET id_tipo_documento = p_id_tipo_documento,
			numero_documento = p_numero_documento,
			fecha_nacimiento = p_fecha_nacimiento,
            id_sexo = p_id_sexo,
            id_estado_civil = p_id_estado_civil
	WHERE id_paciente = p_id_paciente AND activo = 1 ;
    
    UPDATE tb_persona
		SET nombres = p_nombres,
			apellidos = p_apellidos,
            correo = p_correo
	WHERE id_persona = v_id_persona AND activo = 1 ;
    
	UPDATE tb_usuario
		SET correo = p_correo,
			passwd = p_password
	WHERE id_persona = v_id_persona AND activo = 1 ; 
    
    COMMIT;
    
     SELECT 
    pa.id_paciente,
    pe.nombres,
    pe.apellidos,
    pe.correo,
    pa.id_tipo_documento,
    td.nombre_documento,
    pa.numero_documento,
    pa.fecha_nacimiento,
    pa.fecha_registro,
    pa.id_sexo,
    ts.sexo,
    pa.id_estado_civil,
    tec.nombre_estado
    FROM tb_paciente pa
    INNER JOIN tb_persona pe ON pe.id_persona = pa.id_persona
    INNER JOIN tb_tipo_documento td ON td.id_tipo_documento = pa.id_tipo_documento
    INNER JOIN tb_tipo_sexo ts ON ts.id_sexo = pa.id_sexo
    INNER JOIN tb_estado_civil tec ON tec.id_estado_civil = pa.id_estado_civil
    WHERE pa.id_paciente = p_id_paciente AND pa.activo = 1;

END //
DELIMITER ;

DELIMITER //

CREATE PROCEDURE buscar_cita_datos_completos_id(
	IN p_id_cita INT
)
BEGIN
	SELECT 
		-- ========================
		-- DATOS DE CITA
		-- ========================
		c.id_cita,
		c.fecha_programada,
		c.hora,
		c.motivo,
		c.activo AS cita_activa,

		-- ========================
		-- ESTADO CITA
		-- ========================
		ec.id_estado_cita,
		ec.nombre_estado AS estado_cita,
		ec.activo AS estado_activo,

		-- ========================
		-- PACIENTE
		-- ========================
		pa.id_paciente,
		pa.numero_documento,
		pa.fecha_nacimiento,
		pa.fecha_registro AS paciente_fecha_registro,
		pa.activo AS paciente_activo,

		-- PERSONA PACIENTE
		pp.id_persona AS persona_paciente_id,
		pp.nombres AS paciente_nombres,
		pp.apellidos AS paciente_apellidos,
		pp.correo AS paciente_correo,

		-- TIPO DOCUMENTO
		td.id_tipo_documento,
		td.nombre_documento,

		-- SEXO
		ts.id_sexo,
		ts.sexo,

		-- ESTADO CIVIL
		ecv.id_estado_civil,
		ecv.nombre_estado AS estado_civil,

		-- ========================
		-- MEDICO
		-- ========================
		m.id_medico,
		m.numero_colegiatura,
		m.telefono,
		m.fecha_registro AS medico_fecha_registro,
		m.activo AS medico_activo,

		-- PERSONA MEDICO
		pm.id_persona AS persona_medico_id,
		pm.nombres AS medico_nombres,
		pm.apellidos AS medico_apellidos,
		pm.correo AS medico_correo,

		-- ESPECIALIDAD
		es.id_especialidad,
		es.nombre AS especialidad

	FROM tb_cita c

	LEFT JOIN tb_estado_cita ec 
		ON ec.id_estado_cita = c.id_estado_cita

	LEFT JOIN tb_paciente pa 
		ON pa.id_paciente = c.id_paciente

	LEFT JOIN tb_persona pp 
		ON pp.id_persona = pa.id_persona

	LEFT JOIN tb_tipo_documento td 
		ON td.id_tipo_documento = pa.id_tipo_documento

	LEFT JOIN tb_tipo_sexo ts 
		ON ts.id_sexo = pa.id_sexo

	LEFT JOIN tb_estado_civil ecv 
		ON ecv.id_estado_civil = pa.id_estado_civil

	LEFT JOIN tb_medico m 
		ON m.id_medico = c.id_medico

	LEFT JOIN tb_persona pm 
		ON pm.id_persona = m.id_persona

	LEFT JOIN tb_especialidad es 
		ON es.id_especialidad = m.id_especialidad

	WHERE c.id_cita = p_id_cita
	AND c.activo = 1;

END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE registrar_cita_validando(
    IN p_id_paciente INT,
    IN p_id_medico INT,
    IN p_fecha_programada DATE,
    IN p_hora TIME,
    IN p_id_estado_cita INT,
    IN p_motivo TEXT
)
BEGIN
    DECLARE v_existe INT DEFAULT 0;

    SELECT COUNT(*)
    INTO v_existe
    FROM tb_cita
    WHERE id_medico = p_id_medico
      AND fecha_programada = p_fecha_programada
      AND hora = p_hora
      AND activo = 1;

    IF v_existe > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El medico ya tiene una cita programada en esa fecha y hora';
    ELSE

        INSERT INTO tb_cita(
            id_paciente,
            id_medico,
            fecha_programada,
            hora,
            id_estado_cita,
            motivo,
            activo
        )
        VALUES (
            p_id_paciente,
            p_id_medico,
            p_fecha_programada,
            p_hora,
            p_id_estado_cita,
            p_motivo,
            1
        );

        SELECT LAST_INSERT_ID() AS id_cita;

    END IF;

END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE actualizar_cita_validando(
    IN p_id_cita INT,
    IN p_id_medico INT,
    IN p_fecha_programada DATE,
    IN p_hora TIME
)
BEGIN
    DECLARE v_existe INT DEFAULT 0;

    -- Verificar si la cita existe
    IF NOT EXISTS (
        SELECT 1 FROM tb_cita 
        WHERE id_cita = p_id_cita
        AND activo = 1
    ) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La cita no existe o esta inactiva';
    END IF;

    -- Verificar conflicto de horario (excluyendo la misma cita)
    SELECT COUNT(*)
    INTO v_existe
    FROM tb_cita
    WHERE id_medico = p_id_medico
      AND fecha_programada = p_fecha_programada
      AND hora = p_hora
      AND id_cita <> p_id_cita
      AND activo = 1;

    IF v_existe > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El medico ya tiene una cita programada en esa fecha y hora';
    ELSE

        UPDATE tb_cita
        SET id_medico = p_id_medico,
            fecha_programada = p_fecha_programada,
            hora = p_hora
        WHERE id_cita = p_id_cita;

        SELECT p_id_cita AS id_cita;
		
    END IF;
	
END //

DELIMITER ;


DELIMITER //

CREATE PROCEDURE listar_citas_paginado(
	IN p_limit INT,
    IN p_offset INT
)
BEGIN

    SELECT 
        -- ======================
        -- CITA
        -- ======================
        c.id_cita,
        c.fecha_programada,
        c.hora,
        c.motivo,

        -- ======================
        -- PACIENTE
        -- ======================
        p.id_paciente,
        pp.nombres AS paciente_nombres,
        pp.apellidos AS paciente_apellidos,

        -- ======================
        -- MEDICO
        -- ======================
        m.id_medico,
        pm.nombres AS medico_nombres,
        pm.apellidos AS medico_apellidos,

        -- ======================
        -- ESTADO CITA
        -- ======================
        ec.id_estado_cita,
        ec.nombre_estado

    FROM tb_cita c

    INNER JOIN tb_paciente p 
        ON p.id_paciente = c.id_paciente

    INNER JOIN tb_persona pp 
        ON pp.id_persona = p.id_persona

    INNER JOIN tb_medico m 
        ON m.id_medico = c.id_medico

    INNER JOIN tb_persona pm 
        ON pm.id_persona = m.id_persona

    INNER JOIN tb_estado_cita ec 
        ON ec.id_estado_cita = c.id_estado_cita

    WHERE c.activo = 1

    LIMIT p_limit OFFSET p_offset;

END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE registrar_historial_medico(
    IN p_id_cita INT,
    IN p_diagnostico TEXT,
    IN p_tratamiento TEXT,
    IN p_observaciones TEXT
)
BEGIN
    DECLARE v_fecha_consulta DATE;
	DECLARE v_existe_historial INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    -- Validar que la cita exista
    SELECT fecha_programada 
    INTO v_fecha_consulta 
    FROM tb_cita 
    WHERE id_cita = p_id_cita
    LIMIT 1;

    IF v_fecha_consulta IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La cita no existe';
    END IF;

	SELECT COUNT(*) 
    INTO v_existe_historial
    FROM tb_historial_medico
    WHERE id_cita = p_id_cita;

    IF v_existe_historial > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Ya existe un historial medico para esta cita';
    END IF;
    
    INSERT INTO tb_historial_medico 
    (id_cita, fecha_consulta, diagnostico, tratamiento, observaciones)
    VALUES 
    (p_id_cita, v_fecha_consulta, p_diagnostico, p_tratamiento, p_observaciones);

    COMMIT;

    SELECT LAST_INSERT_ID() AS id_historial;

END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE cambiar_estado_cita(
    IN p_id_cita INT,
    IN p_id_estado_cita INT
)
BEGIN
    DECLARE v_existe_estado INT;
    DECLARE v_existe_cita INT;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    SELECT COUNT(*) 
    INTO v_existe_cita
    FROM tb_cita
    WHERE id_cita = p_id_cita;

    IF v_existe_cita = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La cita no existe';
    END IF;

    -- 2️⃣ Validar que el estado exista
    SELECT COUNT(*) 
    INTO v_existe_estado
    FROM tb_estado_cita
    WHERE id_estado_cita = p_id_estado_cita;

    IF v_existe_estado = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El estado de cita no existe';
    END IF;

  
    UPDATE tb_cita 
    SET id_estado_cita = p_id_estado_cita 
    WHERE id_cita = p_id_cita;

    COMMIT;

END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE listar_historial_por_correo(
    IN p_correo VARCHAR(100),
    IN p_limit INT,
    IN p_offset INT
)
BEGIN

    -- Total de registros
    SELECT COUNT(*) AS total
    FROM tb_historial_medico hm
    INNER JOIN tb_cita c ON hm.id_cita = c.id_cita
    INNER JOIN tb_paciente pa ON c.id_paciente = pa.id_paciente
    INNER JOIN tb_persona pe ON pa.id_persona = pe.id_persona
    WHERE hm.activo = 1
      AND pe.correo = p_correo;

    -- Datos paginados
    SELECT 
        hm.id_historial,
        hm.id_cita,
        hm.fecha_consulta,
        hm.diagnostico,
        hm.tratamiento,
        hm.observaciones
    FROM tb_historial_medico hm
    INNER JOIN tb_cita c ON hm.id_cita = c.id_cita
    INNER JOIN tb_paciente pa ON c.id_paciente = pa.id_paciente
    INNER JOIN tb_persona pe ON pa.id_persona = pe.id_persona
    WHERE hm.activo = 1
      AND pe.correo = p_correo
    ORDER BY hm.fecha_consulta DESC
    LIMIT p_limit OFFSET p_offset;

END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE listar_citas_por_correo_paginado(
    IN p_correo VARCHAR(100),
    IN p_limit INT,
    IN p_offset INT
)
BEGIN

    -- ======================
    -- TOTAL REGISTROS
    -- ======================
    SELECT COUNT(*) AS total
    FROM tb_cita c
    INNER JOIN tb_paciente p ON p.id_paciente = c.id_paciente
    INNER JOIN tb_persona pp ON pp.id_persona = p.id_persona
    WHERE c.activo = 1
      AND pp.correo = p_correo;

    -- ======================
    -- DATA PAGINADA
    -- ======================
    SELECT 
        c.id_cita,
        c.fecha_programada,
        c.hora,
        c.motivo,

        p.id_paciente,
        pp.nombres AS paciente_nombres,
        pp.apellidos AS paciente_apellidos,

        m.id_medico,
        pm.nombres AS medico_nombres,
        pm.apellidos AS medico_apellidos,

        ec.id_estado_cita,
        ec.nombre_estado

    FROM tb_cita c

    INNER JOIN tb_paciente p 
        ON p.id_paciente = c.id_paciente

    INNER JOIN tb_persona pp 
        ON pp.id_persona = p.id_persona

    INNER JOIN tb_medico m 
        ON m.id_medico = c.id_medico

    INNER JOIN tb_persona pm 
        ON pm.id_persona = m.id_persona

    INNER JOIN tb_estado_cita ec 
        ON ec.id_estado_cita = c.id_estado_cita

    WHERE c.activo = 1
      AND pp.correo = p_correo

    ORDER BY c.fecha_programada DESC

    LIMIT p_limit OFFSET p_offset;

END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE listar_citas_paginado_filtro(
    IN p_limit INT,
    IN p_offset INT,
    IN p_id_estado_cita INT,
    IN p_fecha DATE
)
BEGIN

    SELECT 
        -- ======================
        -- CITA
        -- ======================
        c.id_cita,
        c.fecha_programada,
        c.hora,
        c.motivo,

        -- ======================
        -- PACIENTE
        -- ======================
        p.id_paciente,
        pp.nombres AS paciente_nombres,
        pp.apellidos AS paciente_apellidos,

        -- ======================
        -- MEDICO
        -- ======================
        m.id_medico,
        pm.nombres AS medico_nombres,
        pm.apellidos AS medico_apellidos,

        -- ======================
        -- ESTADO CITA
        -- ======================
        ec.id_estado_cita,
        ec.nombre_estado

    FROM tb_cita c

    INNER JOIN tb_paciente p 
        ON p.id_paciente = c.id_paciente

    INNER JOIN tb_persona pp 
        ON pp.id_persona = p.id_persona

    INNER JOIN tb_medico m 
        ON m.id_medico = c.id_medico

    INNER JOIN tb_persona pm 
        ON pm.id_persona = m.id_persona

    INNER JOIN tb_estado_cita ec 
        ON ec.id_estado_cita = c.id_estado_cita

    WHERE c.activo = 1
      AND (p_id_estado_cita IS NULL OR c.id_estado_cita = p_id_estado_cita)
      AND (p_fecha IS NULL OR c.fecha_programada = p_fecha)

    LIMIT p_limit OFFSET p_offset;

END //

DELIMITER ;