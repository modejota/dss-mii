package practicas.GomezGarciaJoseAlberto_p1.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import practicas.GomezGarciaJoseAlberto_p1.modelo.Usuario;
import practicas.GomezGarciaJoseAlberto_p1.services.UsuarioServiceImpl;

import java.util.List;

@Path("usuarios")
public class UserController {

    private String BASE_URI = "http://localhost:8080/GomezGarciaJoseAlberto-p1/rest/usuarios/";
    private UsuarioServiceImpl usuarioService = new UsuarioServiceImpl();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() throws JsonProcessingException {
        List<Usuario> listaUsuarios = usuarioService.getAll();
        String json =  new ObjectMapper().writeValueAsString(listaUsuarios);
        return Response.ok(json).build();
    }


    @GET
    @Path("{username}")
    @Produces(MediaType.APPLICATION_JSON)
    // Este método no tiene demasiado sentido en la API actual, dado que solo guardamos username, password y rol.
    // Si se tuviera alguna información adicional, como calle, fecha de alta, ..., sí que tendría más sentido.
    // Se deja de cara al futuro. En cualquier caso, devuelve el campo contraseña vacío (sin ni siquiera el hash)
    public Response getByUsername(@PathParam("username") Long id) throws Exception {

        try {
            Usuario userN = usuarioService.get(id);
            String json =  new ObjectMapper().writeValueAsString(userN);
            return Response.ok(json).build();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("{error: Usuario con ID '" + id + "' no encontrado.}")
                    .build();
        }
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(Usuario usuario) {
        try {
            usuarioService.add(usuario);
            return Response
                    .status(Response.Status.CREATED)
                    .header("Location", BASE_URI + usuario.getId())
                    .build();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("{error: El usuario ya existe.}")
                    .build();
        }
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") Long id, Usuario usuario) {
        try {
            usuarioService.edit(id, usuario.getUsername(), usuario.getPassword(), usuario.getRole());
            return Response
                    .status(Response.Status.OK)
                    .entity("{result: Usuario modificado con éxito}")
                    .build();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{error: " + e.getMessage() + "}")
                    .build();
        }
    }

    @DELETE
    @Path("{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("username") Long id) {
        try {
            usuarioService.delete(id);
            return Response
                    .status(Response.Status.OK)
                    .entity("{result: Usuario eliminado con éxito}")
                    .build();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("{error: " + e.getMessage() + "}")
                    .build();
        }
    }
}
