package com.hotel.ui;

import com.hotel.DateUtil;
import com.hotel.dao.*;
import com.hotel.domain.Booking;
import com.hotel.domain.Clients;
import com.hotel.domain.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.sql.Date;

public class BookingUI extends AbstractMainUI {

    private MenuItem getAllBookingsMenuItem = new MenuItem("Get all bookings");
    private MenuItem getFirstBookingMenuItem = new MenuItem("Get first booking");
    private MenuItem getBookingByIdMenuItem = new MenuItem("Get booking by ID");
    private MenuItem getBookingByAvailableMenuItem = new MenuItem("Get booking by available");
    private MenuItem getBookingByRoomNumberMenuItem = new MenuItem("Get booking by room number");
    private MenuItem getBookingByClientNameMenuItem = new MenuItem("Get booking by client`s name");

    private Button getAllBookingsButton = new Button("Get all bookings");
    private Button getFirstBookingButton = new Button("Get first booking");
    private Button getBookingByIdButton = new Button("Get booking by ID");
    private Button getBookingByAvailableButton = new Button("Get booking by available");
    private Button getBookingByRoomNumberButton = new Button("Get booking by room number");
    private Button getBookingByClientNameButton = new Button("Get booking by client`s name");

    private TextField idField = new TextField();
    private ComboBox<Room> roomComboBox = new ComboBox<Room>();
    private ComboBox<Clients> clientComboBox = new ComboBox<Clients>();
    private DatePicker checkInDateDatePicker = new DatePicker();
    private DatePicker checkOutDateDatePicker = new DatePicker();

    private TableView<Booking> bookingTableView = new TableView<>();
    private TableColumn<Booking, Long> idColumn = new TableColumn<>("ID");
    private TableColumn<Booking, Room> roomColumn = new TableColumn<>("Room");
    private TableColumn<Booking, Clients> clientColumn = new TableColumn<>("Client");
    private TableColumn<Booking, Date> checkInDateColumn = new TableColumn<>("Check-in date");
    private TableColumn<Booking, Date> checkOutDateColumn = new TableColumn<>("Check-out date");
    private ObservableList<Booking> masterData = FXCollections.observableArrayList();

    BookingDAO bookingDAO = new BookingDAOImpl();
    RoomDAO roomDAO = new RoomDAOImpl();
    ClientsDAO clientsDAO = new ClientsDAOImpl();

    public BookingUI() {
        setTop(initMenuBar(getAllBookingsMenuItem, getFirstBookingMenuItem, getBookingByIdMenuItem, getBookingByAvailableMenuItem, getBookingByRoomNumberMenuItem, getBookingByClientNameMenuItem));
        setCenter(initFields());
        setRight(initButtons(getAllBookingsButton, getFirstBookingButton, getBookingByIdButton, getBookingByAvailableButton, getBookingByRoomNumberButton, getBookingByClientNameButton));
        setBottom(initTable());
        loadData();
        setTableData();
        initListeners();
        getStylesheets().add("com/hotel/resources/Simple.css");
    }

    protected void deleteButton_Click() {
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

    protected void updateButton_Click() {
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

    protected void createButton_Click() {
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
            createMenuItem.setText("_New...");
            refreshTable();
        } else if (createButton.getText().equals("New...")) {
            Booking booking = new Booking();
            booking.setRoom(null);
            booking.setClient(null);
            booking.setCheckInDate(null);
            booking.setCheckOutDate(null);

            setFieldsData(booking);
            createButton.setText("Save");
            createMenuItem.setText("_Save");
        }
    }

    protected void requestButtons_Clicks(ActionEvent e) {
        if (e.getSource().equals(getAllBookingsButton) || e.getSource().equals(getAllBookingsMenuItem)) {
            loadData();
            setTableData();
        } else if (e.getSource().equals(getFirstBookingButton) || e.getSource().equals(getFirstBookingMenuItem)) {
            masterData.clear();
            masterData.addAll(bookingDAO.getFirstBooking());
            setTableData();
        } else if (e.getSource().equals(getBookingByIdButton) || e.getSource().equals(getBookingByIdMenuItem)) {
            long id;
            try {
                String idStr = InputBox.show("Enter booking ID:", "Input data");
                if (idStr.equals("")) return;
                id = Long.parseLong(idStr);
                masterData.clear();
                masterData.addAll(bookingDAO.getBookingById(id));
                setTableData();
            } catch (NumberFormatException ex) {
                MessageBox.show("ID number must be a Long value", "NumberFormatException");
                requestButtons_Clicks(e);
            }
        } else if (e.getSource().equals(getBookingByAvailableButton) || e.getSource().equals(getBookingByAvailableMenuItem)) {
            boolean isAvailable;
            isAvailable = ConfirmationBox.show("Turn the availability", "Input data", "Is available", "Is not available");
            masterData.clear();
            masterData.addAll(bookingDAO.getBookingByAvailable(isAvailable));
            setTableData();
        } else if (e.getSource().equals(getBookingByRoomNumberButton) || e.getSource().equals(getBookingByRoomNumberMenuItem)) {
            int roomNumber;
            try {
                String idStr = InputBox.show("Enter room number:", "Input data");
                if (idStr.equals("")) return;
                roomNumber = Integer.parseInt(idStr);
                masterData.clear();
                masterData.addAll(bookingDAO.getBookingByRoomNumber(roomNumber));
                setTableData();
            } catch (NumberFormatException ex) {
                MessageBox.show("Room number must be an Integer value", "NumberFormatException");
                requestButtons_Clicks(e);
            }
        } else if (e.getSource().equals(getBookingByClientNameButton) || e.getSource().equals(getBookingByClientNameMenuItem)) {
            String firstName = InputBox.show("Enter client`s first name:", "Input data");
            if (firstName.equals("")) return;
            String lastName = InputBox.show("Enter client`s last name:", "Input data");
            if (lastName.equals("")) return;
            masterData.clear();
            masterData.addAll(bookingDAO.getBookingByClientName(firstName, lastName));
            setTableData();
        }
    }

    protected Pane initFields() {
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.TOP_CENTER);
        pane.setPadding(new Insets(25, 10, 10, 10));
        pane.setHgap(5);
        pane.setVgap(10);

        Label roomLabel = new Label("Room number");
        Label clientLabel = new Label("Client");
        Label checkInDateLabel = new Label("Check-in date");
        Label checkOutDateLabel = new Label("Check-out date");

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

    protected Pane initTable() {
        VBox pane = new VBox();
        pane.setPadding(new Insets(10, 10, 10, 10));

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        roomColumn.setCellValueFactory(new PropertyValueFactory<>("room"));
        clientColumn.setCellValueFactory(new PropertyValueFactory<>("client"));
        checkInDateColumn.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));
        checkOutDateColumn.setCellValueFactory(new PropertyValueFactory<>("checkOutDate"));

        roomColumn.setPrefWidth(350);
        clientColumn.setPrefWidth(350);

        bookingTableView.setEditable(true);
        bookingTableView.getColumns().addAll(idColumn, roomColumn, clientColumn, checkInDateColumn, checkOutDateColumn);
        pane.getChildren().add(bookingTableView);

        return pane;
    }

    protected void loadData() {
        masterData.clear();
        masterData.addAll(bookingDAO.getAllBookings());
    }

    protected void setTableData() {
        bookingTableView.setItems(masterData);
    }

    private void refreshTable() {
        TableView.TableViewFocusModel<Booking> roomModelFocused = bookingTableView.getFocusModel();
        loadData();
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
