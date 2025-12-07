package com.ubb.gui;

import com.ubb.domain.connection.FriendShip;
import com.ubb.domain.duck.Duck;
import com.ubb.service.FriendService;
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
import java.util.List;

public class FriendShipTabController {
    @FXML
    private TableView<FriendShip> friendTableView;

    @FXML
    private Button removeFriendButton, addFriendButton, prevFriendButton, nextFriendButton;

    @FXML
    private TextField userOneId, userTwoId;

    private FriendService  friendService;

    private final Pageable pageable = new Pageable(0, 4);

    public FriendShipTabController(FriendService friendService) {
        this.friendService = friendService;
    }

    @FXML
    public void initialize() {
        friendTableView.setEditable(true);

        TableColumn<FriendShip, Long> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<FriendShip, String> colUserOne = new TableColumn<>("User One");
        colUserOne.setCellValueFactory(data ->
                new ReadOnlyObjectWrapper<>(data.getValue().getUserOne().getUsername()));

        TableColumn<FriendShip, String> colUserTwo = new TableColumn<>("User Two");
        colUserTwo.setCellValueFactory(data ->
                new ReadOnlyObjectWrapper<>(data.getValue().getUserTwo().getUsername()));

        friendTableView.getColumns().addAll(colId, colUserOne, colUserTwo);

        ObservableList<FriendShip> friendData = FXCollections.observableArrayList();
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
                friendService.addObject("0," + id1 + "," + id2);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            friendData.setAll(friendService.findAllOnPage(pageable).getElementsOnPage());
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
    }
}
