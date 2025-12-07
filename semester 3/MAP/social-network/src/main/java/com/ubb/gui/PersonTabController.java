package com.ubb.gui;

import com.ubb.domain.person.Person;
import com.ubb.exception.EntityException;
import com.ubb.service.PersonService;
import com.ubb.utils.paging.Pageable;
import com.ubb.domain.validator.ValidatorContext;
import com.ubb.domain.validator.ValidatorPerson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;

public class PersonTabController {

    @FXML
    private TableView<Person> personTableView;

    @FXML
    private Button prevPersonButton;

    @FXML
    private Button nextPersonButton;

    @FXML
    private TextField personIdField, personUsernameField, personEmailField, personPasswordField,
            personFirstName, personLastName, personOccupationField, personEmpathyScoreField;

    @FXML
    private Button personAddButton, personRemoveButton;

    private final PersonService personService;

    private final Pageable pageable = new Pageable(0, 4);

    public PersonTabController(PersonService personService) {
        this.personService = personService;
    }

    @FXML
    public void initialize() {

        personTableView.setEditable(true);

        List<TableColumn<Person, ?>> columns = List.of(
                col("ID", "id"),
                col("Username", "username"),
                col("Email", "email"),
                col("Password", "password"),
                col("First Name", "firstName"),
                col("Last Name", "lastName"),
                col("Occupation", "occupation"),
                col("Empathy Score", "empathyScore")
        );

        personTableView.getColumns().addAll(columns);

        ObservableList<Person> personData = FXCollections.observableArrayList();
        personTableView.setItems(personData);

        personData.setAll(
                personService.findAllOnPage(pageable).getElementsOnPage()
        );

        // Pagination Buttons
        nextPersonButton.setOnAction(e -> {
            pageable.setPageNumber(pageable.getPageNumber() + 1);
            personData.setAll(personService.findAllOnPage(pageable).getElementsOnPage());
        });

        prevPersonButton.setOnAction(e -> {
            pageable.setPageNumber(Math.max(0, pageable.getPageNumber() - 1));
            personData.setAll(personService.findAllOnPage(pageable).getElementsOnPage());
        });

        // Table Row Events
        personTableView.setRowFactory(tv -> {
            TableRow<Person> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    Person clicked = row.getItem();

                    if (event.getClickCount() == 2) {
                        fillForm(clicked);
                    } else if (event.getClickCount() == 1) {
                        clearForm();
                    }
                } else clearForm();
            });

            return row;
        });

        // Add Button
        personAddButton.setOnAction(e -> {
            String line = "0,";
            line += personUsernameField.getText() + ",";
            line += personEmailField.getText() + ",";
            line += personPasswordField.getText() + ",";
            line += personFirstName.getText() + ",";
            line += personLastName.getText() + ",";
            line += personOccupationField.getText() + ",";
            line += personEmpathyScoreField.getText();

            ValidatorContext context = new ValidatorContext(new ValidatorPerson());

            if (context.validate(line)) {
                try {
                    personService.addObject(line);
                    personData.setAll(personService.findAllOnPage(pageable).getElementsOnPage());

                    showInfo("Person added successfully!");

                } catch (SQLException ex) {
                    showError(ex.getMessage());
                }
            } else {
                showWarn("Invalid input, please check again!");
            }
        });

        // Remove Button
        personRemoveButton.setOnAction(e -> {
            try {
                var id = Integer.parseInt(personIdField.getText());
                var person = personService.findId(id);
                personService.removeObject(person);

                personData.setAll(personService.findAllOnPage(pageable).getElementsOnPage());

                showInfo("Person removed successfully!");

            } catch (SQLException ex) {
                showError(ex.getMessage());
            } catch (EntityException ex) {
                showWarn(ex.getMessage());
            }
        });

        // Start with remove disabled
        personRemoveButton.setDisable(true);
    }

    // Fill form on double-click
    private void fillForm(Person p) {
        personIdField.setText(String.valueOf(p.getId()));
        personUsernameField.setText(p.getUsername());
        personEmailField.setText(p.getEmail());
        personPasswordField.setText(p.getPassword());
        personFirstName.setText(p.getFirstName());
        personLastName.setText(p.getLastName());
        personOccupationField.setText(p.getOccupation());
        personEmpathyScoreField.setText(String.valueOf(p.getEmpathyScore()));

        personAddButton.setDisable(true);
        personRemoveButton.setDisable(false);

        disableFields(true);
    }

    // Clear form
    private void clearForm() {
        personIdField.clear();
        personUsernameField.clear();
        personEmailField.clear();
        personPasswordField.clear();
        personFirstName.clear();
        personLastName.clear();
        personOccupationField.clear();
        personEmpathyScoreField.clear();

        disableFields(false);
        personAddButton.setDisable(false);
        personRemoveButton.setDisable(true);
    }

    private void disableFields(boolean b) {
        personUsernameField.setEditable(!b);
        personEmailField.setEditable(!b);
        personPasswordField.setEditable(!b);
        personFirstName.setEditable(!b);
        personLastName.setEditable(!b);
        personOccupationField.setEditable(!b);
        personEmpathyScoreField.setEditable(!b);
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

    private void showWarn(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Database Error");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
