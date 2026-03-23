package ro.mpp.repository.DBRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp.domain.User;
import ro.mpp.repository.IUserRepository;
import ro.mpp.utils.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserRepository implements IUserRepository {
    private final Database db;
    private static final Logger logger = LogManager.getLogger(UserRepository.class);

    public UserRepository(Database database) {
        this.db = database;
    }

    @Override
    public Optional<User> findByUsernameAndPassword(String username, String password) {
        logger.info("Finding user {} with password {}", username, password);
        String sql = "SELECT * FROM user WHERE username = ? AND password = ?";

        try {
            Connection conn = db.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(new User(rs));
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error in UserRepo::findByUsernameAndPassword", e);
            throw new RuntimeException("Error in UserRepo::findByUsernameAndPassword", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> save(User entity) {
        return Optional.empty();
    }

    @Override
    public Optional<User> update(User entity) {
        return Optional.empty();
    }

    @Override
    public void delete(User entity) {

    }

    @Override
    public Optional<User> find(Integer integer) {
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }
}
