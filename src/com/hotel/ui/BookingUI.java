package com.hotel.ui;

import com.hotel.DateUtil;
import com.hotel.Main;
import com.hotel.dao.*;
import com.hotel.domain.Booking;
import com.hotel.domain.Clients;
import com.hotel.domain.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Date;

public class BookingUI extends BorderPane {
    private TextField idField = new TextField();
    private ComboBox<Room> roomComboBox = new ComboBox<Room>();
    private ComboBox<Clients> clientComboBox = new ComboBox<Clients>();
    private DatePicker checkInDateDatePicker = new DatePicker();
    private DatePicker checkOutDateDatePicker = new DatePicker();

    private Button createButton = new Button("New...");
    private Button updateButton = new Button("Update...");
    private Button deleteButton = new Button("Delete...");

    private Button roomButton = new Button("Rooms...");
    private Button clientsButton = new Button("Clients...");

    private TableView<Booking> bookingTableView = new TableView<>();
    private TableColumn<Booking, Long> idColumn = new TableColumn<Booking, Long>("ID");
    private TableColumn<Booking, Room> roomColumn = new TableColumn<Booking, Room>("Номер");
    private TableColumn<Booking, Clients> clientColumn = new TableColumn<Booking, Clients>("Клієнт");
    private TableColumn<Booking, Date> checkInDateColumn = new TableColumn<Booking, Date>("Дата заселення");
    private TableColumn<Booking, Date> checkOutDateColumn = new TableColumn<Booking, Date>("Дата виселення");

    BookingDAO bookingDAO = new BookingDAOImpl();
    RoomDAO roomDAO = new RoomDAOImpl();
    ClientsDAO clientsDAO = new ClientsDAOImpl();

    public BookingUI() {
        setPadding(new Insets(10, 10, 10, 10));
        setTop(new Label());
        setCenter(initFields());
        setRight(initButtons());
        setBottom(initTable());
        setTableData();
        initListeners();
    }

    private Pane initButtons() {
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(5, 20, 20, 20));
        pane.setVgap(10);
        pane.setHgap(20);
        pane.setAlignment(Pos.CENTER);
        pane.add(createButton, 0, 0);
        pane.add(updateButton, 0, 1);
        pane.add(deleteButton, 0, 2);
        pane.add(roomButton, 3, 0);
        pane.add(clientsButton, 3, 1);

        createButton.setOnAction(e -> createButton_Click());
        updateButton.setOnAction(e -> updateButton_Click());
        deleteButton.setOnAction(e -> deleteButton_Click());

        roomButton.setOnAction(e -> {
            Stage stage = Main.getStage();
            stage.close();
            stage.setScene(new Scene(new RoomUI()));
            stage.setWidth(700);
            stage.setTitle("Hotel DataBase --- Rooms");
            stage.show();
        });
        clientsButton.setOnAction(e -> {
            Stage stage = Main.getStage();
            stage.close();
            stage.setScene(new Scene(new ClientsUI()));
            stage.setWidth(1000);
            stage.setTitle("Hotel DataBase --- Clients");
            stage.show();
        });

