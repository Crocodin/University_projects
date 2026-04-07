package ro.mpp.net.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp.authenticator.IAuthenticator;
import ro.mpp.service.IFestivalService;

import java.net.Socket;

public class FestivalJsonCS extends AbstractConcurrentServer {
    private static final Logger logger = LogManager.getLogger(FestivalJsonCS.class);
    private final IFestivalService festivalService;
    private final IAuthenticator authenticator;

    public FestivalJsonCS(int port, IFestivalService festivalService, IAuthenticator authenticator) {
        logger.info("Creating FestivalJsonCS");
        super(port);
        logger.info("Created successfully FestivalJsonCS");
        this.festivalService = festivalService;
        this.authenticator = authenticator;
    }

    @Override
    protected Thread createWorker(Socket client) {
        return new Thread(new FestivalClientJsonWorker(festivalService, authenticator, client));
    }
}
