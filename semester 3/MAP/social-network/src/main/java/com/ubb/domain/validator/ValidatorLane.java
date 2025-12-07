package com.ubb.domain.validator;

public class ValidatorLane implements ValidatorStrategy {
    @Override
    public boolean validate(String input) {
        String[] parts = input.split(",");
        if (parts.length != 2) return false;
        try {
            int id = Integer.parseInt(parts[0]);
            int distance = Integer.parseInt(parts[1]);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
}
