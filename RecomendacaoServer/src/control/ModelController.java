/**
 * 
 */
package control;

/**
 * @author Thiago
 *
 */

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

public class ModelController implements IController {

	Session session;

	public ModelController() {
		this.session = GeneralController.getSession();
	}

	public void insert(Object object) {
		this.session.beginTransaction();
		this.session.persist(object);
		this.session.getTransaction().commit();
	}

	public void update(Object object) {
		this.session.beginTransaction();
		this.session.merge(object);
		this.session.getTransaction().commit();
	}

	public void delete(Object object) {
		this.session.beginTransaction();
		this.session.delete(object);
		this.session.getTransaction().commit();
	}

	public List<?> list(Class<?> iClass) {
		return (List<?>) this.session.createCriteria(iClass).list();
	}

	public Object getByCode(Class<?> iClass, Integer id) {
		return this.session.get(iClass, id);
	}

	/* Limpar o banco de dados */
	public void deleteAll() {

		this.session.beginTransaction();

		this.session.createSQLQuery("delete from rest").executeUpdate();

		this.session.getTransaction().commit();
	}

	public void close() {
		this.session.close();
	}

	@Override
	public List<Object> query(String sql, List<Object> parametros) {

		for (int i = 0; i < parametros.size(); i++)
			sql = sql.replace("#" + (i + 1), parametros.get(i).toString());

		Query query = session.createSQLQuery(sql);
		@SuppressWarnings("unchecked")
		List<Object> res = query.list();
		return res;
	}

	public List<Object> query(String sql) {
		Query query = session.createSQLQuery(sql);

		@SuppressWarnings("unchecked")
		List<Object> list = query.list();

		return list;
	}

	public boolean isOpen() {
		return this.session.isOpen();
	}
}