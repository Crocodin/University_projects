package ro.mpp.repository.ORMRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp.domain.ShowArtist;
import ro.mpp.repository.IShowArtistRepository;
import ro.mpp.utils.JPAUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ShowArtistRepoORM implements IShowArtistRepository {
    private static final Logger logger = LogManager.getLogger(ShowArtistRepoORM.class);
    private final EntityManagerFactory emf = JPAUtils.getEntityManagerFactory();

    @Override
    public List<ShowArtist> filterByDate(LocalDate date) {
        logger.info("filterByDate {}", date);
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT sa FROM ShowArtist sa WHERE sa.show.date LIKE :date", ShowArtist.class)
                    .setParameter("date", date.toString() + "%")
                    .getResultList();
        } catch (Exception e) {
            logger.error("Error while finding shows & artist date", e);
            throw new RuntimeException("Error while finding shows & artist date: " + e.getMessage());
        }
    }

    @Override
    public Optional<ShowArtist> save(ShowArtist entity) {
        return Optional.empty();
    }

    @Override
    public Optional<ShowArtist> update(ShowArtist entity) {
        return Optional.empty();
    }

    @Override
    public void delete(ShowArtist entity) {

    }

    @Override
    public Optional<ShowArtist> find(Integer integer) {
        return Optional.empty();
    }

    @Override
    public List<ShowArtist> findAll() {
        logger.debug("findAll");
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            List<ShowArtist> showArtistList = em.createQuery("SELECT sa FROM ShowArtist sa", ShowArtist.class).getResultList();
            em.getTransaction().commit();
            return showArtistList;
        }
    }
}
