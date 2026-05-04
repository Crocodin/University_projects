package ro.mpp.repository.ORMRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp.domain.Venue;
import ro.mpp.repository.IVenueRepository;
import ro.mpp.utils.JPAUtils;

import java.util.List;
import java.util.Optional;

public class VenueRepoORM implements IVenueRepository {
    private static final Logger logger = LogManager.getLogger(VenueRepoORM.class);
    private final EntityManagerFactory emf = JPAUtils.getEntityManagerFactory();

    @Override
    public Optional<Venue> save(Venue entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Venue> update(Venue entity) {
        return Optional.empty();
    }

    @Override
    public void delete(Venue entity) {

    }

    @Override
    public Optional<Venue> find(Integer integer) {
        logger.debug("Finding Venue by ID {}", integer);
        try (EntityManager em = emf.createEntityManager()) {
            Venue venue = em.find(Venue.class, integer);
            if  (venue != null) {
                return Optional.of(venue);
            }
            logger.warn("Venue not found {}", integer);
            return Optional.empty();
        }
    }

    @Override
    public List<Venue> findAll() {
        return List.of();
    }
}
