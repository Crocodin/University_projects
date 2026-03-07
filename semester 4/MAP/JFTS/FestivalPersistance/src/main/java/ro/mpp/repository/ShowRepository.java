package ro.mpp.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp.domain.Artist;
import ro.mpp.domain.Show;
import ro.mpp.domain.Venue;
import ro.mpp.utils.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShowRepository implements Repository<Integer, Show> {
    private final DataBase db = new DataBase();
    private final ArtistRepository artistRepository = new ArtistRepository();
    private final VenueRepository venueRepository = new VenueRepository();

    private static final Logger logger = LogManager.getLogger(ShowRepository.class);

    @Override
    public Optional<Show> save(Show entity) {
        logger.traceEntry("ShowRepository save {}", entity);

        String sql = "INSERT INTO show (date, title, soldSeats, venue_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setTimestamp(1, entity.getDate());
            ps.setString(2, entity.getTitle());
            ps.setInt(3, entity.getSoldSeats());
            ps.setInt(4, entity.getVenue().getId());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                entity.setId(rs.getInt(1));
            }

            logger.traceExit("ShowRepository saved show, now show_artist relation");
            String joinSql = "INSERT INTO show_artist (show_id, artist_id) VALUES (?, ?)";
            try (PreparedStatement joinPs = conn.prepareStatement(joinSql)) {
                for (Artist artist : entity.getPerformers()) {
                    joinPs.setInt(1, entity.getId());
                    joinPs.setInt(2, artist.getId());
                    joinPs.addBatch();
                }
                joinPs.executeBatch();
            }

            return Optional.of(entity);

        } catch (SQLException e) {
            logger.error("Error while saving show", e);
            throw new RuntimeException("Error while saving show: " + e.getMessage());
        }
    }

    @Override
    public Optional<Show> update(Show entity) {
        logger.traceEntry("ShowRepository update {}", entity);

        String sql = "UPDATE show SET date = ?, title = ?, soldSeats = ?, venue_id = ? WHERE id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, entity.getDate());
            ps.setString(2, entity.getTitle());
            ps.setInt(3, entity.getSoldSeats());
            ps.setInt(4, entity.getVenue().getId());
            ps.setInt(5, entity.getId());

            ps.executeUpdate();

            return Optional.of(entity);

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

        try (Connection conn = db.getConnection()) {

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

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Venue venue = venueRepository.find(rs.getInt("venue_id")).orElse(null);
                List<Artist> performers = getPerformers(conn, id);

                return Optional.of(new Show(rs, performers, venue));
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

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Integer showId = rs.getInt("id");
                Venue venue = venueRepository.find(rs.getInt("venue_id")).orElse(null);
                List<Artist> performers = getPerformers(conn, showId);

                shows.add(new Show(rs, performers, venue));
            }

            return shows;
        } catch (SQLException e) {
            logger.error("Error while finding all shows", e);
            throw new RuntimeException("Error while finding all shows: " + e.getMessage());
        }
    }

    private List<Artist> getPerformers(Connection conn, Integer showId) throws SQLException {
        String sql = "SELECT artist_id FROM show_artist WHERE show_id = ?";
        List<Artist> artists = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, showId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Integer artistId = rs.getInt("artist_id");
                artistRepository.find(artistId).ifPresent(artists::add);
            }
        }

        return artists;
    }
}