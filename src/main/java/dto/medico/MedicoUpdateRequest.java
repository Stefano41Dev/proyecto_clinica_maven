package dto.medico;

public record MedicoUpdateRequest (
        String nombres,
        String apellidos,
        Integer idEspecialidad,
        String numeroColegiatura,
        String telefono
){
}
