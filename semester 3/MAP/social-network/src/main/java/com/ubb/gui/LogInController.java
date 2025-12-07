package com.ubb.gui;

import com.ubb.domain.user.User;
import com.ubb.exception.userException.UserDoseNotExistException;
import com.ubb.facade.UserFacade;
import com.ubb.service.DuckService;
import com.ubb.service.PersonService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Objects;

public class LogInController {

    @FXML
    private TextField usernameTestFieldLogIn, passwordTestFieldLogIn;

    @FXML
    private Button continueButton, leaveButton;

    @FXML
    public void initialize() {
        var personService = new PersonService();
        var duckService = new DuckService();
        var userFacade = new UserFacade(duckService,  personService);
        // can throw them away, and remake them in main

        leaveButton.setOnAction(event -> {
            Stage stage = (Stage) leaveButton.getScene().getWindow();
            stage.close();
        });

        continueButton.setOnAction(event -> {
            String username = usernameTestFieldLogIn.getText();
            String password = passwordTestFieldLogIn.getText();
            System.out.println(username + " " + password);
            try {
                openMainWindow(userFacade.login(username, password));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (UserDoseNotExistException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Warning");
                alert.setHeaderText(e.getMessage());
                alert.showAndWait();
            } catch (NoSuchAlgorithmException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        });

        usernameTestFieldLogIn.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                passwordTestFieldLogIn.requestFocus();
            }
        });

        passwordTestFieldLogIn.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                continueButton.fire();
            }
        });
    }

    private void openMainWindow(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ubb/gui/social-network-app.fxml"));
            loader.setControllerFactory(param -> new MainController(user));

            Stage stage = (Stage) usernameTestFieldLogIn.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Social Network - Logged in as " + user.getUsername());
            stage.getScene().getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("/com/ubb/gui/styles.css")).toExternalForm()
            );
            stage.show();

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed to open main window " +  ex.getMessage());
        }
    }
}
