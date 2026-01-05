package com.ubb.gui;

import com.ubb.domain.user.User;
import com.ubb.facade.UserFacade;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.*;
import java.sql.SQLException;
import java.util.Objects;

public class ProfileController {

    @FXML
    private ImageView profileImageView;

    @FXML
    private Label usernameLabel, emailLabel;

    @FXML
    private Button changePictureButton;

    private User loggedUser;
    private UserFacade userFacade;

    public void setService(User user, UserFacade userFacade) {
        this.loggedUser = user;
        this.userFacade = userFacade;
        loadUser();
    }

    private void loadUser() {
        usernameLabel.setText(loggedUser.getUsername());
        emailLabel.setText(loggedUser.getEmail());

        if (loggedUser.getProfilePicture() != null) {
            profileImageView.setImage(
                    new Image(new ByteArrayInputStream(loggedUser.getProfilePicture()))
            );
        } else {
            profileImageView.setImage(
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream("/default-avatar.png")))
            );
        }
    }

    @FXML
    private void initialize() {
        changePictureButton.setOnAction(e -> changePicture());
    }

    private void changePicture() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );

        File file = chooser.showOpenDialog(profileImageView.getScene().getWindow());
        if (file == null) return;

        try {
            byte[] bytes = readFile(file);
            userFacade.updateProfilePicture(loggedUser.getId(), bytes);

            profileImageView.setImage(new Image(new FileInputStream(file)));

        } catch (Exception ex) {
            throw new RuntimeException("Failed to update profile picture", ex);
        }
    }

    private byte[] readFile(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return fis.readAllBytes();
        }
    }
}
