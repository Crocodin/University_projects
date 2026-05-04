package ro.mpp.repository.ORMRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp.domain.Ticket;
import ro.mpp.exceptions.TicketModifier;
import ro.mpp.repository.IShowRepository;
import ro.mpp.repository.ITicketRepository;
import ro.mpp.utils.JPAUtils;
import ro.mpp.validator.ValidatorStrategy;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TicketRepoORM implements ITicketRepository {
    private static final Logger logger = LogManager.getLogger(TicketRepoORM.class);
    private final EntityManagerFactory emf = JPAUtils.getEntityManagerFactory();
    private final ValidatorStrategy<Ticket> ticketValidator;

    @Override
    public Optional<Ticket> incrementSeats(Ticket ticket, int seats) throws TicketModifier {
        logger.info("Changing the number of seats from {} to {} for ticket {}",
                ticket.getNumberOfSeats(), seats, ticket.getId());
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            try {
                ticket.setNumberOfSeats(seats);
                Ticket merged = em.merge(ticket);
                em.getTransaction().commit();
                return Optional.of(merged);
            } catch (Exception ex) {
                em.getTransaction().rollback();
                logger.error(ex.getMessage(), ex);
                return Optional.empty();
            }
        }
    }

    @Override
    public Optional<Ticket> save(Ticket entity) {
        logger.debug("Entering save method, validating entity");
        ticketValidator.validate(entity);
        logger.debug("Saving entity {}", entity);

        try (EntityManager em = emf.createEntityManager()) {
            try {
                em.getTransaction().begin();
                Ticket merged = em.merge(entity);
                em.getTransaction().commit();
                return Optional.of(merged);
            } catch (Exception ex) {
                em.getTransaction().rollback();
                logger.error(ex.getMessage(), ex);
                return Optional.empty();
            }
        }
    }

    @Override
    public Optional<Ticket> update(Ticket entity) {
        return Optional.empty();
    }

    @Override
    public void delete(Ticket entity) {

    }

    @Override
    public Optional<Ticket> find(Integer integer) {
        logger.debug("Entering find method with id {}", integer);
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Ticket merged = em.find(Ticket.class, integer);
            em.getTransaction().commit();
            if (merged != null)
                return Optional.of(merged);
            return Optional.empty();
        }
    }

    @Override
    public List<Ticket> findAll() {
        return List.of();
    }
}
