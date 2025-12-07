package com.ubb.gui;

import com.ubb.domain.user.User;
import com.ubb.facade.UserFacade;
import com.ubb.service.ConnectionService;
import com.ubb.service.FriendService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class GraphTabController {
    @FXML
    TextFlow graphTabTextFlow;
    @FXML
    Button numberOfCommunitiesButton;
    @FXML
    Button longestChainButton;

    public UserFacade userFacade;
    public FriendService friendService;

    public GraphTabController(UserFacade userFacade, FriendService friendService) {
        this.userFacade = userFacade;
        this.friendService = friendService;
    }

    @FXML
    public void initialize() {
        graphTabTextFlow.getStyleClass().add("console-area");

        numberOfCommunitiesButton.setOnAction(event -> {
            int count = ConnectionService.findCommunities(
                    userFacade.getUsers(),
                    friendService
            ).size();

            graphTabTextFlow.getChildren().clear();
            Text linePrefix = new Text("The number of communities is ");
            linePrefix.getStyleClass().add("console-text");

            Text numberOfCommunitiesText = new Text(String.valueOf(count));
            numberOfCommunitiesText.getStyleClass().add("console-text-key");
            graphTabTextFlow.getChildren().addAll(linePrefix, numberOfCommunitiesText);
        });

        longestChainButton.setOnAction(event -> {
            graphTabTextFlow.getChildren().clear();

            var listOfCon = ConnectionService.biggestCommunity(
                    userFacade.getUsers(),
                    friendService
            ).stream().toList();

            for (User user : listOfCon) {
                Text linePrefix = new Text("  └─ " + user.getId() + " - ");
                linePrefix.getStyleClass().add("console-text");

                Text username = new Text(user.getUsername() + "\n");
                username.getStyleClass().add("console-text-username");

                graphTabTextFlow.getChildren().addAll(linePrefix, username);
            }
        });
    }
}
