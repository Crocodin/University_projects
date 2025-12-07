package com.ubb.gui;

import com.ubb.domain.event.Event;
import com.ubb.domain.user.User;
import com.ubb.exception.EntityException;
import com.ubb.facade.UserFacade;
import com.ubb.service.EventService;
import com.ubb.utils.paging.Pageable;
import com.ubb.domain.validator.ValidatorContext;
import com.ubb.domain.validator.ValidatorEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;

public class EventTabController {
    @FXML
    private TableView<Event> eventTableView;

    @FXML
    private TableView<User> subscribersTableView;

    @FXML
    private Button notifySubsButton;

    @FXML
    private Button addEventButton, removeEventButton;

    @FXML
    private Button addSubButton, removeSubButton;

    @FXML
    private Button previousEventButton, nextEventButton;

    @FXML
    private TextField eventIdTextField, eventNameTextField;


    private EventService eventService;
    private UserFacade userFacade;
    Event clickedEvent;
    private final Pageable pageable = new Pageable(0, 4);

    public EventTabController(EventService eventService, UserFacade userFacade) {
        this.eventService = eventService;
        this.userFacade = userFacade;
    }

    @FXML
    private void initialize() {
        eventTableView.setEditable(true);
        List<TableColumn<Event, ?>> columns = List.of(
                col("ID", "id"),
                col("Name", "name")
        );

        subscribersTableView.setEditable(true);
        List<TableColumn<User, ?>> columnsSub = List.of(
                col("ID", "id"),
                col("Username", "username")
        );

        eventTableView.getColumns().addAll(columns);
        ObservableList<Event> eventData = FXCollections.observableArrayList();
        eventTableView.setItems(eventData);

        eventData.setAll(
                eventService.findAllOnPage(pageable).getElementsOnPage()
        );

        subscribersTableView.getColumns().addAll(columnsSub);
        ObservableList<User> eventDataSub = FXCollections.observableArrayList();
        subscribersTableView.setItems(eventDataSub);

        this.nextEventButton.setOnAction(e -> {
            pageable.setPageNumber(pageable.getPageNumber() + 1);

            eventData.setAll(eventService.findAllOnPage(pageable).getElementsOnPage());
        });
        this.previousEventButton.setOnAction(e -> {
            pageable.setPageNumber(pageable.getPageNumber() - 1);

            eventData.setAll(eventService.findAllOnPage(pageable).getElementsOnPage());
        });

        eventTableView.setRowFactory(tv -> {
            TableRow<Event> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    this.clickedEvent = row.getItem();

                    if (event.getClickCount() == 2) {
                        // Double-click -> fill the form
                        eventIdTextField.setText(String.valueOf(clickedEvent.getId()));
                        eventNameTextField.setText(clickedEvent.getName());

                        try {
                            eventDataSub.setAll(
                                eventService.findId(Long.parseLong(eventIdTextField.getText())).getSubscribers()
                            );
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }

                        removeEventButton.setDisable(false);
                        addEventButton.setDisable(true);
                        removeSubButton.setDisable(true);
                    } else if (event.getClickCount() == 1) {
                        // Single-click on a different row -> clear form
                        eventDataSub.clear();
                        removeSubButton.setDisable(false);
                        eventTableView.getSelectionModel().clearSelection();
                        clearEventFields();
                    }
                } else {
                    // Clicked empty space -> clear form
                    eventDataSub.clear();
                    removeSubButton.setDisable(false);
                    clearEventFields();
                }
            });

