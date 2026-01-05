package com.ubb.facade;

import com.ubb.config.Config;
import com.ubb.domain.duck.Duck;
import com.ubb.domain.person.Person;
import com.ubb.domain.user.User;
import com.ubb.exception.EntityException;
import com.ubb.exception.userException.UserDoseNotExistException;
import com.ubb.service.DuckService;
import com.ubb.service.PersonService;
import lombok.Getter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Stream;

/**
 * Provides a unified interface for managing all users in the system.
 * <p>
 * The {@code UserFacade} class abstracts access to multiple user services,
 * such as {@link DuckService} and {@link PersonService}, allowing clients
 * to interact with different types of users through a single entry point.
 * </p>
 */
@Getter
public class UserFacade {
    /** Service responsible for managing {@link Duck} users. */
    private final DuckService duckService;

    /** Service responsible for managing {@link Person} users. */
    private final PersonService personService;

    /**
     * Constructs a new {@code UserFacade} with the specified services.
     *
     * @param duckService   the service handling {@link Duck} users
     * @param personService the service handling {@link Person} users
     */
    public UserFacade(DuckService duckService, PersonService personService) {
        this.duckService = duckService;
        this.personService = personService;
    }

    public List<User> getUsers() {
        try {
            return Stream.concat(duckService.getObjects().stream(), personService.getObjects().stream()).toList();
        } catch (EntityException e) {
            return List.of();
        }
    }

    public int size() {
        try {
            return duckService.getObjects().size() + personService.getObjects().size();
        } catch (EntityException e) {
            return -1;
        }
    }

    /**
     * Retrieves a user by their unique identifier.
     * <p>
     * The search is performed across both {@link DuckService} and {@link PersonService}.
     * </p>
     *
     * @param id the unique identifier of the user
     * @return the {@link User} with the specified ID
     * @throws UserDoseNotExistException if no user with the given ID exists
     */
    public User getUser(Long id) throws UserDoseNotExistException, SQLException {
        try {
            return personService.findId(id);
        } catch (EntityException e) {
            return duckService.findId(id);

        }
    }

    /**
     * Removes a user with the specified ID from the system.
     * <p>
     * The method determines the user type and delegates the removal
     * to the corresponding service.
     * </p>
     *
     * @param id the unique identifier of the user to remove
     * @throws UserDoseNotExistException if no user with the given ID exists
     */
    public User removeUserId(Long id) throws SQLException {
        User user = getUser(id);
        if (user instanceof Duck) duckService.removeObject((Duck) user);
        if (user instanceof Person) personService.removeObject((Person) user);
        return  user;
    }

    public User login(String username, String password) throws UserDoseNotExistException, SQLException, NoSuchAlgorithmException {
        byte[] hashBytes = MessageDigest.getInstance("MD5").digest(password.getBytes());

        // Convert to hex string
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        String hashedPassword = sb.toString();

        for (var user : personService.getObjects()) {
            if (user.getUsername().equals(username) && user.getPassword().equals(hashedPassword)) {
                return user;
            }
        }
        throw new UserDoseNotExistException(username + " doesn't exist or incorrect password");
    }

    public void updateProfilePicture(long userId, byte[] profilePicture)
            throws UserDoseNotExistException, SQLException {

        User user = this.getUser(userId);

        try (Connection connection = DriverManager.getConnection(
                Config.getProperties().getProperty("DB_URL"),
                Config.getProperties().getProperty("DB_USER"),
                Config.getProperties().getProperty("DB_PASSWORD"))) {
            String sql = "UPDATE users SET profile_picture = ? WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setBytes(1, profilePicture);
            stmt.setLong(2, userId);

            int updated = stmt.executeUpdate();
            if (updated == 0) {
                throw new SQLException("Profile picture update failed for user " + userId);
            }
        }

        user.setProfilePicture(profilePicture);
    }

}
