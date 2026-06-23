package ro.mpp.repository.ORMRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import ro.mpp.domain.Show;
import ro.mpp.repository.IShowRepository;
import ro.mpp.utils.JPAUtils;

import java.util.List;
import java.util.Optional;

@Repository
public class ShowRepoORM implements IShowRepository {
    private static final Logger logger = LogManager.getLogger(ShowRepoORM.class);
    private final EntityManagerFactory emf = JPAUtils.getEntityManagerFactory();

    @Override
    public Optional<Show> save(Show entity) {
        logger.debug("Saving show {}", entity);
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            try {
                em.persist(entity);
                em.flush();
                em.refresh(entity);
                em.getTransaction().commit();
                return Optional.of(entity);
            } catch (Exception e) {
                logger.error("Error saving show", e);
                em.getTransaction().rollback();
                throw e;
            }
        }
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
                logger.error("Error updating show", e);
                em.getTransaction().rollback();
                throw e;
            }
        }
    }

    @Override
    public void delete(Show entity) {
        logger.debug("Deleting show with id {}", entity.getId());
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            try {
                Show managed = em.contains(entity) ? entity : em.merge(entity);
                em.createQuery("delete from ShowArtist where show.id = :id").setParameter("id", entity.getId()).executeUpdate();
                em.remove(managed);
                em.getTransaction().commit();
            } catch (Exception e) {
                logger.error("Error deleting show", e);
                em.getTransaction().rollback();
                throw e;
            }
        }
    }

    @Override
    public Optional<Show> find(Integer id) {
        logger.debug("Finding show with id {}", id);
        try (EntityManager em = emf.createEntityManager()) {
            Show show = em.find(Show.class, id);
            if (show != null)
                return Optional.of(show);
            logger.warn("Show with id {} not found", id);
            return Optional.empty();
        }
    }

    @Override
    public List<Show> findAll() {
        logger.debug("Finding all shows");
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT s FROM Show s", Show.class).getResultList();
        }
    }
}