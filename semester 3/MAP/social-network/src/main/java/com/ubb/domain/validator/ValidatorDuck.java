package com.ubb.domain.validator;

import com.ubb.domain.duck.RaceType;

/**
 * Provides static method for validating input strings representing {@link com.ubb.domain.duck.Duck} objects.
 */
public class ValidatorDuck implements ValidatorStrategy {
    /**
     * Validates a string representing a {@link com.ubb.domain.duck.Duck}.
     * <br> The expected format is:
     * <pre>
     * id,username,email,password,type,speed,endurance
     * </pre>
     * where {@code id} is a long, {@code type} is a valid {@link RaceType},
     * and {@code speed} and {@code endurance} are integers.
     *
     * @param input the string to validate
     * @return {@code true} if the string is valid, {@code false} otherwise
     */
    public boolean validate(String input) {
        String[] parts = input.split(",");
        if (parts.length != 7) return false;
        try {
            RaceType.valueOf(parts[4]);
            Integer.parseInt(parts[5]);
            Integer.parseInt(parts[6]);
        }
        catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}
