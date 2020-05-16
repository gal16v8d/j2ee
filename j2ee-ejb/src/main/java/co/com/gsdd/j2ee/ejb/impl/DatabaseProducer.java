package co.com.gsdd.j2ee.ejb.impl;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@ApplicationScoped
public class DatabaseProducer {

	private static final String UNIT_NAME = "j2eePerson";
	@Produces
	@PersistenceContext(unitName = UNIT_NAME)
	private EntityManager manager;

	@PostConstruct
	public void init() {
		checkEntityManager();
	}
	
	private void checkEntityManager() {
        log.info("EntityManager is {}", getManager());
        if (getManager() == null) {
            manager = getEmFromContext();
        }
    }
	
	private EntityManager getEmFromContext() {
        EntityManager em = null;
        try {
            em = Persistence.createEntityManagerFactory(UNIT_NAME).createEntityManager();
        } catch (Exception e) {
            log.error("Can not get EM using name", e);
        }
        return em;
    }

}
