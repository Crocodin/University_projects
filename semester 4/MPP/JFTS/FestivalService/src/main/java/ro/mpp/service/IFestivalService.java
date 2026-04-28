package ro.mpp.service;

import ro.mpp.domain.Artist;
import ro.mpp.domain.Show;
import ro.mpp.domain.ShowArtist;
import ro.mpp.domain.Ticket;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IFestivalService {
    // ---------- SHOW-ARTIST
    public List<ShowArtist> findByDate(LocalDate date);
    public List<ShowArtist> findAll();
    public Optional<Show> findById(int id);

    // ---------- TICKET
    public Optional<Ticket> sellTicket(Show show, String buyerName, int seats);
    public boolean modifyTicket(int ticketId, int seats);
}
