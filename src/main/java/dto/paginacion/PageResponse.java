package dto.paginacion;

import lombok.Getter;

import java.util.List;
@Getter
public class PageResponse<T> {
    private List<T> contenido;
    private int pagina;
    private int tamanioPagina;
    private long totalRegistros;
    private int totalPaginas;

    public PageResponse(List<T> contenido, int pagina, int tamanioPagina, long totalRegistros) {
        this.contenido = contenido;
        this.pagina = pagina;
        this.tamanioPagina = tamanioPagina;
        this.totalRegistros = totalRegistros;
        this.totalPaginas = (int) Math.ceil((double) totalRegistros / tamanioPagina);
    }
}
