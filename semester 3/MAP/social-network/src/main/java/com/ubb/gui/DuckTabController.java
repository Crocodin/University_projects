package com.ubb.gui;

import com.ubb.domain.duck.Duck;
import com.ubb.domain.duck.RaceType;
import com.ubb.dto.DuckFilterDTO;
import com.ubb.exception.EntityException;
import com.ubb.service.DuckService;
import com.ubb.utils.paging.Pageable;
import com.ubb.domain.validator.ValidatorContext;
import com.ubb.domain.validator.ValidatorDuck;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DuckTabController {

    @FXML
    private TableView<Duck> duckTableView;

    @FXML
    private ComboBox<String> duckComboBox;

    @FXML
    private Button prevDuckButton;

    @FXML
    private Button nextDuckButton;

    @FXML
    private TextField duckIdField, duckUsernameField, duckPasswordField, duckEmailField, duckSpeedField, duckEnduranceField;

    @FXML
    private ComboBox<String> duckTypeComboBox;

    @FXML
    private Button duckAddButton, duckRemoveButton;

    private final DuckService duckService;

    private final Pageable pageable = new Pageable(0, 4);

    private DuckFilterDTO dto = new DuckFilterDTO();

    public DuckTabController(DuckService duckService) {
        this.duckService = duckService;
    }

    @FXML
    public void initialize() {

        duckTableView.setEditable(true);
        List<TableColumn<Duck, ?>> columns = List.of(
                col("ID", "id"),
                col("Username", "username"),
                col("Email", "email"),
                col("Type", "type"),
                col("Speed", "speed"),
                col("Endurance", "endurance")
        );

        duckTableView.getColumns().addAll(columns);
        ObservableList<Duck> duckData = FXCollections.observableArrayList();
        duckTableView.setItems(duckData);

        duckData.setAll(
                duckService.findAllOnPage(pageable).getElementsOnPage()
        );

        ObservableList<String> options = FXCollections.observableArrayList("ALL DUCKS", "FLYING_AND_SWIMMING", "FLYING", "SWIMMING");
        duckComboBox.setItems(options);
        duckComboBox.setValue(options.getFirst());
        duckComboBox.setOnAction(event -> {
            pageable.setPageNumber(0);

            if (duckComboBox.getValue().equals(options.getFirst())) {
                dto = new DuckFilterDTO();
                duckData.setAll(duckService.findAllOnPage(pageable, dto).getElementsOnPage());
            } else {
                dto = new DuckFilterDTO();
                dto.setType(Optional.of(RaceType.valueOf(duckComboBox.getValue())));
                duckData.setAll(duckService.findAllOnPage(pageable, dto).getElementsOnPage());
            }
        });

        ObservableList<String> optionsComboBox = FXCollections.observableArrayList("FLYING_AND_SWIMMING", "FLYING", "SWIMMING");
        duckTypeComboBox.setItems(optionsComboBox);

        this.nextDuckButton.setOnAction(e -> {
            pageable.setPageNumber(pageable.getPageNumber() + 1);

            if (dto.getType().isEmpty()) {
                duckData.setAll(duckService.findAllOnPage(pageable).getElementsOnPage());
            }
            else duckData.setAll(duckService.findAllOnPage(pageable, dto).getElementsOnPage());
        });
        this.prevDuckButton.setOnAction(e -> {
            pageable.setPageNumber(pageable.getPageNumber() - 1);

            if (dto.getType().isEmpty()) {
                duckData.setAll(duckService.findAllOnPage(pageable).getElementsOnPage());
            }
            else duckData.setAll(duckService.findAllOnPage(pageable, dto).getElementsOnPage());
        });

        duckTableView.setRowFactory(tv -> {
            TableRow<Duck> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    Duck clickedDuck = row.getItem();

                    if (event.getClickCount() == 2) {
                        // Double-click -> fill the form
                        duckIdField.setText(String.valueOf(clickedDuck.getId()));
                        duckUsernameField.setText(clickedDuck.getUsername());
                        duckEmailField.setText(clickedDuck.getEmail());
                        duckPasswordField.setText(clickedDuck.getPassword());
                        duckTypeComboBox.setValue(clickedDuck.getType().toString());
                        duckSpeedField.setText(String.valueOf(clickedDuck.getSpeed()));
                        duckEnduranceField.setText(String.valueOf(clickedDuck.getEndurance()));

                        duckUsernameField.setEditable(false);
                        duckEmailField.setEditable(false);
                        duckPasswordField.setEditable(false);
                        duckTypeComboBox.setDisable(true);
                        duckSpeedField.setEditable(false);
                        duckEnduranceField.setEditable(false);
                        duckAddButton.setDisable(true);
                        duckRemoveButton.setDisable(false);

                    } else if (event.getClickCount() == 1) {
                        // Single-click on a different row -> clear form
                        duckTableView.getSelectionModel().clearSelection();
                        clearDuckFields();
                    }
                } else {
                    // Clicked empty space -> clear form
                    clearDuckFields();
                }
            });

            return row;
        });

        duckAddButton.setOnAction(e -> {
            String line = "0,";
            line += duckUsernameField.getText() + ",";
            line += duckEmailField.getText() + ",";
            line += duckPasswordField.getText() + ",";
            line += duckTypeComboBox.getValue() + ",";
            line += duckSpeedField.getText() + ",";
            line += duckEnduranceField.getText();

            ValidatorContext context = new ValidatorContext(new ValidatorDuck());

            if (context.validate(line)) {
                try {
                    duckService.addObject(line);
                    duckData.setAll(duckService.findAllOnPage(pageable, dto).getElementsOnPage());

                    // Show success popup
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("User successfully added!");
                    alert.showAndWait();

                } catch (SQLException exception) {
                    // Show SQL error popup
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Database Error");
                    alert.setHeaderText("Failed to add user");
                    alert.setContentText(exception.getMessage());
                    alert.showAndWait();
                }
            } else {
                // Show validation error popup
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Validation Error");
                alert.setHeaderText("Invalid input");
                alert.setContentText("Error adding the user, please check your input!");
                alert.showAndWait();
            }
        });

        duckRemoveButton.setOnAction(e -> {
            try {
                var id = Integer.parseInt(duckIdField.getText());
                var duck = duckService.findId(id);
                duckService.removeObject(duck);
                duckData.setAll(duckService.findAllOnPage(pageable, dto).getElementsOnPage());

                // Show success popup
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("User successfully removed!");
                alert.showAndWait();
            }
            catch (SQLException exception) {
                // Show SQL error popup
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Database Error");
                alert.setHeaderText("Failed to add user");
                alert.setContentText(exception.getMessage());
                alert.showAndWait();
            }
            catch (EntityException exception) {
                // Show validation error popup
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Validation Error");
                alert.setHeaderText("Invalid input");
                alert.setContentText("Error adding the user, please check your input!");
                alert.showAndWait();
            }
        });
    }

    private void clearDuckFields() {
        duckIdField.clear();
        duckUsernameField.clear();
        duckEmailField.clear();
        duckPasswordField.clear();
        duckTypeComboBox.setValue(null);
        duckSpeedField.clear();
        duckEnduranceField.clear();

        duckUsernameField.setEditable(true);
        duckEmailField.setEditable(true);
        duckPasswordField.setEditable(true);
        duckTypeComboBox.setDisable(false);
        duckSpeedField.setEditable(true);
        duckEnduranceField.setEditable(true);
        duckAddButton.setDisable(false);
        duckRemoveButton.setDisable(true);
    }

    private <S, T> TableColumn<S, T> col(String title, String property) {
        TableColumn<S, T> c = new TableColumn<>(title);
        c.setCellValueFactory(new PropertyValueFactory<>(property));
        return c;
    }
}
