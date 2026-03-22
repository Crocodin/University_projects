package ro.mpp.repository.DBRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp.domain.Venue;
import ro.mpp.repository.IVenueRepository;
import ro.mpp.utils.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VenueRepository implements IVenueRepository {
    private final Database db = new Database();
    private static final Logger logger = LogManager.getLogger(VenueRepository.class);

    @Override
    public Optional<Venue> save(Venue entity) {
        logger.traceEntry("VenueRepository save {}", entity);
        String sql = "INSERT INTO venue (name, address, capacity) VALUES (?, ?, ?)";

        try {
            Connection conn = db.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, entity.getName());
                ps.setString(2, entity.getAddress());
                ps.setInt(3, entity.getCapacity());

                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        entity.setId(rs.getInt(1));
                    }
                }
                return Optional.of(entity);
            }
        }catch (SQLException e) {
            logger.error("Error while saving venue", e);
            throw new RuntimeException("Error while saving venue: " + e.getMessage());
        }
    }

    @Override
    public Optional<Venue> update(Venue entity) {
        logger.traceEntry("VenueRepository update {}", entity);
        String sql = "UPDATE venue SET name = ?, address = ?, capacity = ? WHERE id = ?";

        try {
            Connection conn = db.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, entity.getName());
                ps.setString(2, entity.getAddress());
                ps.setInt(3, entity.getCapacity());
                ps.setInt(4, entity.getId());

                ps.executeUpdate();
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            logger.error("Error while updating venue", e);
            throw new RuntimeException("Error while updating venue: " + e.getMessage());
        }
    }

    @Override
    public void delete(Venue entity) {
        logger.traceEntry("VenueRepository delete {}", entity);
        String sql = "DELETE FROM venue WHERE id = ?";

        try {
            Connection conn = db.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, entity.getId());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            logger.error("Error while deleting venue", e);
            throw new RuntimeException("Error while deleting venue: " + e.getMessage());
        }
    }

    @Override
    public Optional<Venue> find(Integer id) {
        logger.traceEntry("VenueRepository find venue with id {}", id);
        String sql = "SELECT * FROM venue WHERE id = ?";

        try {
            Connection conn = db.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(new Venue(rs));
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error while finding venue with id {}", id, e);
            throw new RuntimeException("Error while finding venue: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Venue> findAll() {
        logger.traceEntry("VenueRepository findAll");
        String sql = "SELECT * FROM venue";
        List<Venue> venues = new ArrayList<>();

        try {
            Connection conn = db.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        venues.add(new Venue(rs));
                    }
                }
                return venues;
            }
        } catch (SQLException e) {
            logger.error("Error while finding all venues", e);
            throw new RuntimeException("Error while finding all venues: " + e.getMessage());
        }
    }
}
