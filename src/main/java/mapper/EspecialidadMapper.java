package mapper;

import dto.especialidad.EspecialidadResponse;
import model.Especialidad;

public class EspecialidadMapper {
    public EspecialidadResponse toDto(Especialidad especialidad) {
        return EspecialidadResponse.builder()
                .idEspecialidad(especialidad.getIdEspecialidad())
                .nombre(especialidad.getNombre())
                .build();
    }
}
