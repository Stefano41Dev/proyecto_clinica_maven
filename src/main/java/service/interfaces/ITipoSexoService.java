package service.interfaces;

import java.util.List;

import dto.tipo_sexo.TipoSexoRequest;
import dto.tipo_sexo.TipoSexoResponse;

public interface ITipoSexoService {
	 List<TipoSexoResponse> listar();
	 TipoSexoResponse buscarPorId(int idSexo);
	 TipoSexoResponse guardar(TipoSexoRequest tipoSexo);
	 TipoSexoResponse actualizar(int idSexo, TipoSexoRequest tipoSexo);
     void eliminar(int idSexo);
}
