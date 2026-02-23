package service.impl;

import dto.medico.MedicoRequest;
import dto.medico.MedicoResponse;
import service.interfaces.IMedicoService;

import java.util.List;

public class MedicoServiceImpl implements IMedicoService {
    @Override
    public List<MedicoResponse> listar(int pagina, int tamanioPagina) {
        return List.of();
    }

    @Override
    public MedicoResponse buscarPorId(int idMedico) {
        return null;
    }

    @Override
    public MedicoResponse registrarMedico(MedicoRequest medico) {

        return null;
    }

    @Override
    public MedicoResponse registrarMedico(int id, MedicoRequest medico) {
        return null;
    }

    @Override
    public void eliminar(int idMedico) {

    }
}
