package com.ubb.domain.validator;

/**
 * Context class that uses a ValidatorStrategy.
 */
public class ValidatorContext {

    private ValidatorStrategy strategy;

    public ValidatorContext(ValidatorStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(ValidatorStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * you really can't figure out what this dose?
     * @param input string to validate
     * @return true if it validated; false if not
     */
    public boolean validate(String input) {
        return strategy.validate(input);
    }
}
