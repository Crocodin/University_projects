package com.ubb.gui;

import com.ubb.domain.connection.Friendship;
import com.ubb.domain.notifications.FriendRequest;
import com.ubb.domain.notifications.RequestStatus;
import com.ubb.facade.UserFacade;
import com.ubb.observer.Observer;
import com.ubb.service.FriendService;
import com.ubb.service.NotificationService;
import com.ubb.utils.paging.Pageable;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class FriendshipTabController implements Observer {
    @FXML
    private TableView<Friendship> friendTableView;

    @FXML
    private Button removeFriendButton, addFriendButton, prevFriendButton, nextFriendButton;

    @FXML
    private TextField userOneId, userTwoId;

    private FriendService  friendService;

    private final Pageable pageable = new Pageable(0, 4);
    private UserFacade userFacade;
    private NotificationService notificationService;

    public FriendshipTabController(FriendService friendService, UserFacade userFacade, NotificationService notificationService) {
        this.friendService = friendService;
        this.userFacade = userFacade;
        this.notificationService = notificationService;
    }

    @FXML
    public void initialize() {
        friendTableView.setEditable(true);
        notificationService.addObserver(this);

        TableColumn<Friendship, Long> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Friendship, String> colUserOne = new TableColumn<>("User One");
        colUserOne.setCellValueFactory(data ->
                new ReadOnlyObjectWrapper<>(data.getValue().getUserOne().getUsername()));

        TableColumn<Friendship, String> colUserTwo = new TableColumn<>("User Two");
        colUserTwo.setCellValueFactory(data ->
                new ReadOnlyObjectWrapper<>(data.getValue().getUserTwo().getUsername()));

        friendTableView.getColumns().addAll(colId, colUserOne, colUserTwo);

        ObservableList<Friendship> friendData = FXCollections.observableArrayList();
        friendTableView.setItems(friendData);

        friendData.setAll(friendService.findAllOnPage(pageable).getElementsOnPage());

        this.nextFriendButton.setOnAction(e -> {
            pageable.setPageNumber(pageable.getPageNumber() + 1);
            friendData.setAll(friendService.findAllOnPage(pageable).getElementsOnPage());
        });

        this.prevFriendButton.setOnAction(e -> {
            pageable.setPageNumber(pageable.getPageNumber() - 1);
            friendData.setAll(friendService.findAllOnPage(pageable).getElementsOnPage());
        });

        addFriendButton.setOnAction(e -> {
            long id1 =  Long.parseLong(userOneId.getText());
            long id2 = Long.parseLong(userTwoId.getText());
            try {
                FriendRequest fr = new FriendRequest((long) 0, userFacade.getUser(id1), userFacade.getUser(id2), RequestStatus.PENDING);
                notificationService.addFriendRequest(fr);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        removeFriendButton.setOnAction(e -> {
            long id1 =  Long.parseLong(userOneId.getText());
            long id2 = Long.parseLong(userTwoId.getText());
            try {
                var friend = friendService.findFriendShip(id1, id2);
                friendService.removeObject(friend);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            friendData.setAll(friendService.findAllOnPage(pageable).getElementsOnPage());
        });

        friendTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                userOneId.setText(friendData.getFirst().getUserOne().getId().toString());
                userTwoId.setText(friendData.getFirst().getUserTwo().getId().toString());
                System.out.println(userOneId.getText());
                System.out.println(userTwoId.getText());
            }
        });
    }

    public void refresh() {
        friendTableView.getItems().setAll(
                friendService.findAllOnPage(pageable).getElementsOnPage()
        );
    }

    @Override
    public void update() {
        this.refresh();
    }
}