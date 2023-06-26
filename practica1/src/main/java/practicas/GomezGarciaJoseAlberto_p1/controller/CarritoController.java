package practicas.GomezGarciaJoseAlberto_p1.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import practicas.GomezGarciaJoseAlberto_p1.cookies.SessionManager;
import practicas.GomezGarciaJoseAlberto_p1.modelo.Carrito;
import practicas.GomezGarciaJoseAlberto_p1.modelo.Producto;
import practicas.GomezGarciaJoseAlberto_p1.modelo.Usuario;
import practicas.GomezGarciaJoseAlberto_p1.services.CarritoServiceImpl;

import java.util.List;

@Path("carritos")
public class CarritoController {

    private String BASE_URI = "http://localhost:8080/GomezGarciaJoseAlberto-p1/rest/carritos/";
    private CarritoServiceImpl carritoService = new CarritoServiceImpl();


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    // No creo que tenga demasiada utilidad dada la gran cantidad de carritos que se pueden crear
    // y toda la info que hay en ellos (los base64 de la imagen estorban mucho), pero se deja hecho.
    public Response findAll(@CookieParam("sessionId") String sessionId) throws JsonProcessingException {
        Usuario u = SessionManager.getInstance().getUsuario(sessionId);
        if (u == null || !u.isAdmin())
            return Response.status(Status.UNAUTHORIZED).entity("{'error': 'Necesitas ser usuario administrador'}").build();
        List<Carrito> listaCarritos = carritoService.getAll();
        String json =  new ObjectMapper().writeValueAsString(listaCarritos);
        return Response.ok(json).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id,
                            @CookieParam("sessionId") String sessionId)
            throws Exception {
        Usuario u = SessionManager.getInstance().getUsuario(sessionId);
        if (u == null)
            return Response.status(Status.UNAUTHORIZED)
                    .entity("{'error': 'Necesitas estar logueado y ser el propietario del carrito'}").build();
        if (u.getId() != id)
            // Usamos el gestor de sesiones para comprobar que no se intenta acceder a un carrito ajeno
            // En caso de ser así, corregimos y devolvemos el carrito correcto. Podríamos devolver un error en su lugar,
            // pero creo que es más cómodo para el usuario que se le redirija a su carrito.
            id = u.getId();
        try {
            Carrito carrito = carritoService.get(id);
            String json =  new ObjectMapper().writeValueAsString(carrito);
            return Response.ok(json).build();
        } catch (Exception e) {
            return Response
                    .status(Status.NOT_FOUND)
                    .entity("{'error': 'Carrito con id '" + id + "' no encontrado.'}")
                    .build();
        }
    }


    @POST
    @Path("{idCarrito}/productos")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addProducto(@PathParam("idCarrito") Long idCarrito,
                                @CookieParam("sessionId") String sessionId,
                                Producto producto) throws Exception {
        Usuario u = SessionManager.getInstance().getUsuario(sessionId);
        if (u == null)
            return Response.status(Status.UNAUTHORIZED)
                     .entity("{'error': 'Necesitas estar logueado y ser el propietario del carrito'}").build();
        if (u.getId() != idCarrito)
            idCarrito = u.getId();  // Mismo check de arriba, redirigimos al carrito correcto
        try {
            carritoService.addProducto(idCarrito, producto);
            return Response
                    .status(Status.CREATED)
                    .header("Location", BASE_URI + idCarrito + "/productos/" + producto.getId())
                    .entity("{message: Producto añadido correctamente.}")
                    .build();
        } catch (Exception e) {
            return Response
                    .status(Status.INTERNAL_SERVER_ERROR)
                    .entity("{'error': " + e.getMessage() + "}")
                    .build();
        }
    }

    @DELETE
    @Path("{idCarrito}/productos/{idProducto}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteProducto(@PathParam("idCarrito") Long idCarrito,
                                   @CookieParam("sessionId") String sessionId,
                                   @PathParam("idProducto") Long idProducto) throws Exception {
        Usuario u = SessionManager.getInstance().getUsuario(sessionId);
        if (u == null)
            return Response.status(Status.UNAUTHORIZED)
                    .entity("{error: Necesitas estar logueado y ser el propietario del carrito}").build();
        if (u.getId() != idCarrito)
            idCarrito = u.getId();  // Mismo check de arriba, redirigimos al carrito correcto
        try {
            carritoService.deleteProducto(idCarrito, idProducto);
            return Response
                    .status(Status.OK)
                    .entity("{'message': 'Producto eliminado correctamente'.}")
                    .build();
        } catch (Exception e) {
            return Response
                    .status(Status.INTERNAL_SERVER_ERROR)
                    .entity("{'error': " + e.getMessage() + "}")
                    .build();
        }
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    // No es tanto un delete como un reset, ya que no se elimina el
    // carrito como tal de la BD, sino que se vacía de productos.
    public Response reset(@PathParam("id") Long id,
    					  @CookieParam("sessionId") String sessionId) throws Exception {
        Usuario u = SessionManager.getInstance().getUsuario(sessionId);
        if (u == null)
            return Response.status(Status.UNAUTHORIZED)
                    .entity("{error: Necesitas estar logueado y ser el propietario del carrito}").build();
        if (u.getId() != id)
            id = u.getId();  // Mismo check de arriba, redirigimos al carrito correcto
        try {
            carritoService.reset(id);
            return Response
                    .status(Status.OK)
                    .entity("{'message': 'Carrito reseteado correctamente.'}")
                    .build();
        } catch (Exception e) {
            return Response
                    .status(Status.INTERNAL_SERVER_ERROR)
                    .entity("{'error': " + e.getMessage() + "}")
                    .build();
        }
    }

}
