package com.ubb.domain.user;

import com.ubb.domain.Entity;
import com.ubb.domain.connection.FriendShip;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents an abstract user in the system.
 * <p>
 * A {@code User} has basic identifying information such as an ID, username,
 * email, and password, as well as a list of friends.
 * This class serves as a base for more specific user types.
 * </p>
 */
@Getter
public abstract class User extends Entity<Long> {

    /** The username chosen by the user. */
    protected final String username;

    /** The user's email address. */
    protected final String email;

    /** The user's password. */
    protected final String password;

    /**
     * Constructs a new {@code User} with the specified information.
     *
     * @param id       the unique identifier of the user
     * @param username the username of the user
     * @param email    the email address of the user
     * @param password the password of the user
     */
    public User(Long id, String username, String email, String password) {
        super(id);
        this.username = username;
        this.email = email;
        this.password = password;
    }

//    public abstract void login();
//    public abstract void logout();
//    public abstract String sendMessage();
//    public abstract void receiveMessage();

    /**
     * Defines behavior for updating the user's state, used on the Even {@code Observer Pattern).
     * <p>
     * This method must be implemented by subclasses to define
     * user-specific update logic.
     * </p>
     */
    public abstract void update();

    /**
     * Returns a string representation of the user, based on csv matrics.
     * <p>
     * The returned string contains the user's ID, username, email, and password,
     * separated by commas.
     * </p>
     *
     * @return a csv representation of this user
     */
    @Override
    public String toString() {
        return super.getId() + "," + username + "," + email + "," + password;
    }

    /**
     * Compares this user to another object for equality.
     * Two users are considered equal if they have the same ID, username, email, and password.
     *
     * @param o the object to compare with
     * @return {@code true} if the users are equal; {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(super.getId(), user.getId()) && Objects.equals(username, user.username) && Objects.equals(email, user.email) && Objects.equals(password, user.password);
    }

    /**
     * Returns the hash code for this user.
     * @return the hash code value
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.getId(), username, email, password);
    }
}
