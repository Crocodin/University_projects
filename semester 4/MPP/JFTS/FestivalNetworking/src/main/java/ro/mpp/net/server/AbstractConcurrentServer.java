package ro.mpp.net.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.Socket;

public abstract class AbstractConcurrentServer extends AbstractServer {
    private static final Logger logger = LogManager.getLogger(AbstractConcurrentServer.class);

    public AbstractConcurrentServer(int port) {
        logger.info("Starting concurrent server on port {}", port);
        super(port);
        logger.info("Concurrent server successfully started on port {}", port);
    }

    @Override
    protected void processClient(Socket client) {
        Thread thread = createWorker(client);
        thread.start();
    }

    protected abstract Thread createWorker(Socket client);
}
