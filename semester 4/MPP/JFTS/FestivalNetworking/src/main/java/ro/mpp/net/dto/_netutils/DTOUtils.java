package ro.mpp.net.dto._netutils;

import ro.mpp.domain.*;
import ro.mpp.net.dto.*;

public class DTOUtils {
    // ----------------- // USER //
    public static UserDTO getDTO(User user){
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getPassword()
        );
    }

    public static User getFromDTO(UserDTO userDTO) {
        return new User(
                userDTO.getId(),
                userDTO.getUsername(),
                userDTO.getPassword()
        );
    }

    // ----------------- // SHOW //
    public static ShowDTO getDTO(Show show){
        return new ShowDTO(
                show.getId(),
                show.getTitle(),
                show.getDate(),
                show.getSoldSeats(),
                getDTO(show.getVenue())
        );
    }

    public static Show getFromDTO(ShowDTO showDTO) {
        return new Show(
                showDTO.getId(),
                showDTO.getDate(),
                showDTO.getTitle(),
                showDTO.getSoldSeats(),
                getFromDTO(showDTO.getVenueDTO())
        );
    }

    // ----------------- // ARTIST //
    public static ArtistDTO getDTO(Artist artist){
        return new ArtistDTO(
                artist.getId(),
                artist.getName()
        );
    }

    public static Artist getFromDTO(ArtistDTO artistDTO) {
        return new Artist(
                artistDTO.getId(),
                artistDTO.getName()
        );
    }

    // ----------------- // SHOW-ARTIST //
    public static ShowArtistDTO getDTO(ShowArtist showArtist) {
        return new ShowArtistDTO(
                getDTO(showArtist.getShow()),
                getDTO(showArtist.getArtist())
        );
    }

    public static ShowArtist getFromDTO(ShowArtistDTO showArtistDTO) {
        return new ShowArtist(
                getFromDTO(showArtistDTO.getShow()),
                getFromDTO(showArtistDTO.getArtist())
        );
    }

    // ----------------- // TICKET //
    public static TicketDTO getDTO(Ticket ticket){
        return new TicketDTO(
                ticket.getId(),
                ticket.getBuyerName(),
                ticket.getNumberOfSeats(),
                ticket.getPurchaseDate(),
                getDTO(ticket.getShow())
        );
    }

    public static Ticket getFromDTO(TicketDTO ticketDTO) {
        return new Ticket(
                ticketDTO.getId(),
                ticketDTO.getBuyerName(),
                ticketDTO.getSeats(),
                ticketDTO.getDate(),
                getFromDTO(ticketDTO.getShow())
        );
    }

    // ----------------- // VENUE //
    public static VenueDTO getDTO(Venue venue){
        return new VenueDTO(
                venue.getId(),
                venue.getName(),
                venue.getAddress(),
                venue.getCapacity()
        );
    }

    public static Venue getFromDTO(VenueDTO venueDTO){
        return new Venue(
                venueDTO.getId(),
                venueDTO.getName(),
                venueDTO.getAddress(),
                venueDTO.getCapacity()
        );
    }
}
