package practicas.GomezGarciaJoseAlberto_p1.services;

import practicas.GomezGarciaJoseAlberto_p1.modelo.Usuario;

import java.util.List;

public interface UsuarioService {

    void add (Usuario usuario) throws Exception;
    void delete (Long id) throws Exception;
    void edit (Long id, String username, String password, String role) throws Exception;
    Usuario get(Long id) throws Exception;
    Usuario getByUsername(String username) throws Exception;
    Usuario getWithPassword(String username) throws Exception;
    List<Usuario> getAll();

}
