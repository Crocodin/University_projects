package com.ubb.domain.validator;

public class ValidatorFriend implements ValidatorStrategy {
    @Override
    public boolean validate(String input) {
        String [] inputArray = input.split(",");
        if (inputArray.length != 2) return false;
        try {
            int id1 = Integer.parseInt(inputArray[0]);
            int id2 = Integer.parseInt(inputArray[1]);
        }
        catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
