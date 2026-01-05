package com.ubb.domain.person;

import com.ubb.domain.user.User;
import lombok.Getter;

import java.util.Objects;

/**
 * Represents a person in the system, extending the base {@link User} class.
 * <p>
 * A {@code Person} has personal details such as first name, last name, occupation,
 * and an empathy score in addition to the common {@code User} attributes.
 * </p>
 */
@Getter
public class Person extends User {
    /** The person's first name. */
    private final String firstName;

    /** The person's last name. */
    private final String lastName;

    /** The person's occupation or job title. */
    private final String occupation;

    /** A numerical representation of the person's empathy level. */
    private final int empathyScore;

    /**
     * Constructs a new {@code Person} with the specified attributes.
     *
     * @param id            the unique identifier of the user
     * @param username      the username of the user
     * @param email         the email address of the user
     * @param password      the user's password
     * @param firstName     the first name of the person
     * @param lastName      the last name of the person
     * @param occupation    the occupation of the person
     * @param empathyScore  the empathy score of the person
     */
    public Person(Long id, String username, String email, String password,
                  String firstName, String lastName, String occupation, int empathyScore) {
        super(id, username, email, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.occupation = occupation;
        this.empathyScore = empathyScore;
    }

    /**
     * Returns a csv representation of the person,
     * including inherited {@code User} information and all person-specific fields.
     *
     * @return a comma-separated string representation of the person
     */
    @Override
    public String toString() {
        return super.toString() + "," + firstName + "," + lastName + "," + occupation + "," + empathyScore;
    }


    /**
     * Creates a {@code Person} object from a comma-separated string.

     * @param line the string to parse
     * @return a {@code Person} instance, or {@code null} if the format is invalid
     */
    public static Person fromString(String line) {
        String[] parts = line.split(",");
        if (parts.length != 8) return null;
        return new Person(
                Long.parseLong(parts[0]),
                parts[1], parts[2], parts[3], parts[4], parts[5], parts[6],
                Integer.parseInt(parts[7])
        );
    }

    /**
     * Compares this person to another object for equality
     * Two persons are equal if all their fields (including inherited ones) are identical
     *
     * @param o the object to compare with
     * @return {@code true} if the persons are equal; {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Person person = (Person) o;
        return empathyScore == person.empathyScore && Objects.equals(firstName, person.firstName) && Objects.equals(lastName, person.lastName) && Objects.equals(occupation, person.occupation);
    }

    /**
     * Returns the hash code for this person
     * @return the hash code value
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), firstName, lastName, occupation, empathyScore);
    }
}
