package ro.mpp.validator;

import ro.mpp.domain.Ticket;
import ro.mpp.exceptions.ValidatorException;

public class TicketValidator implements ValidatorStrategy<Ticket> {
    @Override
    public void validate(Ticket object) throws ValidatorException {
        if (object == null) throw new ValidatorException("Ticket object is null");
        if (object.getNumberOfSeats() < 0)  throw new ValidatorException("Number can't be less than 0");
        if (object.getBuyerName().isEmpty()) throw new ValidatorException("Buyer name is empty");
    }
}
