package practicas.GomezGarciaJoseAlberto_p1.services;

import practicas.GomezGarciaJoseAlberto_p1.modelo.Carrito;
import practicas.GomezGarciaJoseAlberto_p1.modelo.Producto;

import java.util.List;

public interface CarritoService {

    void addProducto (Long idCarrito, Producto producto) throws Exception;
    void reset (Long id) throws Exception;
    void deleteProducto (Long idCarrito, Long idProducto) throws Exception;
    Carrito get(Long id) throws Exception;
    List<Carrito> getAll();

}
