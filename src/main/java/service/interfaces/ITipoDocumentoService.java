package service.interfaces;

import java.util.List;

import dto.tipo_documento.TipoDocumentoRequest;
import dto.tipo_documento.TipoDocumentoResponse;

public interface ITipoDocumentoService {
	List<TipoDocumentoResponse> listar();
	TipoDocumentoResponse buscarPorId(int idTipoDocumento);
	TipoDocumentoResponse guardar(TipoDocumentoRequest tipoDocumento);
	TipoDocumentoResponse actualizar(int id, TipoDocumentoRequest tipoDocumento);
    void eliminar(int idTipoDocumento);
}
