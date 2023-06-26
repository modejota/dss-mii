package practicas.GomezGarciaJoseAlberto_p1.services;

import practicas.GomezGarciaJoseAlberto_p1.modelo.Carrito;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import practicas.GomezGarciaJoseAlberto_p1.modelo.Producto;

public class CarritoServiceImpl implements CarritoService {

    private static final String PERSISTENCE_UNIT_NAME = "practicaDSS";
    private static EntityManagerFactory factoria;

    public CarritoServiceImpl() {
        factoria = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }

    @Override
    public void addProducto(Long idCarrito, Producto producto) throws Exception {
        EntityManager em = factoria.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Carrito carrito = em.find(Carrito.class, idCarrito);
            carrito.addProducto(producto);
            em.merge(carrito);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            tx.commit();
            em.close();
        }
    }

    @Override
    public void reset(Long id) throws Exception {
        EntityManager em = factoria.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Carrito carrito = em.find(Carrito.class, id);
            carrito.resetProductos();
            em.merge(carrito);
            tx.commit();
        } catch (Exception e) {
            throw new Exception("Producto con id '" + id + "' no existe");
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteProducto(Long idCarrito, Long idProducto) throws Exception {
        EntityManager em = factoria.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Carrito carrito = em.find(Carrito.class, idCarrito);
            carrito.deleteProducto(idProducto);
            em.merge(carrito);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            tx.commit();
            em.close();
        }
    }

    @Override
    public Carrito get(Long id) throws Exception {
        EntityManager em = factoria.createEntityManager();
        Query q = em.createQuery("SELECT t FROM Carrito t WHERE t.id = :id");
        q.setParameter("id", id);
        try {
            return (Carrito) q.getSingleResult();
        } catch (Exception e) {
            throw new Exception("Carrito con ID '" + id + "' no existe.");
        } finally {
            em.close();
        }
    }

    @Override
    public List<Carrito> getAll() {
        EntityManager em = factoria.createEntityManager();
        Query q = em.createQuery("SELECT t FROM Carrito t");
        List<Carrito> listaCarritos = q.getResultList();
        em.close();
        return listaCarritos;
    }

}
