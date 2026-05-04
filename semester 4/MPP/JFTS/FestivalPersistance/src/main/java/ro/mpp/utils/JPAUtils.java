package ro.mpp.utils;


import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JPAUtils {
    private static final Logger logger = LogManager.getLogger(JPAUtils.class);
    private static EntityManagerFactory emf;

    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            logger.info("Initializing EntityManagerFactory for persistence unit 'FestivalSystem'");
            try {
                emf = Persistence.createEntityManagerFactory("FestivalSystem");
                logger.info("EntityManagerFactory initialized successfully");
            } catch (Exception e) {
                logger.error("Failed to initialize EntityManagerFactory", e);
                throw e;
            }
        } else logger.debug("EntityManagerFactory already initialized, reusing existing instance");

        return emf;
    }

    public static void closeEntityManagerFactory() {
        if (emf != null && emf.isOpen()) {
            logger.info("Closing EntityManagerFactory");
            emf.close();
            logger.info("EntityManagerFactory closed successfully");
        } else logger.warn("closeEntityManagerFactory called but EMF is already closed or null");

    }
}
