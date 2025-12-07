package com.ubb.domain.validator;

public class ValidatorFlock implements ValidatorStrategy {

    @Override
    public boolean validate(String input) {
        String[] parts = input.split(",");
        if (parts.length != 2) return false;
        try {
            long id = Long.parseLong(parts[0]);
            String name = parts[1];
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
}
