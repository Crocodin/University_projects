package com.ubb;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SocialNetworkApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/ubb/gui/login.fxml")
        );
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("Social Network App");

        stage.show();
    }
}
