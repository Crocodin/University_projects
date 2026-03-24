package ro.mpp.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import lombok.Setter;
import ro.mpp.domain.*;
import ro.mpp.exceptions.ValidatorException;
import ro.mpp.service.IFestivalService;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MainController {
    private IFestivalService festivalService;
    @Setter private User user;

    @FXML private DatePicker datePicker;

    @FXML private TableView<ShowArtist> fullTableView;
    @FXML private TableColumn<ShowArtist, String> fullArtistColumn;
    @FXML private TableColumn<ShowArtist, String> fullDateColumn;
    @FXML private TableColumn<ShowArtist, String> fullVenueColumn;
    @FXML private TableColumn<ShowArtist, Integer> fullSoldSeatsColumn, fullAvailableSeatsColumn;

    @FXML private TableView<ShowArtist> filteredTableView;
    @FXML private TableColumn<ShowArtist, String> filteredArtistColumn;
    @FXML private TableColumn<ShowArtist, String> filteredVenueColumn;
    @FXML private TableColumn<ShowArtist, String> filteredFromColumn;
    @FXML private TableColumn<ShowArtist, Integer> filteredAvailableSeatsColumn;

    @FXML private Button sellTicketButton, modifyTicketButton;
    @FXML private TextField buyerName, numberOfSeats, ticketId, newNumberOfSeats;

    private ShowArtist selectedShowArtist;
    private LocalDate selectedDate;

    private final ObservableList<ShowArtist> fullshowArtistObservableList = FXCollections.observableArrayList();
    private final ObservableList<ShowArtist> filteredShowArtistObservableList = FXCollections.observableArrayList();

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @FXML private void initialize() {
        fullTableView.setItems(fullshowArtistObservableList);
        filteredTableView.setItems(filteredShowArtistObservableList);

        fullArtistColumn.setCellValueFactory(cell -> {
            ShowArtist showArtist = cell.getValue();
            Artist artist = showArtist.getArtist();
            return new SimpleObjectProperty<>(artist.getName());
        });

        fullDateColumn.setCellValueFactory(cell -> {
            String ts = cell.getValue().getShow().getDate();
            return new SimpleObjectProperty<>(LocalDateTime.parse(ts).format(DATE_FORMATTER));
        });

        fullVenueColumn.setCellValueFactory(cell -> {
            ShowArtist showArtist = cell.getValue();
            Show show = showArtist.getShow();
            return new SimpleObjectProperty<>(show.getVenue().getName());
        });

        fullAvailableSeatsColumn.setCellValueFactory(cell -> {
            ShowArtist showArtist = cell.getValue();
            Show show = showArtist.getShow();
            return new SimpleObjectProperty<>(show.remainingSeats());
        });

        fullSoldSeatsColumn.setCellValueFactory(cell -> {
            ShowArtist showArtist = cell.getValue();
            Show show = showArtist.getShow();
            return new SimpleObjectProperty<>(show.getSoldSeats());
        });

        filteredArtistColumn.setCellValueFactory(cell -> {
            ShowArtist showArtist = cell.getValue();
            Artist artist = showArtist.getArtist();
            return new SimpleObjectProperty<>(artist.getName());
        });

        filteredFromColumn.setCellValueFactory(cell -> {
            String ts = cell.getValue().getShow().getDate();
            return new SimpleObjectProperty<>(LocalDateTime.parse(ts).format(TIME_FORMATTER));
        });

        filteredVenueColumn.setCellValueFactory(cell -> {
            ShowArtist showArtist = cell.getValue();
            Show show = showArtist.getShow();
            return new SimpleObjectProperty<>(show.getVenue().getName());
        });

        filteredAvailableSeatsColumn.setCellValueFactory(cell -> {
            ShowArtist showArtist = cell.getValue();
            Show show = showArtist.getShow();
            return new SimpleObjectProperty<>(show.remainingSeats());
        });

        fullTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                selectedShowArtist = fullshowArtistObservableList.get(fullTableView.getSelectionModel().getSelectedIndex());
            }
        });

        filteredTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                selectedShowArtist = filteredShowArtistObservableList.get(filteredTableView.getSelectionModel().getSelectedIndex());
            }
        });

        fullTableView.setRowFactory(tv -> new TableRow<ShowArtist>() {
            @Override
            protected void updateItem(ShowArtist item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().remove("sold-out");
                if (item != null && !empty && item.getShow().isSoldOut()) {
                    getStyleClass().add("sold-out");
                }
            }
        });

        filteredTableView.setRowFactory(tv -> new TableRow<ShowArtist>() {
            @Override
            protected void updateItem(ShowArtist item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().remove("sold-out");
                if (item != null && !empty && item.getShow().isSoldOut()) {
                    getStyleClass().add("sold-out");
                }
            }
        });

        buyerName.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                numberOfSeats.requestFocus();
            }
        });

        numberOfSeats.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                sellTicketButton.fire();
            }
        });

        ticketId.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                newNumberOfSeats.requestFocus();
            }
        });

        newNumberOfSeats.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                modifyTicketButton.fire();
            }
        });
    }

    public void setFestivalService(IFestivalService festivalService) {
        this.festivalService = festivalService;

        this.refresh();
    }

    private void refresh() {
        this.fullshowArtistObservableList.clear();
        fullshowArtistObservableList.addAll(this.festivalService.findAll());

        if (this.selectedDate != null) {
            this.filteredShowArtistObservableList.clear();
            filteredShowArtistObservableList.addAll(this.festivalService.findByDate(this.selectedDate));
        }
    }

    public void sellTicketAction(ActionEvent actionEvent) {
        if (selectedShowArtist == null) {
            this.showError("Please select a show first!");
            return;
        }
        try {
            int numberOfSeats = Integer.parseInt(this.numberOfSeats.getText());
            String buyerName = this.buyerName.getText();

            this.festivalService.sellTicket(selectedShowArtist.getShow(), buyerName, numberOfSeats);
            this.refresh();
            this.showInfo("Ticket has been successfully sold!");
        } catch (NumberFormatException e) {
            numberOfSeats.clear();
            this.showError("Please enter a valid number!");
        } catch (Exception e) {
            numberOfSeats.clear();
            this.showError(e.getMessage());
        }
    }

    public void modifyTicketAction(ActionEvent actionEvent) {
        try {
            int ticketId = Integer.parseInt(this.ticketId.getText());
            int numberOfSeats = Integer.parseInt(this.newNumberOfSeats.getText());
            festivalService.modifyTicket(ticketId, numberOfSeats);
            this.refresh();
            this.showInfo("Ticket has been successfully modified!");

        } catch (NumberFormatException e) {
            this.showError("Please enter a valid number!");
        } catch (Exception e) {
            this.showError(e.getMessage());
        } finally {
            numberOfSeats.clear();
            ticketId.clear();
        }
    }

    private void showError(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(error);
        alert.showAndWait();
    }

    private void showInfo(String info) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(info);
        alert.showAndWait();
    }

    public void setFilterDate(ActionEvent actionEvent) {
        this.selectedDate = datePicker.getValue();
        this.refresh();
    }
}
