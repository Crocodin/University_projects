package ro.mpp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main extends Application {
    private ApplicationContext context;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        context = new ClassPathXmlApplicationContext(" festivalConfigurations.xml");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
        loader.setControllerFactory(c -> context.getBean(c));
        Scene scene = new Scene(loader.load());
        stage.setTitle("FTS - Login");
        stage.setScene(scene);
        stage.show();
    }
}
