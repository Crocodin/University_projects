package ro.mpp.repository.DBRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp.domain.Show;
import ro.mpp.domain.Ticket;
import ro.mpp.exceptions.TicketModifier;
import ro.mpp.exceptions.ValidatorException;
import ro.mpp.repository.ITicketRepository;
import ro.mpp.utils.Database;
import ro.mpp.validator.ValidatorStrategy;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TicketRepository implements ITicketRepository {
    private final Database db;
    private final ShowRepository showRepository;
    private final ValidatorStrategy<Ticket> ticketValidator;
    private static final Logger logger = LogManager.getLogger(TicketRepository.class);

    public TicketRepository(Database db, ShowRepository showRepository, ValidatorStrategy<Ticket> ticketValidator) {
        this.db = db;
        this.showRepository = showRepository;
        this.ticketValidator = ticketValidator;
    }

    @Override
    public Optional<Ticket> incrementSeats(Ticket ticket, int seats) throws TicketModifier {
        logger.info("Increment seats for ticket {} from {} to {}", ticket.getId(), ticket.getNumberOfSeats(), seats);

        String sql = "UPDATE ticket SET number_of_seats = ? WHERE id = ?";
        try {
            Connection conn = db.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, seats);
                ps.setInt(2, ticket.getId());
                ps.executeUpdate();
            }

            return Optional.of(ticket);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Ticket> save(Ticket entity) throws ValidatorException {
        logger.info("Validating ticket {}", entity);
        ticketValidator.validate(entity);
        logger.traceEntry("TicketRepository save {}", entity);
        String sql = "INSERT INTO ticket (buyer_name, number_of_seats, purchase_date, show_id) VALUES (?, ?, ?, ?)";

        try {
            Connection conn = db.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, entity.getBuyerName());
                ps.setInt(2, entity.getNumberOfSeats());
                ps.setTimestamp(3, entity.getPurchaseDate());
                ps.setInt(4, entity.getShow().getId());

                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        entity.setId(rs.getInt(1));
                    }
                }
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            logger.error("Error while saving ticket", e);
            throw new RuntimeException("Error while saving ticket: " + e.getMessage());
        }
    }

    @Override
    public Optional<Ticket> update(Ticket entity) {
        logger.traceEntry("TicketRepository update {}", entity);
        String sql = "UPDATE ticket SET buyer_name = ?, number_of_seats = ?, purchase_date = ? WHERE id = ?";

        try {
            Connection conn = db.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, entity.getBuyerName());
                ps.setInt(2, entity.getNumberOfSeats());
                ps.setTimestamp(3, entity.getPurchaseDate());
                ps.setInt(4, entity.getShow().getId());
                ps.setInt(5, entity.getId());

                ps.executeUpdate();

                return Optional.of(entity);
            }
        } catch (SQLException e) {
            logger.error("Error while updating ticket", e);
            throw new RuntimeException("Error while updating ticket: " + e.getMessage());
        }
    }

    @Override
    public void delete(Ticket entity) {
        logger.traceEntry("TicketRepository delete {}", entity);
        String sql = "DELETE FROM ticket WHERE id = ?";

        try {
            Connection conn = db.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, entity.getId());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            logger.error("Error while deleting ticket", e);
            throw new RuntimeException("Error while deleting ticket: " + e.getMessage());
        }
    }

    @Override
    public Optional<Ticket> find(Integer id) {
        logger.traceEntry("TicketRepository find ticket with id {}", id);
        String sql = "SELECT * FROM ticket WHERE id = ?";

        try {
            Connection conn = db.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Show show = showRepository
                                .find(rs.getInt("show_id"))
                                .orElseThrow(() -> {
                                    logger.error("Error while finding show for ticket with id {}", id);
                                    return new RuntimeException("Show not found");
                                });

                        return Optional.of(new Ticket(rs, show));
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error while finding ticket with id {}", id, e);
            throw new RuntimeException("Error while finding ticket: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Ticket> findAll() {
        logger.traceEntry("TicketRepository findAll");
        String sql = "SELECT * FROM ticket";
        List<Ticket> tickets = new ArrayList<>();

        try {
            Connection conn = db.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Integer id = rs.getInt("id");
                    Show show = showRepository
                            .find(id)
                            .orElseThrow(() -> {
                                logger.error("Error while finding show with id {}", id);
                                return new RuntimeException("Show not found");
                            });

                    tickets.add(new Ticket(rs, show));
                }
                return tickets;
            }
        } catch (SQLException e) {
            logger.error("Error while finding all tickets", e);
            throw new RuntimeException("Error while finding all tickets: " + e.getMessage());
        }
    }
}
