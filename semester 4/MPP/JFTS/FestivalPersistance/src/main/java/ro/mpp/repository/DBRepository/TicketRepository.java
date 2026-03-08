package ro.mpp.repository.DBRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp.domain.Show;
import ro.mpp.domain.Ticket;
import ro.mpp.repository.Repository;
import ro.mpp.utils.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TicketRepository implements Repository<Integer, Ticket> {
    private final DataBase db = new DataBase();
    private final ShowRepository showRepository = new ShowRepository();

    private static final Logger logger = LogManager.getLogger(TicketRepository.class);

    @Override
    public Optional<Ticket> save(Ticket entity) {
        logger.traceEntry("TicketRepository save {}", entity);

        String sql = "INSERT INTO ticket (buyer_name, number_of_seats, purchase_date, show_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, entity.getBuyerName());
            ps.setInt(2, entity.getNumberOfSeats());
            ps.setTimestamp(3, entity.getPurchaseDate());
            ps.setInt(4, entity.getShow().getId());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                entity.setId(rs.getInt(1));
            }

            return Optional.of(entity);
        } catch (SQLException e) {
            logger.error("Error while saving ticket", e);
            throw new RuntimeException("Error while saving ticket: " + e.getMessage());
        }
    }

    @Override
    public Optional<Ticket> update(Ticket entity) {
        logger.traceEntry("TicketRepository update {}", entity);

        String sql = "UPDATE ticket SET buyer_name = ?, number_of_seats = ?, purchase_date = ?, show_id = ? WHERE id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, entity.getBuyerName());
            ps.setInt(2, entity.getNumberOfSeats());
            ps.setTimestamp(3, entity.getPurchaseDate());
            ps.setInt(4, entity.getShow().getId());
            ps.setInt(5, entity.getId());

            ps.executeUpdate();

            return Optional.of(entity);

        } catch (SQLException e) {
            logger.error("Error while updating ticket", e);
            throw new RuntimeException("Error while updating ticket: " + e.getMessage());
        }
    }

    @Override
    public void delete(Ticket entity) {
        logger.traceEntry("TicketRepository delete {}", entity);

        String sql = "DELETE FROM ticket WHERE id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error while deleting ticket", e);
            throw new RuntimeException("Error while deleting ticket: " + e.getMessage());
        }
    }

    @Override
    public Optional<Ticket> find(Integer id) {
        logger.traceEntry("TicketRepository find ticket with id {}", id);

        String sql = "SELECT * FROM ticket WHERE id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Show show = showRepository
                        .find(rs.getInt("show_id"))
                        .orElseThrow(() -> {
                            logger.error("Error while finding show for ticket with id {}", id);
                            return new RuntimeException("Show not found");
                        });

                return Optional.of(new Ticket(rs, show));
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

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

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

        } catch (SQLException e) {
            logger.error("Error while finding all tickets", e);
            throw new RuntimeException("Error while finding all tickets: " + e.getMessage());
        }
    }
}
