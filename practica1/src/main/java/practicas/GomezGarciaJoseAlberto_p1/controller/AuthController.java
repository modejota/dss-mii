package practicas.GomezGarciaJoseAlberto_p1.controller;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import practicas.GomezGarciaJoseAlberto_p1.cookies.SessionManager;
import practicas.GomezGarciaJoseAlberto_p1.modelo.Usuario;
import practicas.GomezGarciaJoseAlberto_p1.services.UsuarioServiceImpl;

import java.util.UUID;

@Path("auth")
public class AuthController {

    private UsuarioServiceImpl usuarioService = new UsuarioServiceImpl();

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout(@CookieParam("sessionId") String sessionId) {
        NewCookie cookieSessionId = new NewCookie("sessionId", "", "/", null, null, 0, false);
        NewCookie cookieUserId = new NewCookie("userId", "", "/", null, null, 0, false);
        NewCookie cookieUserRole = new NewCookie("userRole", "", "/", null, null, 0, false);
        SessionManager.getInstance().removeSession(sessionId);
        return Response.ok().
                cookie(cookieUserRole).cookie(cookieUserId).cookie(cookieSessionId).
                build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    // Necesita enviar JSON, por lo menos, con los campos "id" o "username" y "password".
    public Response loginJSON(Usuario user) throws Exception {
        String hashedPassword = UsuarioServiceImpl.hashPassword(user.getPassword());
        if (user.getId() != null) {
            user = usuarioService.get(user.getId());
        } else if (user.getUsername() != null) {
            user = usuarioService.getWithPassword(user.getUsername());
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (user.getPassword().equals(hashedPassword)) {
            String sessionId = UUID.randomUUID().toString();
            NewCookie cookieSessionId = new NewCookie("sessionId", sessionId, "/", null, null, 1800, false);
            NewCookie cookieUserId = new NewCookie("userId", user.getId().toString(), "/", null, null, 1800, false);
            NewCookie cookieUserRole = new NewCookie("userRole", user.getRole().toString(), "/", null, null, 1800, false);
            SessionManager.getInstance().addSession(sessionId, user);
            return Response.ok().
                    cookie(cookieSessionId).cookie(cookieUserId).cookie(cookieUserRole).
                    build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
	}

}