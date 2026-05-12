package ro.mpp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.mpp.domain.Show;
import ro.mpp.domain.ShowArtist;
import ro.mpp.domain.Ticket;
import ro.mpp.exceptions.TicketModifier;
import ro.mpp.repository.DBRepository.*;
import ro.mpp.repository.IShowArtistRepository;
import ro.mpp.repository.IShowRepository;
import ro.mpp.repository.ITicketRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FestivalService implements IFestivalService {
    private final IShowRepository showRepository;
    private final ITicketRepository ticketRepository;
    private final IShowArtistRepository showArtistRepository;

    public List<ShowArtist> findAllShows() {
        return showArtistRepository.findAll();
    }

    @Override
    public List<ShowArtist> findByDate(LocalDate date) {
        return showArtistRepository.filterByDate(date);
    }

    @Override
    public List<ShowArtist> findAll() {
        return showArtistRepository.findAll();
    }

    @Override
    public Optional<Show> findById(int id) {
        return this.showRepository.find(id);
    }

    @Override
    public Optional<Ticket> sellTicket(Show show, String buyerName, int seats) {
        Ticket ticket = show.sellTicket(buyerName, seats);
        if (show.remainingSeats() >= seats) {
            var t = ticketRepository.save(ticket);
            if (t.isPresent()) {
                show.addToSoldSeats(seats);
                showRepository.update(show);
            }
            return t;
        }
        throw new TicketModifier("Not enough seats for the ticket");
    }

    @Override
    public boolean modifyTicket(int ticketId, int seats) {
        var opT = ticketRepository.find(ticketId);
        if (opT.isEmpty()) return false;
        opT.ifPresent(ticket -> {
            if (seats < 0) throw new TicketModifier("Seats can't be negative");
            int newSeats = seats - ticket.getNumberOfSeats();

            Show show = ticket.getShow();
            if (show.remainingSeats() - newSeats >= 0) {
                ticket.setNumberOfSeats(seats);
                ticketRepository.incrementSeats(ticket, seats).ifPresent(t -> {
                    t.getShow().addToSoldSeats(newSeats); // this number can be +/-
                    showRepository.update(t.getShow());
                });
            }
        });
        return true;
    }

    @Override
    public Optional<Show> findShowById(int id) {
        return showRepository.find(id);
    }

    @Override
    public Optional<Show> saveShow(Show show) {
        return showRepository.save(show);
    }

    @Override
    public Optional<Show> updateShow(int id, Show show) {
        return showRepository.find(id)
                .flatMap(existing -> showRepository.update(show));
    }

    @Override
    public boolean deleteShow(int id) {
        return showRepository.find(id)
                .map(show -> {
                    showRepository.delete(show);
                    return true;
                })
                .orElse(false);
    }
}
