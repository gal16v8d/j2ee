package co.com.gsdd.j2ee.ejb;

import java.util.List;
import java.util.Optional;

import co.com.gsdd.j2ee.db.jpa.AbstractJPAEntity;

public interface ICrud<T extends AbstractJPAEntity, D> {

    T save(T entity);

    Optional<T> find(D id);

    List<T> findAll();

    Optional<T> update(D id, T entity);

    boolean delete(D id);
}
