package ro.mpp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ro.mpp.net.server.AbstractConcurrentServer;
import ro.mpp.net.errors.ServerErrorStatus;
import ro.mpp.net.server.FestivalJsonCS;

@SpringBootApplication(scanBasePackages = "ro.mpp")
public class StartJsonServer {
    private static final Logger logger = LogManager.getLogger(StartJsonServer.class);

    public static void main(String[] args) {
        SpringApplication.run(StartJsonServer.class, args);
    }

    @Bean
    public AbstractConcurrentServer jsonServer(ro.mpp.observer.IFestivalService festivalService) throws ServerErrorStatus {
        logger.info("Starting FTS Json - Java Server");
        AbstractConcurrentServer server = new FestivalJsonCS(5555, festivalService);
        server.start();
        return server;
    }
}
