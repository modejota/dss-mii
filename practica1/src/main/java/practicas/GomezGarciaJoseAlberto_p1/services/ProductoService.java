package practicas.GomezGarciaJoseAlberto_p1.services;

import java.util.List;

import practicas.GomezGarciaJoseAlberto_p1.modelo.Producto;

public interface ProductoService {
	void add (Producto producto) throws Exception;
	void delete (Long id) throws Exception;
	void edit (Long id, Producto producto) throws Exception;
	Producto get(Long id) throws Exception;
	List<Producto> getAll();
}
