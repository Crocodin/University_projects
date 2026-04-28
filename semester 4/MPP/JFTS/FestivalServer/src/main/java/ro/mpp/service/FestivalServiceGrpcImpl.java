package ro.mpp.service;

import com.google.protobuf.Empty;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp.domain.Show;
import ro.mpp.domain.ShowArtist;
import ro.mpp.domain.Ticket;
import ro.mpp.grpc.FestivalProto;
import ro.mpp.grpc.FestivalServiceGrpc;
import ro.mpp.observer.IFestivalObserver;
import ro.mpp.observer.IFestivalService;
import ro.mpp.grpc.FestivalProto.*;

import java.lang.classfile.Opcode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class FestivalServiceGrpcImpl extends FestivalServiceGrpc.FestivalServiceImplBase
        implements IFestivalObserver {

    private final IFestivalService festivalService;
    private final Map<String, StreamObserver<FestivalProto.EventMessage>> subscribers = new ConcurrentHashMap<>();
    private static final Logger logger = LogManager.getLogger(FestivalServiceGrpcImpl.class);

    public FestivalServiceGrpcImpl(IFestivalService festivalService) {
        this.festivalService = festivalService;
        festivalService.login("__grpc__", "", this);
    }

    @Override
    public void login(FestivalProto.LoginRequest request, StreamObserver<FestivalProto.LoginResponse> responseObserver) {
        logger.debug("Login request from: {}", request.getUsername());
        var user = festivalService.authenticate(request.getUsername(), request.getPassword());
        if (user.isPresent()) {
            responseObserver.onNext(LoginResponse.newBuilder()
                    .setSuccess(true)
                    .setUserId(user.get().getId())
                    .setUsername(user.get().getUsername())
                    .build());
        } else {
            responseObserver.onNext(LoginResponse.newBuilder()
                    .setSuccess(false)
                    .setErrorMessage("Invalid username or password")
                    .build());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void logout(LogoutRequest request, StreamObserver<BoolResponse> responseObserver) {
        logger.debug("Logout request from: {}", request.getUsername());
        festivalService.logout(request.getUsername());
        subscribers.remove(request.getUsername());
        responseObserver.onNext(BoolResponse.newBuilder().setSuccess(true).build());
        responseObserver.onCompleted();
    }

    @Override
    public void findByDate(DateRequest request, StreamObserver<ShowArtistList> responseObserver) {
        logger.debug("FindByDate request: {}", request.getDate());
        List<ShowArtist> filtered = festivalService.findByDate(LocalDate.parse(request.getDate()));
        ShowArtistList list = ShowArtistList.newBuilder()
                .addAllShowArtists(filtered.stream().map(this::toProto).toList())
                .build();
        responseObserver.onNext(list);
        responseObserver.onCompleted();
    }

    @Override
    public void findAll(Empty request, StreamObserver<ShowArtistList> responseObserver) {
        logger.debug("FindAll request");

        try {
            List<ShowArtist> all = festivalService.findAll();

            ShowArtistList list = ShowArtistList.newBuilder()
                    .addAllShowArtists(all.stream().map(this::toProto).toList())
                    .build();

            responseObserver.onNext(list);
        } catch (Exception e) {
            logger.error("Error in FindAll", e);
            responseObserver.onError(e);
            return;
        }

        responseObserver.onCompleted();
    }

    @Override
    public void sellTicket(SellTicketRequest request, StreamObserver<TicketResponse> responseObserver) {
        logger.debug("SellTicket request for showId: {}", request.getShowId());
        try {
            // fetch the Show from the service so we have the full object
            Optional<Show> show = festivalService.findById(request.getShowId());

            if (show.isEmpty()) throw new RuntimeException("Synchronization issue, no show with is " + request.getShowId());

            var result = festivalService.sellTicket(show.get(), request.getBuyerName(), request.getSeats());

            if (result.isPresent()) {
                responseObserver.onNext(TicketResponse.newBuilder()
                        .setSuccess(true)
                        .setTicketId(result.get().getId())
                        .build());
            } else {
                responseObserver.onNext(TicketResponse.newBuilder()
                        .setSuccess(false)
                        .setErrorMessage("Could not sell ticket")
                        .build());
            }
        } catch (Exception e) {
            logger.error("Error selling ticket", e);
            responseObserver.onNext(TicketResponse.newBuilder()
                    .setSuccess(false)
                    .setErrorMessage(e.getMessage())
                    .build());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void modifyTicket(ModifyTicketRequest request, StreamObserver<BoolResponse> responseObserver) {
        logger.debug("ModifyTicket request for ticketId: {}", request.getTicketId());
        try {
            boolean ok = festivalService.modifyTicket(request.getTicketId(), request.getSeats());
            responseObserver.onNext(BoolResponse.newBuilder().setSuccess(ok).build());
        } catch (Exception e) {
            logger.error("Error modifying ticket", e);
            responseObserver.onNext(BoolResponse.newBuilder()
                    .setSuccess(false)
                    .setErrorMessage(e.getMessage())
                    .build());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void subscribe(SubscribeRequest request, StreamObserver<EventMessage> responseObserver) {
        logger.info("Subscribe request from: {}", request.getUsername());
        subscribers.put(request.getUsername(), responseObserver);

        // keep the stream open until the client disconnects
        ((ServerCallStreamObserver<EventMessage>) responseObserver).setOnCancelHandler(() -> {
            logger.info("Subscriber {} disconnected", request.getUsername());
            subscribers.remove(request.getUsername());
        });
    }

    @Override
    public void ticketSold(Ticket ticket) {
        logger.info("Broadcasting ticketSold to {} subscribers", subscribers.size());
        EventMessage event = EventMessage.newBuilder()
                .setEventType(EventType.TICKET_SOLD)
                .setTicketId(ticket.getId())
                .setShow(toProto(ticket.getShow()))
                .setSeats(ticket.getNumberOfSeats())
                .build();
        broadcast(event);
    }

    @Override
    public void ticketModified(Ticket ticket) {
        logger.debug("Broadcasting ticketModified to {} subscribers", subscribers.size());
        EventMessage event = EventMessage.newBuilder()
                .setEventType(EventType.TICKET_MODIFIED)
                .setTicketId(ticket.getId())
                .setShow(toProto(ticket.getShow()))
                .setSeats(ticket.getNumberOfSeats())
                .build();
        broadcast(event);
    }

    private void broadcast(EventMessage event) {
        subscribers.entrySet().removeIf(entry -> {
            try {
                entry.getValue().onNext(event);
                return false;
            } catch (Exception e) {
                logger.warn("Subscriber {} disconnected: {}", entry.getKey(), e.getMessage());
                return true;
            }
        });
    }

    private ShowMessage toProto(Show show) {
        VenueMessage venueMessage = VenueMessage.newBuilder()
                .setId(show.getVenue().getId())
                .setName(show.getVenue().getName())
                .setAddress(show.getVenue().getAddress())
                .setCapacity(show.getVenue().getCapacity())
                .build();

        return ShowMessage.newBuilder()
                .setShowId(show.getId())
                .setTitle(show.getTitle())
                .setDate(show.getDate())
                .setSoldSeats(show.getSoldSeats())
                .setVenue(venueMessage)
                .build();
    }

    private ShowArtistMessage toProto(ShowArtist sa) {
        ArtistMessage artistMessage = ArtistMessage.newBuilder()
                .setId(sa.getArtist().getId())
                .setName(sa.getArtist().getName())
                .build();

        ShowMessage showMessage = toProto(sa.getShow());

        return ShowArtistMessage.newBuilder()
                .setArtist(artistMessage)
                .setShow(showMessage)
                .build();
    }
}