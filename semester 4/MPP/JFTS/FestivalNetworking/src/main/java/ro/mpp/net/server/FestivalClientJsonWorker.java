package ro.mpp.net.server;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp.authenticator.IAuthenticator;
import ro.mpp.domain.Show;
import ro.mpp.domain.ShowArtist;
import ro.mpp.domain.Ticket;
import ro.mpp.net.dto.ShowArtistDTO;
import ro.mpp.net.dto.TicketDTO;
import ro.mpp.net.dto.UserDTO;
import ro.mpp.net.dto._netutils.DTOUtils;
import ro.mpp.net.protocol.*;
import ro.mpp.observer.IFestivalObserver;
import ro.mpp.observer.IFestivalService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class FestivalClientJsonWorker implements Runnable, IFestivalObserver {

    private final IFestivalService festivalService;
    private final Socket connection;

    private BufferedReader input;
    private PrintWriter output;
    private final Gson gson = new Gson();
    private volatile boolean running;

    private static final Logger logger = LogManager.getLogger(FestivalClientJsonWorker.class);

    public FestivalClientJsonWorker(IFestivalService festivalService, Socket connection) {
        logger.debug("Creating FestivalClientJsonWorker");
        this.festivalService = festivalService;
        this.connection = connection;

        try {
            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            output = new PrintWriter(connection.getOutputStream());
            running = true;
        } catch (Exception e) {
            logger.error("Error initializing Json Worker: {}", e.getMessage());
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                String requestLine = input.readLine();
                if (requestLine == null) {       // client disconnected cleanly
                    running = false;
                    break;
                }
                logger.debug("Request received: {}", requestLine);
                Request request = gson.fromJson(requestLine, Request.class);
                Response response = this.handleRequest(request);
                if (response != null) {
                    this.sendResponse(response);
                }
            } catch (IOException e) {
                logger.error("Error reading request", e);
                running = false;
            }
        }
        closeConnection();
    }

    private Response handleRequest(Request request) {
        logger.debug("Handling request of type: {}", request.getRequestType());

        switch (request.getRequestType()) {
            case LOGIN: {
                UserDTO userDTO = request.getUser();
                logger.debug("Login request from: {}", userDTO.getUsername());

                var user = festivalService.authenticate(userDTO.getUsername(),  userDTO.getPassword());
                if (user.isEmpty()) {
                    return Response.builder()
                            .responseType(ResponseType.ERROR)
                            .errorMessage("User not found!")
                            .build();
                }
                festivalService.login(userDTO.getUsername(), userDTO.getPassword(), this);
                return Response.builder()
                        .responseType(ResponseType.OK)
                        .user(DTOUtils.getDTO(user.get()))
                        .build();

            }

            case LOGOUT: {
                logger.debug("Logout request");
                festivalService.logout(request.getUser().getUsername());
                running = false;
                return Response.builder()
                        .responseType(ResponseType.OK)
                        .build();
            }

            case FIND_ALL: {
                List<ShowArtist> all = festivalService.findAll();
                ShowArtistDTO[] DTOs = all.stream()
                        .map(DTOUtils::getDTO)
                        .toArray(ShowArtistDTO[]::new);

                return Response.builder()
                        .responseType(ResponseType.FIND_ALL)
                        .showArtists(DTOs)
                        .build();
            }

            case FIND_BY_DATE: {
                LocalDate date = LocalDate.parse(request.getDate());
                List<ShowArtist> filtered = festivalService.findByDate(date);
                ShowArtistDTO[] DTOs = filtered.stream()
                        .map(DTOUtils::getDTO)
                        .toArray(ShowArtistDTO[]::new);

                return Response.builder()
                        .responseType(ResponseType.FIND_BY_DATE)
                        .showArtists(DTOs)
                        .build();
            }

            case BUY_TICKET: {
                TicketDTO ticketDTO = request.getTicket();
                Show show = DTOUtils.getFromDTO(ticketDTO.getShow());

                Optional<Ticket> result = festivalService.sellTicket(show, ticketDTO.getBuyerName(), ticketDTO.getSeats());

                if (result.isPresent()) {
                    return Response.builder()
                            .responseType(ResponseType.OK)
                            .ticket(DTOUtils.getDTO(result.get()))
                            .build();
                }
                return Response.builder()
                        .responseType(ResponseType.ERROR)
                        .errorMessage("Could not sell ticket")
                        .build();
            }

            case MODIFY_TICKET: {
                if (festivalService.modifyTicket(request.getTicketId(), request.getSeats())) {
                    return Response.builder()
                            .responseType(ResponseType.OK)
                            .build();
                }
                return Response.builder()
                        .responseType(ResponseType.ERROR)
                        .errorMessage("Ticket not found")
                        .build();
            }

            default:
                logger.warn("Unknown request type: {}", request.getRequestType());
                return Response.builder()
                        .responseType(ResponseType.ERROR)
                        .errorMessage("Unknown request type")
                        .build();
        }
    }

    private void sendResponse(Response response) {
        String json = gson.toJson(response);
        logger.debug("Sending response: {}", json);
        synchronized (output) {
            output.println(json);
            output.flush();
        }
    }

    private void closeConnection() {
        try {
            input.close();
            output.close();
            connection.close();
            logger.debug("Connection closed");
        } catch (IOException e) {
            logger.error("Error closing connection", e);
        }
    }

    @Override
    public void ticketSold(Ticket ticket) {
        Response response = Response.builder()
                .responseType(ResponseType.TICKET_SOLD)
                .ticket(DTOUtils.getDTO(ticket))
                .build();
        sendResponse(response);
    }

    @Override
    public void ticketModified(Ticket ticket) {
        Response response = Response.builder()
                .responseType(ResponseType.TICKET_MODIFIED)
                .ticket(DTOUtils.getDTO(ticket))
                .build();
        sendResponse(response);
    }
}
