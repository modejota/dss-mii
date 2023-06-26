package practicas.GomezGarciaJoseAlberto_p1.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
public class Carrito {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	// Está pensado para que cada usuario tenga un carrito, por lo que ambos ID deberían ser los mismos.
	// Los métodos controladores del usuario crean ambos a la vez, y no se puede borrar el carrito por separado.
	private Long id;

	@ManyToMany(cascade = CascadeType.ALL)
	private List<Producto> productos = new ArrayList<>();

	@OneToOne(mappedBy="carrito")
	private Usuario Usuario;
	
	public Carrito() {}
	
	public Carrito(Long _id) {
		this.id = _id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Producto> getProductos() {
		return productos;
	}

	public void addProducto(Producto producto) {
		this.productos.add(producto);
	}

	public void deleteProducto(Long idProducto) {
		for (Producto producto : productos) {
			if (Objects.equals(producto.getId(), idProducto)) {
				productos.remove(producto);
				break;
			}
		}
	}

	public void resetProductos() {
		this.productos = new ArrayList<>();
	}

}
