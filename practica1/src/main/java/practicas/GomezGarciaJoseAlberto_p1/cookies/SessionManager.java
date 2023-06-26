package practicas.GomezGarciaJoseAlberto_p1.cookies;

import practicas.GomezGarciaJoseAlberto_p1.modelo.Usuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SessionManager {

	// Instancia del singleton
	private static SessionManager instance;

	// Mapa de sesiones. Búsqueda en O(1) por sessionId
	private HashMap<String, Usuario> map;

	// Constructor privado
	private SessionManager() {
	  this.map = new HashMap<String, Usuario>();
	}
	// Método para obtener la instancia del singleton
	public static SessionManager getInstance() {
	  if (instance == null) {
		  instance = new SessionManager();
	  }
	  return instance;
	}

	// Añadir una sesión
	public void addSession(String sessionId, Usuario user) {
	  this.map.put(sessionId, user);
	}

	// Eliminar una sesión
	public void removeSession(String sessionId) {
	  this.map.remove(sessionId);
	}

	// Obtener un usuario a partir de una sesión
	public Usuario getUsuario(String sessionId) {
	  return this.map.get(sessionId);
	}

}

// Check cookie in JSP
// <c:if test="${cookie.containsKey('sessionId')}">