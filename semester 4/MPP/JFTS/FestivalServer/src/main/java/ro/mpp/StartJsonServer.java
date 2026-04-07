package ro.mpp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ro.mpp.net.server.AbstractConcurrentServer;
import ro.mpp.net.server.AbstractServer;
import ro.mpp.net.errors.ServerErrorStatus;

public class StartJsonServer {
    private static final Logger logger = LogManager.getLogger(StartJsonServer.class);

    public static void main(String[] args) {
        logger.info("Starting FTS Json - Java Server");

        ApplicationContext context = new ClassPathXmlApplicationContext("/spring-server.xml");

        AbstractConcurrentServer server = context.getBean("festivalServer", AbstractConcurrentServer.class);
        try {
            server.start();
        } catch (ServerErrorStatus e) {
            logger.error("Error starting server: {}", e.getMessage());
        }
    }
}
