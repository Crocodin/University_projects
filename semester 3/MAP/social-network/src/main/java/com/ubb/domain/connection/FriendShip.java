package com.ubb.domain.connection;

import com.ubb.domain.Entity;
import com.ubb.domain.user.User;
import lombok.Getter;

import java.util.Map;

/**
 * Represents a friendship between two users.
 * This class stores references to both users involved in the friendship.
 */
@Getter
public class FriendShip extends Entity<Long> {
    /** The first user in the friendship. */
    private final User userOne;
    /** The second user in the friendship. */
    private final User userTwo;

    /**
     * Construct a new friendship between two users
     *
     * @param userOne the first user
     * @param userTwo the second user
     */
    public FriendShip(Long id, User userOne, User userTwo) {
        super(id);
        this.userOne = userOne;
        this.userTwo = userTwo;
    }

    @Override
    public String toString() {
        return super.getId() + "," + userOne.getId() + "," + userTwo.getId();
    }

    public static FriendShip fromString(String str, Map<Long, User> allUsers) {
        String[] parts = str.split(",");
        Long id = Long.parseLong(parts[0]);
        User u1 = allUsers.get(Long.parseLong(parts[1]));
        User u2 = allUsers.get(Long.parseLong(parts[2]));
        return new FriendShip(id, u1, u2);
    }
}
