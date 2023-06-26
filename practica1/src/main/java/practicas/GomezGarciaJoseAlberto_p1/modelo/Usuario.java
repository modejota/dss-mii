package practicas.GomezGarciaJoseAlberto_p1.modelo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // Podría ser un DNI sin letra, en nuestro caso es un ID auto-incremental "interno" de la BD.
    
    @JsonProperty("username")
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @JsonProperty("password")
    @Column(name="password", nullable = false)
    private String password;

    @JsonProperty("role")
    @Column(name="role", nullable = false)
    private String role;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Carrito carrito;

    public Usuario() {}
    public Usuario(String _username, String _password, String _user) {
        this.username = _username;
        this.password = _password;
        this.role = _user;
    }

    public Long getId() { return this.id; }
    public void setId(Long _id) { this.id = _id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public boolean isAdmin() { return this.role.equals("admin") || this.role.equals("ADMIN"); }


    public String serialize() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }

    @Override
    public String toString() {
        return "Usuario {" +
                "username=" + this.username +
                ", password=' '" +
                '}';
    }


}
