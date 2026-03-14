package ro.mpp.repository.DBRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp.domain.Artist;
import ro.mpp.repository.IArtistRepository;
import ro.mpp.utils.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ArtistRepository implements IArtistRepository {
    private final Database db = Database.getInstance();
    private static final Logger logger = LogManager.getLogger(ArtistRepository.class);

    @Override
    public Optional<Artist> save(Artist entity) {
        logger.traceEntry("ArtistRepository save {}", entity);
        String sql = "INSERT INTO artist (name) VALUES (?)";

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getName());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                entity.setId(rs.getInt(1));
            }

            return Optional.of(entity);
        } catch (SQLException e) {
            logger.error("Error while saving artist", e);
            throw new RuntimeException("Error while saving artist: " + e.getMessage());
        }
    }

    @Override
    public Optional<Artist> update(Artist entity) {
        logger.traceEntry("ArtistRepository update {}", entity);
        String sql = "UPDATE artist SET name = ? WHERE id = ?";

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getName());
            ps.setInt(2, entity.getId());
            ps.executeUpdate();

            return Optional.of(entity);
        } catch (SQLException e) {
            logger.error("Error while updating artist", e);
            throw new RuntimeException("Error while updating artist: " + e.getMessage());
        }
    }

    @Override
    public void delete(Artist entity) {
        logger.traceEntry("ArtistRepository delete {}", entity);
        String sql = "DELETE FROM artist WHERE id = ?";

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error while deleting artist", e);
            throw new RuntimeException("Error while deleting artist: " + e.getMessage());
        }
    }

    @Override
    public Optional<Artist> find(Integer integer) {
        logger.traceEntry("ArtistRepository find artist with id {}", integer);
        String sql = "SELECT * FROM artist WHERE id = ?";

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, integer);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(new Artist(rs));
            }
        } catch (SQLException e) {
            logger.error("Error while finding artist with id {}", integer, e);
            throw new RuntimeException("Error while finding artist: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Artist> findAll() {
        logger.traceEntry("ArtistRepository findAll");
        String sql = "SELECT * FROM artist";
        List<Artist> artists = new ArrayList<>();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                artists.add(new Artist(rs));
            }

            return artists;
        } catch (SQLException e) {
            logger.error("Error while finding all artists", e);
            throw new RuntimeException("Error while finding all artists: " + e.getMessage());
        }
    }
}
