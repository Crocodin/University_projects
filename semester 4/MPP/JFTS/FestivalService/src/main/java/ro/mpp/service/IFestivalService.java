package ro.mpp.service;

import ro.mpp.domain.Artist;
import ro.mpp.domain.Show;
import ro.mpp.domain.Ticket;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IFestivalService {
    // ---------- SHOW
    public List<Show> findByPerformerAndDate(Artist performer, LocalDateTime date);

    // ---------- TICKET
    public Optional<Ticket> sellTicket(Show show, int seats);
    public boolean modifyTicket(Show show, int seats);
}
