package ro.mpp.validator;

import ro.mpp.exceptions.ValidatorException;

public interface ValidatorStrategy<T> {
    public void validate(T object) throws ValidatorException;
}
