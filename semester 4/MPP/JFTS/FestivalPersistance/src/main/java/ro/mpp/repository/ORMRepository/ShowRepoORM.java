package ro.mpp.repository.ORMRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp.domain.Show;
import ro.mpp.repository.IShowRepository;
import ro.mpp.utils.JPAUtils;

import java.util.List;
import java.util.Optional;

public class ShowRepoORM implements IShowRepository {
    private static final Logger logger = LogManager.getLogger(ShowRepoORM.class);
    private final EntityManagerFactory emf = JPAUtils.getEntityManagerFactory();

    @Override
    public Optional<Show> save(Show entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Show> update(Show entity) {
        logger.debug("Updating show with id {}", entity.getId());
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            try {
                Show merged = em.merge(entity);
                em.getTransaction().commit();
                return Optional.of(merged);
            } catch (Exception e) {
                logger.error(e);
                em.getTransaction().rollback();
                throw e;
            }
        }
    }

    @Override
    public void delete(Show entity) {

    }

    @Override
    public Optional<Show> find(Integer integer) {
        logger.debug("Finding show with id {}", integer);
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Show show = em.find(Show.class, integer);
            em.getTransaction().commit();
            if (show != null)
                return Optional.of(show);

            logger.warn("Show with id {} not found", integer);
            return Optional.empty();
        }
    }

    @Override
    public List<Show> findAll() {
        return List.of();
    }
}
