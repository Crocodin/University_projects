package ro.mpp.repository;

import ro.mpp.domain.Ticket;
import ro.mpp.exceptions.TicketModifier;

import java.util.Optional;

public interface TicketRepository extends Repository<Integer, Ticket> {
    public Optional<Ticket> incrementSeats(Ticket ticket, int seats) throws TicketModifier;
}
