package co.com.gsdd.j2ee.ejb.impl;

import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;

import co.com.gsdd.j2ee.db.jpa.AbstractJPAEntity;
import co.com.gsdd.j2ee.ejb.ICrud;
import lombok.Getter;

@Getter
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public abstract class AbstractCrudEJB<T extends AbstractJPAEntity, D> implements ICrud<T, D> {

	@Inject
	private EntityManager manager;
	private final Class<T> clazz;

	public AbstractCrudEJB(Class<T> clazz) {
		this.clazz = clazz;
	}
	
	@Override
	public T save(T entity) {
		getManager().persist(entity);
		getManager().flush();
		return entity;
	}
	
	@Override
	public T find(D id) {
		return getManager().find(getClazz(), id);
	}

	@Override
	public List<T> findAll() {
		final CriteriaQuery<T> criteriaQuery = getManager().getCriteriaBuilder().createQuery(getClazz());
		criteriaQuery.select(criteriaQuery.from(getClazz()));
		return getManager().createQuery(criteriaQuery).getResultList();
	}

	public abstract T update(D id, T entity);

	@Override
	public boolean delete(D id) {
		boolean success = false;
		T entity = find(id);
		if (entity != null) {
			getManager().remove(entity);
			getManager().flush();
			success = true;
		}
		return success;
	}

}
