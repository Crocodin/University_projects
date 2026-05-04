package ro.mpp.repository.ORMRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp.domain.Artist;
import ro.mpp.repository.IArtistRepository;
import ro.mpp.utils.JPAUtils;

import java.util.List;
import java.util.Optional;

public class ArtistRepoORM implements IArtistRepository {
    private static final Logger logger = LogManager.getLogger(ArtistRepoORM.class);
    private final EntityManagerFactory emf = JPAUtils.getEntityManagerFactory();

    @Override
    public Optional<Artist> save(Artist entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Artist> update(Artist entity) {
        return Optional.empty();
    }

    @Override
    public void delete(Artist entity) {

    }

    @Override
    public Optional<Artist> find(Integer integer) {
        logger.debug("Find Artist with id: {}", integer);
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Artist artist = em.find(Artist.class, integer);
            em.getTransaction().commit();
            if (artist != null)
                return Optional.of(artist);
            return Optional.empty();
        }
    }

    @Override
    public List<Artist> findAll() {
        return List.of();
    }
}
