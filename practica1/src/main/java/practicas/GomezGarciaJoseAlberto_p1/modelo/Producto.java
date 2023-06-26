package practicas.GomezGarciaJoseAlberto_p1.modelo;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;

@Entity
public class Producto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty("id")
	private Long id;	//Podría ser por ejemplo un código de barras o ID similar
	
	@JsonProperty("nombre")
	@Column(name="nombre", unique = true, nullable=false)
	private String nombre;
    
	@JsonProperty("precio")
	@Column(name="precio", nullable=false)
	private double precio;
	
	@JsonProperty("imagen")
	@Column(name="imagen", nullable=false)
	@Lob	// Objeto grande, dado que pretendemos almacenar una imagen codificada en base64.
	private String imagen;
	
	public Producto() {}
	public Producto(Long _id, String _nombre,double _precio, String _imagen) {
		this.id = _id;
		this.nombre = _nombre;
		this.precio = _precio;
		this.imagen = _imagen;
	}
	public Producto(String jsonObject) throws JsonProcessingException {
		Producto p = new ObjectMapper().readValue(jsonObject, Producto.class);
		this.id = p.id;
		this.nombre = p.nombre;
		this.precio = p.precio;
		this.imagen = p.imagen;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public double getPrecio() {
		return precio;
	}
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	public String getImagen() {
		return imagen;
	}
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	
	public String serialize() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(this);
	}
	
    @Override
    public String toString() {
        return "Producto {" +
                "id=" + this.id +
                ", nombre='" + this.nombre + '\'' +
                ", precio='" + this.precio + '\'' +
                ", imagen=" + this.imagen +
                '}';
    }
	
}
