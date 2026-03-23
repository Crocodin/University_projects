package ro.mpp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import lombok.Setter;
import ro.mpp.authenticator.IAuthenticator;
import ro.mpp.domain.User;

public class LoginController {
    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private Button loginButton;

    @Setter private IAuthenticator authenticator;

    @FXML public void ifIsEnterUsername(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            password.requestFocus();
        }
    }

    @FXML public void ifIsEnterPassword(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            loginButton.fire();
        }
    }

    @FXML public void login(ActionEvent actionEvent) {
        authenticator.authenticate(username.getText(), password.getText()).ifPresentOrElse(
                this::openMainWindow,
                this::showError
        );
    }

    private void openMainWindow(User user) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
        ((MainController) loader.getController()).setUser(user);

        Stage stage = new Stage();
        stage.setTitle("Login as " + user.getUsername());
        stage.setScene(new Scene(loader.getRoot()));
        stage.show();
        ((Stage) loginButton.getScene().getWindow()).close();
    }

    private void showError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Invalid username or password");
        alert.showAndWait();
    }
}