        return pane;
    }

    private void deleteButton_Click() {
        if (isEmptyFieldData()) {
            MessageBox.show("Choose a row to delete!", "Delete info");
            return;
        }
        if (ConfirmationBox.show("Are you sure you want to delete this row?\n" +
                bookingTableView.getSelectionModel().getSelectedItems(), "Delete confirmation", "Yes", "No")) {
            Booking booking = getFieldsData();
            booking = bookingDAO.getBookingById(booking.getId());
            if (bookingDAO.deleteBooking(booking.getId())) {
                MessageBox.show("Row is deleted successfully!", "Delete info");
                refreshTable();
            }
        }
    }

    private void updateButton_Click() {
        if (isEmptyFieldData()) {
            MessageBox.show("Choose a row to update!", "Update info");
            return;
        }
        if (ConfirmationBox.show("Are you sure you want to update this row?\n" +
                bookingTableView.getSelectionModel().getSelectedItems(), "Update confirmation", "Yes", "No")) {
            Booking booking = getFieldsData();
            if (bookingDAO.updateBooking(booking)) {
                MessageBox.show("Row is updated successfully!", "Update info");
                refreshTable();
            }
        }
    }

    private void createButton_Click() {
        if (createButton.getText().equals("Save")) {
            if (isEmptyFieldData()) {
                MessageBox.show("Cannot create an empty record!", "Create info");
                return;
            }

            Booking booking = getFieldsData();
            if (bookingDAO.insertBooking(booking)) {
                MessageBox.show("New room created successfully!", "Create info");
            }

            createButton.setText("New...");
            refreshTable();
        } else if (createButton.getText().equals("New...")) {
            Booking booking = new Booking();
            booking.setRoom(null);
            booking.setClient(null);
            booking.setCheckInDate(null);
            booking.setCheckOutDate(null);

            setFieldsData(booking);
            createButton.setText("Save");
        }
    }

    private Pane initFields() {
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setHgap(5);
        pane.setVgap(10);

        Label roomLabel = new Label("Room number");
        roomLabel.setTextFill(Color.BLUE);
        roomLabel.setFont(Font.font("Times New Roman", 20));
        Label clientLabel = new Label("Client");
        clientLabel.setTextFill(Color.BLUE);
        clientLabel.setFont(Font.font("Times New Roman", 20));
        Label checkInDateLabel = new Label("Check-in date");
        checkInDateLabel.setTextFill(Color.BLUE);
        checkInDateLabel.setFont(Font.font("Times New Roman", 20));
        Label checkOutDateLabel = new Label("Check-out date");
        checkOutDateLabel.setTextFill(Color.BLUE);
        checkOutDateLabel.setFont(Font.font("Times New Roman", 20));

        roomComboBox.getItems().addAll((roomDAO.getAllRooms()));
        roomComboBox.setMaxWidth(350);
        clientComboBox.getItems().addAll((clientsDAO.getAllClients()));
        clientComboBox.setMaxWidth(350);

        pane.add(roomLabel, 0, 0);
        pane.add(roomComboBox, 1, 0);
        pane.add(clientLabel, 0, 1);
        pane.add(clientComboBox, 1, 1);
        pane.add(checkInDateLabel, 0, 2);
        pane.add(checkInDateDatePicker, 1, 2);
        pane.add(checkOutDateLabel, 0, 3);
        pane.add(checkOutDateDatePicker, 1, 3);

        return pane;
    }

    private Pane initTable() {
        VBox pane = new VBox();
        pane.setPadding(new Insets(10, 10, 10, 10));

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        roomColumn.setCellValueFactory(new PropertyValueFactory<>("room"));
        clientColumn.setCellValueFactory(new PropertyValueFactory<>("client"));
        checkInDateColumn.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));
        checkOutDateColumn.setCellValueFactory(new PropertyValueFactory<>("checkOutDate"));

        bookingTableView.setEditable(true);
        bookingTableView.getColumns().addAll(idColumn, roomColumn, clientColumn, checkInDateColumn, checkOutDateColumn);
        pane.getChildren().add(bookingTableView);

        return pane;
    }

    private ObservableList<Booking> loadData() {
        ObservableList<Booking> masterData = FXCollections.observableArrayList();
        masterData.addAll(bookingDAO.getAllBookings());

        return masterData;
    }

    private void setTableData() {
        bookingTableView.setItems(loadData());
    }

    private void refreshTable() {
        TableView.TableViewFocusModel<Booking> roomModelFocused = bookingTableView.getFocusModel();
        setTableData();
        bookingTableView.setFocusModel(roomModelFocused);
    }

    private void initListeners() {
        bookingTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Booking booking = newSelection;
                setFieldsData(booking);
            }
        });
    }

    private void setFieldsData(Booking booking) {
        idField.setText(String.valueOf(booking.getId()));
        roomComboBox.setValue(booking.getRoom());
        clientComboBox.setValue(booking.getClient());

        if (booking.getCheckInDate() != null) {
            checkInDateDatePicker.setValue(booking.getCheckInDate().toLocalDate());
        } else {
            checkInDateDatePicker.setValue(null);
        }
        if (booking.getCheckOutDate() != null) {
            checkOutDateDatePicker.setValue(booking.getCheckOutDate().toLocalDate());
        } else {
            checkOutDateDatePicker.setValue(null);
        }
    }

    private Booking getFieldsData() {
        Booking booking = new Booking();

        Room room = roomComboBox.getValue();
        Clients clients = clientComboBox.getValue();

        booking.setId(Long.parseLong(idField.getText()));
        booking.setRoom(room);
        booking.setClient(clients);
        booking.setCheckInDate(DateUtil.convertStringIntoSqlDate(checkInDateDatePicker.getValue().toString()));
        if (checkOutDateDatePicker.getValue() != null) {
            booking.setCheckOutDate(DateUtil.convertStringIntoSqlDate(checkOutDateDatePicker.getValue().toString()));
        }

        return booking;
    }

    private boolean isEmptyFieldData() {
        if (roomComboBox.getValue() == null || clientComboBox.getValue() == null || checkInDateDatePicker.getValue() == null) {
            return true;
        } else {
            return false;
        }
    }
}
