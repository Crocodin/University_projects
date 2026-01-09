package com.ubb.domain.duck;

import com.ubb.domain.flock.Flock;
import com.ubb.domain.user.User;
import com.ubb.repo.file.FileRepo;
import com.ubb.service.NotificationService;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * Represents a duck.
 * <br>
 * A {@code Duck} extends {@link User} and has additional attributes such as
 * {@link RaceType type}, speed, endurance, and an optional {@link Flock} it belongs to.
 */
@Getter
@Setter
public abstract class Duck extends User {
    /** The type of race the duck can participate in.
     * -- GETTER --
     *  Returns the race type of the duck.
     */
    private final RaceType type;

    /** The duck's speed attribute.
     * -- GETTER --
     *  Returns the speed of the duck.
     */
    private final int speed;

    /** The duck's endurance attribute.
     * -- GETTER --
     *  Returns the endurance of the duck.
     */
    private final int endurance;

    /**
     * Constructs a new {@code Duck} without a flock, this is for the {@link FileRepo},
     * when {@link FileRepo#saveToFile()} and {@link FileRepo#loadFromFile()} we don't save the {@link Flock}.
     *
     * @param id        the unique identifier of the duck
     * @param username  the username of the duck (as a user)
     * @param email     the email address of the duck
     * @param password  the password of the duck
     * @param type      the race type of the duck
     * @param speed     the duck's speed
     * @param endurance the duck's endurance
     */
    public Duck(Long id, String username, String email, String password,
                RaceType type, int speed, int endurance) {
        super(id, username, email, password);
        this.type = type;
        this.speed = speed;
        this.endurance = endurance;
    }

    public Duck(Long id, String username, String email, String password, byte[] profilePicture,
                RaceType type, int speed, int endurance) {
        super(id, username, email, password, profilePicture);
        this.type = type;
        this.speed = speed;
        this.endurance = endurance;
    }

    /**
     * Returns a string representation of the duck, including all relevant attributes.
     * @return a comma-separated string of the duck's information
     */
    @Override
    public String toString() {
        return super.toString() + "," + type + "," + speed + "," + endurance;
    }

    /**
     * Creates a {@code Duck} object from a comma-separated string.
     * <br>
     * Expected format:
     * <pre>
     * id,username,email,password,type,speed,endurance
     * </pre>
     *
     * @param line the string to parse
     * @return a {@code Duck} instance, or {@code null} if the format is invalid
     */
    public static Duck fromString(String line) {
        String[] parts = line.split(",");
        if (parts.length != 7) return null;
        return switch (RaceType.valueOf(parts[4])) {
            case FLYING -> new FlyingDuck(
                    Long.parseLong(parts[0]),
                    parts[1], parts[2], parts[3],
                    RaceType.FLYING,
                    Integer.parseInt(parts[5]),
                    Integer.parseInt(parts[6])
            );
            case SWIMMING -> new SwimingDuck(
                    Long.parseLong(parts[0]),
                    parts[1], parts[2], parts[3],
                    RaceType.SWIMMING,
                    Integer.parseInt(parts[5]),
                    Integer.parseInt(parts[6])
            );
            case FLYING_AND_SWIMMING -> new FlyingAndSwimmingDuck(
                    Long.parseLong(parts[0]),
                    parts[1], parts[2], parts[3],
                    RaceType.FLYING_AND_SWIMMING,
                    Integer.parseInt(parts[5]),
                    Integer.parseInt(parts[6])
            );
            default -> null;
        };
    }

    /**
     * Compares this duck to another object for equality.
     * Two ducks are equal if all attributes (including inherited ones) match.
     *
     * @param o the object to compare
     * @return {@code true} if equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Duck duck = (Duck) o;
        return speed == duck.speed && endurance == duck.endurance && type == duck.type;
    }

    /**
     * Returns the hash code for this duck.
     * @return the hash code value
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type, speed, endurance);
    }

    public static String sqlInsertQuery() {
        return "INSERT INTO duck(username, email, password, type, speed, endurance) VALUES (?, ?, ?, ?, ?, ?)";
    }
}
