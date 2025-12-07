package com.ubb.domain.validator;

/**
 * Provides static method for validating input strings representing an object
 */
public interface ValidatorStrategy {
    /**
     * validates a string that represents an {@code Object}
     * @param input the string to be validated
     * @return true if the string represent an object, false if not
     */
    public boolean validate(String input);
}
