package ro.mpp.observer;

import ro.mpp.domain.Ticket;

public interface IFestivalObserver {
    void ticketSold(Ticket ticket);
    void ticketModified(Ticket ticket);
}
