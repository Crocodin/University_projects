package com.ubb.gui;

import com.ubb.domain.user.User;
import com.ubb.facade.UserFacade;
import com.ubb.service.EventService;
import com.ubb.service.FriendService;
import com.ubb.service.MessageService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.Objects;

public class ProfileController {

    @FXML private ImageView profileImageView;

    @FXML private Label usernameLabel;
    @FXML private Label emailLabel;

    @FXML private Label friendsCountLabel;
    @FXML private Label messagesCountLabel;
    @FXML private Label eventsCountLabel;

    @FXML private Button changePictureButton;

    private User loggedUser;
    private UserFacade userFacade;

    /**
     * Called by the parent controller AFTER loading the FXML
     */
    public void setService(User user, UserFacade userFacade, MessageService messageService, EventService eventService,  FriendService friendService) {
        this.loggedUser = user;
        this.userFacade = userFacade;

        if (profileImageView != null) {
            loadUser();
            friendsCountLabel.setText(String.valueOf(friendService.getFriends(user).size()));
            messagesCountLabel.setText(String.valueOf(messageService.getNumberOfMessages(user.getId())));
            eventsCountLabel.setText(String.valueOf(eventService.findByUser(user.getId())));
        }
    }

    @FXML
    private void initialize() {
        changePictureButton.setOnAction(e -> changePicture());
    }

    private void loadUser() {
        if (loggedUser == null) return;

        usernameLabel.setText(loggedUser.getUsername());
        emailLabel.setText(loggedUser.getEmail());

        friendsCountLabel.setText("0");
        messagesCountLabel.setText("0");
        eventsCountLabel.setText("0");

        loadProfileImage();
    }

    private void loadProfileImage() {
        try {
            if (loggedUser.getProfilePicture() != null) {
                profileImageView.setImage(
                        new Image(new ByteArrayInputStream(loggedUser.getProfilePicture()))
                );
            } else {
                profileImageView.setImage(
                        new Image(Objects.requireNonNull(
                                getClass().getResourceAsStream("/default-avatar.png")
                        ))
                );
            }
        } catch (Exception e) {
            // Absolute fallback, never crash UI
            profileImageView.setImage(null);
        }
    }

    private void changePicture() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose profile picture");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );

        File file = chooser.showOpenDialog(profileImageView.getScene().getWindow());
        if (file == null) return;

        try {
            byte[] bytes = readFile(file);
            userFacade.updateProfilePicture(loggedUser.getId(), bytes);

            // Update UI immediately
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