            return row;
        });

        subscribersTableView.setRowFactory(tv -> {
            TableRow<User> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    User clickedUser = row.getItem();

                    if (event.getClickCount() == 2) {
                        // Double-click -> fill the form
                        eventIdTextField.setText(String.valueOf(clickedUser.getId()));
                        eventNameTextField.setText(clickedUser.getUsername());

                        eventNameTextField.setEditable(false);
                        removeEventButton.setDisable(true);
                        removeSubButton.setDisable(false);
                        addEventButton.setDisable(true);
                        addSubButton.setDisable(true);
                    } else if (event.getClickCount() == 1) {
                        // Single-click on a different row -> clear form
                        removeEventButton.setDisable(false);
                        clearEventFields();
                    }
                } else {
                    // Clicked empty space -> clear form
                    removeEventButton.setDisable(false);
                    clearEventFields();
                }
            });

            return row;
        });

        addEventButton.setOnAction(e -> {
            String line = "RE,0," + eventNameTextField.getText();
            ValidatorContext context = new ValidatorContext(new ValidatorEvent());
            if (context.validate(line)) {
                try {
                    eventService.addObject(line);

                    // Show success popup
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Event successfully added!");
                    alert.showAndWait();
                } catch (SQLException ex) {
                    // Show SQL error popup
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Database Error");
                    alert.setHeaderText("Failed to add event");
                    alert.setContentText(ex.getMessage());
                    alert.showAndWait();
                }
            }
            else {
                // Show validation error popup
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Validation Error");
                alert.setHeaderText("Invalid input");
                alert.setContentText("Error adding the event, please check your input!");
                alert.showAndWait();
            }
        });

        addSubButton.setOnAction(e -> {
            try {
                var idEvent = Long.parseLong(eventIdTextField.getText());
                var idUser = Long.parseLong(eventNameTextField.getText());
                eventService.addSubscriber(idEvent, userFacade.getUser(idUser));

                try {
                    eventDataSub.setAll(
                            eventService.findId(clickedEvent.getId()).getSubscribers()
                    );
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                // Show success popup
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("User subscribed subscribed!");
                alert.showAndWait();
            }
            catch (SQLException exception) {
                // Show SQL error popup
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Database Error");
                alert.setHeaderText("Failed to subscribed user");
                alert.setContentText(exception.getMessage());
                alert.showAndWait();
            }
            catch (EntityException exception) {
                // Show validation error popup
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Validation Error");
                alert.setHeaderText("Invalid input");
                alert.setContentText("Error subscribing user, please check your input!");
                alert.showAndWait();
            }
        });

        removeEventButton.setOnAction(e -> {
            try {
                var idEvent = Integer.parseInt(eventIdTextField.getText());
                var event = eventService.findId(idEvent);
                eventService.removeObject(event);
                eventData.setAll(eventService.findAllOnPage(pageable).getElementsOnPage());

                // Show success popup
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Event successfully removed!");
                alert.showAndWait();
            }
            catch (SQLException exception) {
                // Show SQL error popup
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Database Error");
                alert.setHeaderText("Failed to add event");
                alert.setContentText(exception.getMessage());
                alert.showAndWait();
            }
            catch (EntityException exception) {
                // Show validation error popup
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Validation Error");
                alert.setHeaderText("Invalid input");
                alert.setContentText("Error adding the event, please check your input!");
                alert.showAndWait();
            }
        });

        removeSubButton.setOnAction(e -> {
            try {
                long idEvent = clickedEvent.getId();
                long idUser = Long.parseLong(eventIdTextField.getText());
                System.out.println(idEvent + " " + idUser);
                eventService.removeSubscriber(idEvent, userFacade.getUser(idUser));

                try {
                    eventDataSub.setAll(
                            eventService.findId(clickedEvent.getId()).getSubscribers()
                    );
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                // Show success popup
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("User unsubscribed removed!");
                alert.showAndWait();
            }
            catch (SQLException exception) {
                // Show SQL error popup
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Database Error");
                alert.setHeaderText("Failed to unsubscribed user");
                alert.setContentText(exception.getMessage());
                alert.showAndWait();
            }
            catch (EntityException exception) {
                // Show validation error popup
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Validation Error");
                alert.setHeaderText("Invalid input");
                alert.setContentText("Error unsubscribing user, please check your input!");
                alert.showAndWait();
            }
        });
    }

    private void clearEventFields() {
        eventIdTextField.clear();
        eventNameTextField.clear();
        eventNameTextField.setEditable(true);
        removeEventButton.setDisable(true);
        removeSubButton.setDisable(true);
        addEventButton.setDisable(false);
        addSubButton.setDisable(false);
    }

    private <S, T> TableColumn<S, T> col(String title, String property) {
        TableColumn<S, T> c = new TableColumn<>(title);
        c.setCellValueFactory(new PropertyValueFactory<>(property));
        return c;
    }
}
