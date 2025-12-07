package com.ubb.gui;

import com.ubb.domain.duck.Duck;
import com.ubb.domain.flock.Flock;
import com.ubb.domain.user.User;
import com.ubb.exception.EntityException;
import com.ubb.service.FlockService;
import com.ubb.service.DuckService;
import com.ubb.utils.paging.Pageable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;

public class FlockTabController {

    @FXML
    private TableView<Flock> eventTableView;

    @FXML
    private TableView<Duck> subscribersTableView;

    @FXML
    private Button addFlockButton, removeFlockButton;
    @FXML
    private Button addDuckToFlockButton, removeDuckFromFlockButton;
    @FXML
    private Button previousFlockButton, nextFlockButton;

    @FXML
    private TextField flockIdTextField, flockNameTextField;

    private final FlockService flockService;
    private Flock clickedFlock;

    private final Pageable pageable = new Pageable(0, 4);

    public FlockTabController(FlockService flockService) {
        this.flockService = flockService;
    }

    @FXML
    public void initialize() {

        eventTableView.setEditable(true);
        List<TableColumn<Flock, ?>> columns = List.of(
                col("ID", "id"),
                col("Name", "name")
        );
        eventTableView.getColumns().addAll(columns);

        ObservableList<Flock> flockData = FXCollections.observableArrayList();
        eventTableView.setItems(flockData);
        flockData.setAll(flockService.findAllOnPage(pageable).getElementsOnPage());

        subscribersTableView.setEditable(true);
        List<TableColumn<Duck, ?>> columnsSub = List.of(
                col("ID", "id"),
                col("Username", "username")
        );
        subscribersTableView.getColumns().addAll(columnsSub);
        ObservableList<Duck> duckData = FXCollections.observableArrayList();
        subscribersTableView.setItems(duckData);

        nextFlockButton.setOnAction(e -> {
            pageable.setPageNumber(pageable.getPageNumber() + 1);
            flockData.setAll(flockService.findAllOnPage(pageable).getElementsOnPage());
        });

        previousFlockButton.setOnAction(e -> {
            pageable.setPageNumber(Math.max(0, pageable.getPageNumber() - 1));
            flockData.setAll(flockService.findAllOnPage(pageable).getElementsOnPage());
        });

        // Table row selection for flocks
        eventTableView.setRowFactory(tv -> {
            TableRow<Flock> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    clickedFlock = row.getItem();

                    if (event.getClickCount() == 2) {
                        // Double-click -> fill form
                        flockIdTextField.setText(String.valueOf(clickedFlock.getId()));
                        flockNameTextField.setText(clickedFlock.getName());

                        try {
                            duckData.setAll(flockService.findId(clickedFlock.getId()).getDucks());
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }

                        removeFlockButton.setDisable(false);
                        addFlockButton.setDisable(true);
                        removeDuckFromFlockButton.setDisable(true);

                    } else if (event.getClickCount() == 1) {
                        eventTableView.getSelectionModel().clearSelection();
                        clearFlockFields();
                    }
                } else {
                    clearFlockFields();
                }
            });
            return row;
        });

        // Table row selection for ducks
        subscribersTableView.setRowFactory(tv -> {
            TableRow<Duck> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    Duck clickedDuck = row.getItem();

                    if (event.getClickCount() == 2) {
                        flockNameTextField.setText(String.valueOf(clickedDuck.getId()));
                        removeDuckFromFlockButton.setDisable(false);
                        addDuckToFlockButton.setDisable(true);
                        addFlockButton.setDisable(true);
                        removeFlockButton.setDisable(true);
                    }
                }
            });
            return row;
        });

        addFlockButton.setOnAction(e -> {
            String line = "0," + flockNameTextField.getText();
            try {
                flockService.addObject(line);
                flockData.setAll(flockService.findAllOnPage(pageable).getElementsOnPage());
                showInfo("Flock added successfully!");
            } catch (SQLException ex) {
                showError(ex.getMessage());
            }
        });

        removeFlockButton.setOnAction(e -> {
            try {
                long id = Long.parseLong(flockIdTextField.getText());
                Flock flock = flockService.findId(id);
                flockService.removeObject(flock);
                flockData.setAll(flockService.findAllOnPage(pageable).getElementsOnPage());
                duckData.clear();
                showInfo("Flock removed successfully!");
            } catch (SQLException | EntityException ex) {
                showError(ex.getMessage());
            }
        });

        addDuckToFlockButton.setOnAction(e -> {
            try {
                long flockId = Long.parseLong(flockIdTextField.getText());
                long duckId = Long.parseLong(flockNameTextField.getText());
                flockService.addToFlock(flockId, duckId);
                duckData.setAll(flockService.findId(flockId).getDucks());
                showInfo("Duck added to flock!");
            } catch (SQLException | EntityException ex) {
                showError(ex.getMessage());
            }
        });

        // Remove Duck from Flock
        removeDuckFromFlockButton.setOnAction(e -> {
            try {
                long flockId = clickedFlock.getId();
                long duckId = Long.parseLong(flockNameTextField.getText());
                flockService.removeFromFlock(flockId,duckId);
                duckData.setAll(flockService.findId(flockId).getDucks());
                showInfo("Duck removed from flock!");
            } catch (SQLException | EntityException ex) {
                showError(ex.getMessage());
            }
        });

        removeFlockButton.setDisable(true);
        removeDuckFromFlockButton.setDisable(true);
    }

    private void clearFlockFields() {
        flockIdTextField.clear();
        flockNameTextField.clear();
        flockIdTextField.setEditable(false);
        flockNameTextField.setEditable(true);

        addFlockButton.setDisable(false);
        removeFlockButton.setDisable(true);
        addDuckToFlockButton.setDisable(false);
        removeDuckFromFlockButton.setDisable(true);
    }

    private <S, T> TableColumn<S, T> col(String title, String property) {
        TableColumn<S, T> c = new TableColumn<>(title);
        c.setCellValueFactory(new PropertyValueFactory<>(property));
        return c;
    }

    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg);
        alert.showAndWait();
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.showAndWait();
    }
}
