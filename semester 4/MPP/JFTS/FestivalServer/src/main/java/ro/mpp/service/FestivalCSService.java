package ro.mpp.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp.authenticator.IAuthenticator;
import ro.mpp.domain.Show;
import ro.mpp.domain.ShowArtist;
import ro.mpp.domain.Ticket;
import ro.mpp.domain.User;
import ro.mpp.exceptions.TicketModifier;
import ro.mpp.observer.IFestivalObserver;
import ro.mpp.observer.IFestivalService;
import ro.mpp.repository.DBRepository.*;
import ro.mpp.repository.IArtistRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class FestivalCSService implements IFestivalService {
    private final Map<String, IFestivalObserver> clients = new ConcurrentHashMap<>();

    private final ShowRepository showRepository;
    private final TicketRepository ticketRepository;
    private final ShowArtistRepository showArtistRepository;
    private final IAuthenticator authenticator;

    private static final Logger logger = LogManager.getLogger(FestivalCSService.class);

    public FestivalCSService(ShowRepository showRepository, TicketRepository ticketRepository, ShowArtistRepository showArtistRepository, IAuthenticator authenticator) {
        this.showRepository = showRepository;
        this.ticketRepository = ticketRepository;
        this.showArtistRepository = showArtistRepository;
        this.authenticator = authenticator;
    }

    @Override
    public synchronized List<ShowArtist> findByDate(LocalDate date) {
        return showArtistRepository.filterByDate(date);
    }

    @Override
    public synchronized List<ShowArtist> findAll() {
        return showArtistRepository.findAll();
    }

    @Override
    public synchronized Optional<Ticket> sellTicket(Show show, String buyerName, int seats) {
        Ticket ticket = show.sellTicket(buyerName, seats);
        if (show.remainingSeats() >= seats) {
            var t = ticketRepository.save(ticket);
            if (t.isPresent()) {
                show.addToSoldSeats(seats);
                showRepository.update(show);
                notifyAll(t.get(), true);
            }
            return t;
        }
        throw new TicketModifier("Not enough seats for the ticket");
    }

    @Override
    public synchronized boolean modifyTicket(int ticketId, int seats) {
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
            notifyAll(ticket, false);
        });
        return true;
    }

    @Override
    public synchronized void login(String username, String password, IFestivalObserver observer) {
        clients.put(username, observer);
    }

    @Override
    public synchronized void logout(String username) {
        clients.remove(username);
    }

    @Override
    public synchronized Optional<User> authenticate(String username, String password) {
        return authenticator.authenticate(username, password);
    }

    //  needed by proxy (no-op on server side, only meaningful on client)
    @Override
    public void setObserver(IFestivalObserver observer) {
    }

    private void notifyAll(Ticket ticket, boolean isSold) {
        logger.info("notifying all {} clients about the ticket {}", clients.size(), ticket);
        clients.values().forEach(observer -> {
            if (isSold) observer.ticketSold(ticket);
            else observer.ticketModified(ticket);
        });
    }
}
