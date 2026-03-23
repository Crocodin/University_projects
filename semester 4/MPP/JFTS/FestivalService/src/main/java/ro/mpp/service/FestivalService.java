package ro.mpp.service;

import ro.mpp.domain.Artist;
import ro.mpp.domain.Show;
import ro.mpp.domain.Ticket;
import ro.mpp.repository.DBRepository.ArtistRepository;
import ro.mpp.repository.DBRepository.ShowRepository;
import ro.mpp.repository.DBRepository.TicketRepository;
import ro.mpp.repository.DBRepository.VenueRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class FestivalService implements IFestivalService {
    private final ArtistRepository artistRepository;
    private final VenueRepository venueRepository;
    private final ShowRepository showRepository;
    private final TicketRepository ticketRepository;

    public FestivalService(ArtistRepository artistRepository, VenueRepository venueRepository, ShowRepository showRepository, TicketRepository ticketRepository) {
        this.artistRepository = artistRepository;
        this.venueRepository = venueRepository;
        this.showRepository = showRepository;
        this.ticketRepository = ticketRepository;
    }

    public List<Show> findAllShows() {
        return showRepository.findAll();
    }

    @Override
    public List<Show> findByPerformerAndDate(Artist performer, LocalDateTime date) {
        return showRepository.findByPerformerAndDate(performer, date);
    }

    @Override
    public Optional<Ticket> sellTicket(Show show, int seats) {
        return Optional.empty();
    }

    @Override
    public boolean modifyTicket(Show show, int seats) {
        return false;
    }
}
