package ro.mpp.repository.DBRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp.domain.Show;
import ro.mpp.domain.Venue;
import ro.mpp.repository.IShowRepository;
import ro.mpp.utils.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShowRepository implements IShowRepository {
    private final Database db;
    private final VenueRepository venueRepository;

    private static final Logger logger = LogManager.getLogger(ShowRepository.class);

    public ShowRepository(Database database, VenueRepository venueRepository) {
        this.db = database;
        this.venueRepository = venueRepository;
    }

    @Override
    public Optional<Show> save(Show entity) {
        logger.traceEntry("ShowRepository save {}", entity);
        String sql = "INSERT INTO show (date, title, soldSeats, venue_id) VALUES (?, ?, ?, ?)";

        try {
            Connection conn = db.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, entity.getDate());
                ps.setString(2, entity.getTitle());
                ps.setInt(3, entity.getSoldSeats());
                ps.setInt(4, entity.getVenue().getId());

                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        entity.setId(rs.getInt(1));
                    }
                }
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            logger.error("Error while saving show", e);
            throw new RuntimeException("Error while saving show: " + e.getMessage());
        }
    }

    @Override
    public Optional<Show> update(Show entity) {
        logger.traceEntry("ShowRepository update {}", entity);
        String sql = "UPDATE show SET date = ?, title = ?, sold_seats = ?, venue_id = ? WHERE id = ?";

        try {
            Connection conn = db.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, entity.getDate());
                ps.setString(2, entity.getTitle());
                ps.setInt(3, entity.getSoldSeats());
                ps.setInt(4, entity.getVenue().getId());
                ps.setInt(5, entity.getId());

                ps.executeUpdate();

                return Optional.of(entity);

            }
        } catch (SQLException e) {
            logger.error("Error while updating show", e);
            throw new RuntimeException("Error while updating show: " + e.getMessage());
        }
    }

    @Override
    public void delete(Show entity) {
        logger.traceEntry("ShowRepository delete {}", entity);

        String deleteJoin = "DELETE FROM show_artist WHERE show_id = ?";
        String deleteShow = "DELETE FROM show WHERE id = ?";

        try {
            Connection conn = db.getConnection();
            logger.info("ShowRepository deleting from join table for {}", entity);
            try (PreparedStatement ps = conn.prepareStatement(deleteJoin)) {
                ps.setInt(1, entity.getId());
                ps.executeUpdate();
            }

            logger.info("ShowRepository deleting {}", entity);
            try (PreparedStatement ps = conn.prepareStatement(deleteShow)) {
                ps.setInt(1, entity.getId());
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            logger.error("Error while deleting show", e);
            throw new RuntimeException("Error while deleting show: " + e.getMessage());
        }
    }

    @Override
    public Optional<Show> find(Integer id) {
        logger.traceEntry("ShowRepository find show with id {}", id);
        String sql = "SELECT * FROM show WHERE id = ?";

        try {
            Connection conn = db.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Venue venue = venueRepository.find(rs.getInt("venue_id")).orElse(null);
                        return Optional.of(new Show(rs, venue));
                    }
                }

            }
        } catch (SQLException e) {
            logger.error("Error while finding show with id {}", id, e);
            throw new RuntimeException("Error while finding show: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Show> findAll() {
        logger.traceEntry("ShowRepository findAll");

        String sql = "SELECT * FROM show";
        List<Show> shows = new ArrayList<>();

        try {
            Connection conn = db.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Integer showId = rs.getInt("id");
                        Venue venue = venueRepository.find(rs.getInt("venue_id")).orElse(null);
                        shows.add(new Show(rs, venue));
                    }
                }

                return shows;
            }
        } catch (SQLException e) {
            logger.error("Error while finding all shows", e);
            throw new RuntimeException("Error while finding all shows: " + e.getMessage());
        }
    }
}