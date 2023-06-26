package practicas.GomezGarciaJoseAlberto_p1.services;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import practicas.GomezGarciaJoseAlberto_p1.modelo.Producto;

public class ProductoServiceImpl implements ProductoService {

	private static final String PERSISTENCE_UNIT_NAME = "practicaDSS";
	private static EntityManagerFactory factoria;

	public ProductoServiceImpl() {
		factoria = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	}

	@Override
	public void add(Producto producto) throws Exception {
		EntityManager em = factoria.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		Query q;
		List<Producto> listaProductos;
		
		tx.begin();
		q = em.createQuery("SELECT t FROM Producto t WHERE t.id = :id");
		q.setParameter("id", producto.getId());
		listaProductos = q.getResultList();
		tx.commit();
		em.close();
		if (listaProductos.size() > 0) {
			throw new Exception("Producto con ID '" + producto.getId() + "' ya existe.");
		}
		em = factoria.createEntityManager();
		tx = em.getTransaction();
		tx.begin();
		q = em.createQuery("SELECT t FROM Producto t WHERE t.nombre = :nombre");
		q.setParameter("nombre", producto.getNombre());
		listaProductos = q.getResultList();
		tx.commit();
		em.close();
		if (listaProductos.size() > 0) {
			throw new Exception("Producto con nombre '" + producto.getNombre() + "' ya existe");
		}
		em = factoria.createEntityManager();
		tx = em.getTransaction();
		tx.begin();
		try {
			String insertSql = "INSERT INTO Producto (id, nombre, precio, imagen) VALUES (?, ?, ?, ?)";
			Query query = em.createNativeQuery(insertSql);
			query.setParameter(1, producto.getId());
			query.setParameter(2, producto.getNombre());
			query.setParameter(3, producto.getPrecio());
			query.setParameter(4, producto.getImagen());
			query.executeUpdate();
		} catch (Exception e) {
			throw new Exception("{error: " + e.getMessage() + "}");
		} finally {
			tx.commit();
			em.close();
		}
	}

	@Override
	public void delete(Long id) throws Exception {
		EntityManager em = factoria.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Producto producto = em.find(Producto.class, id);
			em.remove(producto);
			tx.commit();
		} catch (Exception e) {
			throw new Exception("Producto con id '" + id + "' no existe");
		} finally {
			em.close();
		}
	}

	@Override
	public void edit(Long id, Producto producto) throws Exception {
		EntityManager em = factoria.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		Query q = em.createQuery("SELECT t FROM Producto t WHERE t.nombre = :nombre AND t.id != :id");
		q.setParameter("nombre", producto.getNombre());
		q.setParameter("id", id);
		List<Producto> listaProductos = q.getResultList();
		if (listaProductos.size() > 0) {
			Producto p = listaProductos.get(0);
			if (!Objects.equals(p.getId(), id)) {
				throw new Exception("Ya existe un producto distinto con ese nombre");
			}
		}
		try {
			tx.begin();
			Producto p = em.find(Producto.class, id);
			if (p != null) {
				p.setNombre(producto.getNombre());
				p.setPrecio(producto.getPrecio());
				p.setImagen(producto.getImagen());
				em.persist(p);
			} else {
				this.add(new Producto(id, producto.getNombre(), producto.getPrecio(), producto.getImagen()));
			}
			tx.commit();
		} catch (Exception e) {
			throw new Exception("{error:" + e.getMessage() + "}");
		} finally {
			em.close();
		}
	}

	@Override
	public Producto get(Long id) throws Exception {
		EntityManager em = factoria.createEntityManager();
		Query q = em.createQuery("SELECT t FROM Producto t WHERE t.id = :id");
		q.setParameter("id", id);
		try {
			return (Producto) q.getSingleResult();
		} catch (Exception e) {
			throw new Exception("Producto con ID '" + id + "' no existe");
		} finally {
			em.close();
		}
	}

	@Override
	public List<Producto> getAll() {
		EntityManager em = factoria.createEntityManager();
		Query q = em.createQuery("SELECT t FROM Producto t");
		List<Producto> listaProductos = q.getResultList();
		em.close();
		return listaProductos;
	}
	
}
