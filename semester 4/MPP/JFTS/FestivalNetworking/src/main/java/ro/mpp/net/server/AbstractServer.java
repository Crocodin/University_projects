package ro.mpp.net.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class AbstractServer {
    private final int port;
    private ServerSocket serverSocket;
    private static final Logger logger = LogManager.getLogger(AbstractServer.class);

    public AbstractServer(int port) {
        this.port = port;
    }

    public void start() {
        logger.info("Starting server on port {}", this.port);
        try {
            this.serverSocket = new ServerSocket(port);
            logger.info("Successfully started server on port {}", this.port);
            while (!serverSocket.isClosed()) {
                logger.debug("Waiting for connection...");
                Socket clientSocket = this.serverSocket.accept();
                logger.info("Connection established from {}", clientSocket.getInetAddress());
                processClient(clientSocket);
            }
        } catch (IOException e) {
            logger.error("Error starting server on port {}: {}", this.port, e.toString());
            throw new RuntimeException(e);
        } finally {
            this.stop();
        }
    }

    public void stop() {
        logger.info("Stopping server on port {}", this.port);
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            logger.error("Error stopping server on port {}: {}", this.port, e.toString());
            throw new RuntimeException(e);
        }
    }

    protected abstract void processClient(Socket client);
}
