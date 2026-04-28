package ro.mpp.net.client;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp.domain.*;
import ro.mpp.net.dto.TicketDTO;
import ro.mpp.net.dto.UserDTO;
import ro.mpp.net.dto._netutils.DTOUtils;
import ro.mpp.net.errors.ProxyErrorStatus;
import ro.mpp.net.errors.ServerErrorStatus;
import ro.mpp.net.protocol.*;
import ro.mpp.observer.IFestivalObserver;
import ro.mpp.observer.IFestivalService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FestivalServicesJsonProxy implements IFestivalService {
    private final String host;
    private final int port;

    private IFestivalObserver client;

    private BufferedReader input;
    private PrintWriter output;
    private final Gson gson = new Gson();
    private Socket connection;

    private final BlockingQueue<Response> responseQueue;
    private volatile boolean running;

    private static final Logger logger = LogManager.getLogger(FestivalServicesJsonProxy.class);

    public FestivalServicesJsonProxy(String host, int port) {
        this.host = host;
        this.port = port;
        this.responseQueue = new LinkedBlockingQueue<>();
    }

    public void start() {
        this.initializeConnection();
    }

    public void stop() {
        this.closeConnection();
    }

    @Override
    public Optional<User> authenticate(String username, String password) {
        var request = Request.builder()
                .requestType(RequestType.LOGIN)
                .user(new UserDTO(-1, username, password))
                .build();
        this.sendRequest(request);

        Response response = this.readResponse();
        if (response.getResponseType().equals(ResponseType.OK)) {
            return Optional.of(DTOUtils.getFromDTO(response.getUser()));
        }
        if (response.getResponseType().equals(ResponseType.ERROR)) {
            logger.error("Login Failed - closing connection - {}", response.getErrorMessage());
            this.closeConnection();
            throw new ServerErrorStatus(response.getErrorMessage());
        }
        return Optional.empty();
    }

    public void logout() {
        logger.debug("JsonProxy request logged out");
        var request = Request.builder()
                .requestType(RequestType.LOGOUT)
                .build();
        this.sendRequest(request);
        Response response = this.readResponse();
        this.stop();
        if (response.getResponseType().equals(ResponseType.ERROR)) {
            logger.error("JsonProxy error logging out: {}", response.getErrorMessage());
            throw new ServerErrorStatus(response.getErrorMessage());
        }
    }

    private void initializeConnection() {
        try {
            connection = new Socket(host, port);

            output = new PrintWriter(connection.getOutputStream(), true);
            output.flush();

            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            this.running = true;

            this.startReader();
        } catch (UnknownHostException e) {
            logger.error("UnknownHostException in in JsonProxy", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("IOException in JsonProxy", e);
            throw new RuntimeException(e);
        }
    }

    private void startReader() {
        Thread thread = new Thread(new ReaderThread());
        thread.start();
    }

    private boolean isUpdate(ResponseType responseType) {
        return responseType == ResponseType.TICKET_SOLD || responseType == ResponseType.TICKET_MODIFIED;
    }

    private void handleUpdate(Response response) {
        if (client == null) return;

        switch (response.getResponseType()) {
            case TICKET_SOLD: {
                Ticket ticket = DTOUtils.getFromDTO(response.getTicket());
                logger.debug("Received ticket sold update: {}", ticket);
                try {
                    client.ticketSold(ticket);
                } catch (Exception e) {
                    logger.error("Error notifying observer for ticket sold: {}", e.getMessage());
                }
                break;
            }
            case TICKET_MODIFIED: {
                Ticket ticket = DTOUtils.getFromDTO(response.getTicket());
                logger.debug("Received ticket modified update: {}", ticket);
                try {
                    client.ticketModified(ticket);
                } catch (Exception e) {
                    logger.error("Error notifying observer for ticket modified: {}", e.getMessage());
                }
                break;
            }
            default:
                logger.warn("Received unexpected update type: {}", response.getResponseType());
        }
    }

    @Override
    public List<ShowArtist> findByDate(LocalDate date) {
        var request = Request.builder()
                .requestType(RequestType.FIND_BY_DATE)
                .date(date.toString())
                .build();
        this.sendRequest(request);
        Response response = this.readResponse();
        if (response.getResponseType().equals(ResponseType.FIND_BY_DATE)) {
            return Arrays.stream(response.getShowArtists()).map(DTOUtils::getFromDTO).toList();
        }
        return Collections.emptyList();
    }

    @Override
    public List<ShowArtist> findAll() {
        var request = Request.builder()
                .requestType(RequestType.FIND_ALL)
                .build();
        this.sendRequest(request);
        Response response = this.readResponse();
        if (response.getResponseType().equals(ResponseType.FIND_ALL)) {
            return Arrays.stream(response.getShowArtists()).map(DTOUtils::getFromDTO).toList();
        }
        return List.of();
    }

    @Override
    public Optional<Show> findById(int id) {
        return Optional.empty();
    }

    @Override
    public Optional<Ticket> sellTicket(Show show, String buyerName, int seats) {
        var request = Request.builder()
                .requestType(RequestType.BUY_TICKET)
                .ticket(TicketDTO.builder()
                        .show(DTOUtils.getDTO(show))
                        .buyerName(buyerName)
                        .seats(seats)
                        .build())
                .build();
        this.sendRequest(request);
        Response response = this.readResponse();
        if (response.getResponseType().equals(ResponseType.OK)) {
            return Optional.of(DTOUtils.getFromDTO(response.getTicket()));
        }
        throw new ProxyErrorStatus(response.getErrorMessage());
    }

    @Override
    public boolean modifyTicket(int ticketId, int seats) {
        var request = Request.builder()
                .requestType(RequestType.MODIFY_TICKET)
                .TicketId(ticketId)
                .seats(seats)
                .build();
        this.sendRequest(request);
        Response response = this.readResponse();
        if (response.getResponseType().equals(ResponseType.OK)) {
            return true;
        }
        throw new ProxyErrorStatus(response.getErrorMessage());
    }

    @Override
    public void login(String username, String password, IFestivalObserver observer) {
        logger.info("I should never print");
    }

    @Override
    public void logout(String username) {
        this.logout();
    }

    @Override
    public void setObserver(IFestivalObserver observer) {
        this.client = observer;
    }

    private class ReaderThread implements Runnable {
        @Override
        public void run() {
            while (running) {
                try {
                    String responseLine = input.readLine();
                    Response response = gson.fromJson(responseLine, Response.class);
                    logger.debug("Received response: {}", response);
                    logger.info("Received response: {}", response.getResponseType());
                    if (isUpdate(response.getResponseType())) {
                        handleUpdate(response);
                    } else {
                        responseQueue.put(response);
                    }
                } catch (IOException e) {
                    logger.error("IOException in ReaderThread", e);
                    throw new RuntimeException(e);

                } catch (InterruptedException e) {
                    logger.error("InterruptedException in ReaderThread via the responseQueue", e);
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void closeConnection() {
        this.running = false;
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            logger.error("IOException in ReaderThread", e);
        }
    }

    private void sendRequest(Request request) {
        String json = gson.toJson(request);
        logger.debug("Sending request: {}", json);
        try {
            output.println(json);
            output.flush();
        } catch (Exception e) {
            logger.error("In JsonProxy error sending request", e);
            throw new RuntimeException(e);
        }
    }

    private Response readResponse() {
        Response response = null;
        try {
            response = responseQueue.take();
        } catch (InterruptedException e) {
            logger.error("InterruptedException in responseQueue", e);
        }
        return response;
    }
}
