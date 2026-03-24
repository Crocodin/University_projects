package ro.mpp.repository.DBRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp.domain.ShowArtist;
import ro.mpp.repository.IArtistRepository;
import ro.mpp.repository.IShowArtistRepository;
import ro.mpp.repository.IShowRepository;
import ro.mpp.utils.Database;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShowArtistRepository implements IShowArtistRepository {
    private final Database db;
    private final IArtistRepository artistRepository;
    private final IShowRepository showRepository;
    private static final Logger logger = LogManager.getLogger(ShowArtistRepository.class);

    public ShowArtistRepository(Database database, IArtistRepository artistRepository, IShowRepository showRepository) {
        this.db = database;
        this.artistRepository = artistRepository;
        this.showRepository = showRepository;
    }

    @Override
    public List<ShowArtist> filterByDate(LocalDate date) {
        logger.info("filterByDate {}", date);
        String sql = "SELECT SA.* FROM show_artist SA JOIN show S ON SA.show_id = S.id WHERE DATE(S.date) = ?";
        List<ShowArtist> showArtists = new ArrayList<>();

        try {
            Connection conn = db.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, date.toString());

                try (ResultSet rs = ps.executeQuery()) {
                    return getShowArtistsFromResultSet(showArtists, rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Error while finding shows & artist date", e);
            throw new RuntimeException("Error while finding shows & artist date: " + e.getMessage());
        }
    }

    @Override
    public Optional<ShowArtist> save(ShowArtist entity) {
        logger.info("save {}", entity);
        String sql = "INSERT INTO show_artist (show_id, artist_id) VALUES (?, ?)";

        try {
            Connection conn = db.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, entity.getShow().getId());
                ps.setInt(2, entity.getArtist().getId());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            logger.error("Error while finding all shows", e);
            throw new RuntimeException("Error while finding all shows: " + e.getMessage());
        }
        return Optional.of(entity);
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
        logger.info("findAll");
        String sql = "SELECT * FROM show_artist";
        List<ShowArtist> showArtists = new ArrayList<>();

        try {
            Connection conn = db.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
                return getShowArtistsFromResultSet(showArtists, rs);
            }

        } catch (SQLException e) {
            logger.error("Error while finding all shows", e);
            throw new RuntimeException("Error while finding all shows: " + e.getMessage());
        }
    }

    private List<ShowArtist> getShowArtistsFromResultSet(List<ShowArtist> showArtists, ResultSet rs) throws SQLException {
        while (rs.next()) {
            var show = showRepository.find(rs.getInt("show_id"));
            var artist = artistRepository.find(rs.getInt("artist_id"));
            if  (show.isEmpty() || artist.isEmpty()) {
                logger.info("show or artist is empty for show_id {}, and artist_id {}", rs.getInt("show_id"), rs.getInt("artist_id"));
            } else {
                showArtists.add(new ShowArtist(show.get(), artist.get()));
            }
        }
        return showArtists;
    }
}
