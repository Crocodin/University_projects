package com.ubb.domain.validator;

public class ValidatorEvent implements ValidatorStrategy{
    public boolean validate(String input) {
        String[] parts = input.split(",");
        if (parts.length != 3) return false;
        try {
            long id = Long.parseLong(parts[1]);
            String name = parts[2];
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
}
