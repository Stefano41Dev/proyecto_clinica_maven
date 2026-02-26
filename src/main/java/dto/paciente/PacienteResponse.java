package dto.paciente;

import com.fasterxml.jackson.annotation.JsonFormat;
import dto.estado_cita.EstadoCitaResponse;
import dto.tipo_documento.TipoDocumentoResponse;
import dto.tipo_sexo.TipoSexoResponse;
import lombok.Builder;

import java.time.LocalDate;
import java.util.Date;

@Builder
public record PacienteResponse(
        Integer idPaciente,
        //Integer idTipoDocumento,
        TipoDocumentoResponse tipoDocumentoResponse,
        String numeroDocumento,
        String nombres,
        String apellidos,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        Date fechaNacimiento,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        Date fechaRegistro,
        //Integer idSexo,
        //String nombreSexo,
        TipoSexoResponse tipoSexoResponse,
        //Integer idEstadoCivil,
        //String nombreEstadoCivil,
        EstadoCitaResponse estadoCitaResponse,
        String correo
) {}