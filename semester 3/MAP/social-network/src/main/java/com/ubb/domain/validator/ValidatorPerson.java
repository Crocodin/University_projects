package com.ubb.domain.validator;

/**
 * Provides static method for validating input strings representing
 * {@link com.ubb.domain.person.Person} objects.
 */
public class ValidatorPerson implements ValidatorStrategy {
    /**
     * Validates a string representing a {@link com.ubb.domain.person.Person}.
     * <br>
     * The expected format is:
     * <pre>
     * id,username,email,password,firstName,lastName,occupation,empathyScore
     * </pre>
     * where {@code id} and {@code empathyScore} are numeric fields.
     *
     * @param input the string to validate
     * @return {@code true} if the string is valid, {@code false} otherwise
     */
    public boolean validate(String input) {
        String[] parts = input.split(",");
        if (parts.length != 8) return false;
        try {
            Integer.parseInt(parts[7]);
        }
        catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}
