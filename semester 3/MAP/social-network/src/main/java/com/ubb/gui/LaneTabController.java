package com.ubb.gui;

import com.ubb.domain.lane.Lane;
import com.ubb.exception.EntityException;
import com.ubb.service.LaneService;
import com.ubb.utils.paging.Pageable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class LaneTabController {

    @FXML
    private TableView<Lane> laneTableView;

    @FXML
    private TableColumn<Lane, Long> laneIdColumn;

    @FXML
    private TableColumn<Lane, Integer> laneDistanceColumn;

    @FXML
    private Button prevLaneButton, nextLaneButton;

    @FXML
    private TextField laneIdField, laneDistanceField;

    @FXML
    private Button laneAddButton, laneRemoveButton;

    private final LaneService laneService;
    private final Pageable pageable = new Pageable(0, 4);

    private final ObservableList<Lane> laneData = FXCollections.observableArrayList();

    public LaneTabController(LaneService laneService) {
        this.laneService = laneService;
    }

    @FXML
    public void initialize() {

        laneIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        laneDistanceColumn.setCellValueFactory(new PropertyValueFactory<>("distance"));

        laneTableView.setItems(laneData);

        refreshPage();

        prevLaneButton.setOnAction(e -> {
            pageable.setPageNumber(Math.max(0, pageable.getPageNumber() - 1));
            refreshPage();
        });

        nextLaneButton.setOnAction(e -> {
            pageable.setPageNumber(pageable.getPageNumber() + 1);
            refreshPage();
        });

        laneTableView.setRowFactory(tv -> {
            TableRow<Lane> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    Lane selected = row.getItem();
                    if (event.getClickCount() == 2) {
                        fillForm(selected);
                    }
                } else clearForm();
            });
            return row;
        });

        laneAddButton.setOnAction(e -> addLane());
        laneRemoveButton.setOnAction(e -> removeLane());
    }

    private void refreshPage() {
        laneData.setAll(laneService.findAllOnPage(pageable).getElementsOnPage());
    }

    private void addLane() {
        try {
            String line = "0," + laneDistanceField.getText();
            laneService.addObject(line);
            refreshPage();
            showInfo("Lane added.");
        } catch (SQLException ex) {
            showError(ex.getMessage());
        }
    }

    private void removeLane() {
        try {
            long id = Long.parseLong(laneIdField.getText());
            Lane lane = laneService.findId(id);

            laneService.removeObject(lane);
            refreshPage();
            showInfo("Lane removed.");

        } catch (EntityException ex) {
            showWarn(ex.getMessage());
        } catch (SQLException ex) {
            showError(ex.getMessage());
        }
    }

    private void fillForm(Lane l) {
        laneIdField.setText(String.valueOf(l.getId()));
        laneDistanceField.setText(String.valueOf(l.getDistance()));

        laneAddButton.setDisable(true);
        laneRemoveButton.setDisable(false);

        laneDistanceField.setEditable(false);
    }

    private void clearForm() {
        laneIdField.clear();
        laneDistanceField.clear();

        laneAddButton.setDisable(false);
        laneRemoveButton.setDisable(true);

        laneDistanceField.setEditable(true);
    }

    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }

    private void showWarn(String msg) {
        new Alert(Alert.AlertType.WARNING, msg).showAndWait();
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }
}
