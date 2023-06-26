package practicas.GomezGarciaJoseAlberto_p1.services;

import jakarta.persistence.*;
import practicas.GomezGarciaJoseAlberto_p1.modelo.Usuario;

import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;

public class UsuarioServiceImpl implements UsuarioService {

    private static final String PERSISTENCE_UNIT_NAME = "practicaDSS";
    private static EntityManagerFactory factoria;

    public UsuarioServiceImpl() {
        factoria = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }

    @Override
    public void add(Usuario usuario) throws Exception {
        EntityManager em = factoria.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        usuario.setPassword(hashPassword(usuario.getPassword()));
        Query q;
        List<Usuario> listaUsuarios;

        tx.begin();
        q = em.createQuery("SELECT u FROM Usuario u WHERE u.username = :username");
        q.setParameter("username", usuario.getUsername());
        listaUsuarios = q.getResultList();
        tx.commit();
        em.close();
        if (listaUsuarios.size() > 0) {
            throw new Exception("Usuario con username '" + usuario.getUsername() + "' ya existe.");
        }
        em = factoria.createEntityManager();
        tx = em.getTransaction();
        tx.begin();
        try {
            String insertSql = "INSERT INTO Usuario (username, password, role) VALUES (?, ?, ?)";
            Query query = em.createNativeQuery(insertSql);
            query.setParameter(1, usuario.getUsername());
            query.setParameter(2, usuario.getPassword());
            query.setParameter(3, usuario.getRole());
            query.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Error al insertar usuario: " + e.getMessage());
        } finally {
            tx.commit();
            em.close();
        }
        em = factoria.createEntityManager();
        tx = em.getTransaction();
        tx.begin();
        try {
            Usuario u = this.getByUsername(usuario.getUsername());
            String insertSql2 = "INSERT INTO Carrito (id) VALUES (?)";
            Query query2 = em.createNativeQuery(insertSql2);
            query2.setParameter(1, u.getId());
            query2.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Error al crear carrito asociado al usuario: " + e.getMessage());
        } finally {
            tx.commit();
            em.close();
        }
    }

    @Override
    public void delete(Long id) throws Exception {
        EntityManager em = factoria.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            String deleteSql = "DELETE FROM Usuario WHERE id = (?)";
            Query query = em.createNativeQuery(deleteSql);
            query.setParameter(1, id);
            query.executeUpdate();
            String deleteSql2 = "DELETE FROM Carrito WHERE id = (?)";
            Query query2 = em.createNativeQuery(deleteSql2);
            query2.setParameter(1, id);
            query2.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Error al eliminar usuario: " + e.getMessage());
        } finally {
            tx.commit();
            em.close();
        }
    }

    @Override
    public void edit(Long id, String username, String password, String role) throws Exception {
        EntityManager em = factoria.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        password = hashPassword(password);
        Query q = em.createQuery("SELECT u FROM Usuario u WHERE u.id = :id");
        q.setParameter("id", id);
        try {
            tx.begin();
            Usuario u = (Usuario) q.getSingleResult();
            if (u != null) {
                u.setPassword(password);
                u.setRole(role);
                em.persist(u);
            } else {
                this.add(new Usuario(username, password, "USER"));
            }
            tx.commit();
        } catch (Exception e) {
            throw new Exception("Error al actualizar usuario: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    @Override
    public Usuario get(Long id) throws Exception {
        EntityManager em = factoria.createEntityManager();
        Query q = em.createQuery("SELECT u FROM Usuario u WHERE u.id = :id");
        q.setParameter("id", id);
        try {
           Usuario u = (Usuario) q.getSingleResult();
           u.setPassword("");
           return u;
        } catch (NoResultException e) {
            throw new Exception("Usuario con ID '" + id + "' no existe");
        } finally {
            em.close();
        }
    }

    @Override
    public Usuario getByUsername(String username) throws Exception {
        EntityManager em = factoria.createEntityManager();
        Query q = em.createQuery("SELECT u FROM Usuario u WHERE u.username = :username");
        q.setParameter("username", username);
        try {
           Usuario u = (Usuario) q.getSingleResult();
           u.setPassword("");
           return u;
        } catch (NoResultException e) {
            throw new Exception("Usuario con username '" + username + "' no existe");
        } finally {
            em.close();
        }
    }

    @Override
    public Usuario getWithPassword(String username) throws Exception {
        EntityManager em = factoria.createEntityManager();
        Query q = em.createQuery("SELECT u FROM Usuario u WHERE u.username = :username");
        q.setParameter("username", username);
        try {
            return (Usuario) q.getSingleResult();
        } catch (NoResultException e) {
            throw new Exception("Usuario con username '" + username + "' no existe");
        } finally {
            em.close();
        }
    }

    @Override
    public List<Usuario> getAll() {
        EntityManager em = factoria.createEntityManager();
        Query q = em.createQuery("SELECT u FROM Usuario u");
        List<Usuario> listaUsuarios = q.getResultList();
        for (Usuario u : listaUsuarios) {
            u.setPassword("");
        }
        em.close();
        return listaUsuarios;
    }

    public static String hashPassword(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}

