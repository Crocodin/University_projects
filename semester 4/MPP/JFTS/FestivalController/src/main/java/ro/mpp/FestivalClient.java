package ro.mpp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp.controller.LoginController;
import ro.mpp.net.client.FestivalServicesJsonProxy;
import ro.mpp.utils.Config;

public class FestivalClient extends Application {

    private FestivalServicesJsonProxy server = null;

    private static final Logger logger = LogManager.getLogger(FestivalClient.class);

    @Override
    public void start(Stage stage) throws Exception {
        logger.info("Starting FestivalClient");

        int port = Integer.parseInt(Config.getProperties().getProperty("SERVER_PORT"));
        String host = Config.getProperties().getProperty("SERVER_HOST");

        logger.debug("Host: {}", host);
        logger.debug("Port: {}", port);

        this.server = new FestivalServicesJsonProxy(host, port);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
        Scene scene = new Scene(loader.load());

        LoginController controller = loader.getController();
        controller.setServer(server);
        server.start();

        stage.setTitle("FTS - Login");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        logger.info("Application shutting down, closing resources");
        this.server.logout();
        logger.info("Resources closed successfully");
    }
}
