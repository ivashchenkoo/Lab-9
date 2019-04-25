package com.hotel.ui;

import com.hotel.ConnectionFactory;
import com.hotel.DateUtil;
import com.hotel.Main;
import com.hotel.dao.*;
import com.hotel.domain.Booking;
import com.hotel.domain.Clients;
import com.hotel.domain.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Date;

public class BookingUI extends BorderPane {
    private MenuBar menuBar;
    private Menu mainMenu = new Menu("_Main");
    private Menu dataBasesMenu = new Menu("_Data Bases");
    private Menu tableMenu = new Menu("_Table");
    private Menu requestMenu = new Menu("_Request...");

    private CustomMenuItem userCustomMenuItem;
    private MenuItem changeUserMenuItem = new MenuItem("_Change DB User");
    private MenuItem exitMenuItem = new MenuItem("_Exit");
    private MenuItem roomsMenuItem = new MenuItem("_Rooms");
    private MenuItem clientsMenuItem = new MenuItem("_Clients");
    private MenuItem createMenuItem = new MenuItem("_New...");
    private MenuItem updateMenuItem = new MenuItem("_Update...");
    private MenuItem deleteMenuItem = new MenuItem("_Delete...");
    private MenuItem clearMenuItem = new MenuItem("_Clear");
    private MenuItem getAllBookingsMenuItem = new MenuItem("Get all bookings");
    private MenuItem getFirstBookingMenuItem = new MenuItem("Get first booking");
    private MenuItem getBookingByIdMenuItem = new MenuItem("Get booking by ID");
    private MenuItem getBookingByAvailableMenuItem = new MenuItem("Get booking by available");

    private TextField idField = new TextField();
    private ComboBox<Room> roomComboBox = new ComboBox<Room>();
    private ComboBox<Clients> clientComboBox = new ComboBox<Clients>();
    private DatePicker checkInDateDatePicker = new DatePicker();
    private DatePicker checkOutDateDatePicker = new DatePicker();

    private Button createButton = new Button("New...");
    private Button updateButton = new Button("Update...");
    private Button deleteButton = new Button("Delete...");
    private Button requestButton = new Button("Request...");
    private Button clearButton = new Button("Clear");

    private Button getAllBookingsButton = new Button("Get all bookings");
    private Button getFirstBookingButton = new Button("Get first booking");
    private Button getBookingByIdButton = new Button("Get booking by ID");
    private Button getBookingByAvailableButton = new Button("Get booking by available");

    private Button roomButton = new Button("Rooms...");
    private Button clientsButton = new Button("Clients...");

    private TableView<Booking> bookingTableView = new TableView<>();
    private TableColumn<Booking, Long> idColumn = new TableColumn<Booking, Long>("ID");
    private TableColumn<Booking, Room> roomColumn = new TableColumn<Booking, Room>("Номер");
    private TableColumn<Booking, Clients> clientColumn = new TableColumn<Booking, Clients>("Клієнт");
    private TableColumn<Booking, Date> checkInDateColumn = new TableColumn<Booking, Date>("Дата заселення");
    private TableColumn<Booking, Date> checkOutDateColumn = new TableColumn<Booking, Date>("Дата виселення");
    private ObservableList<Booking> masterData = FXCollections.observableArrayList();

    BookingDAO bookingDAO = new BookingDAOImpl();
    RoomDAO roomDAO = new RoomDAOImpl();
    ClientsDAO clientsDAO = new ClientsDAOImpl();

    public BookingUI() {
        setTop(initMenuBar());
        setCenter(initFields());
        setRight(initButtons());
        setBottom(initTable());
        loadData();
        setTableData();
        initListeners();
        getStylesheets().add("com/hotel/resources/Simple.css");
    }

    private MenuBar initMenuBar() {
        menuBar = new MenuBar(mainMenu, dataBasesMenu, tableMenu);
        dataBasesMenu.getItems().addAll(roomsMenuItem, clientsMenuItem);
        tableMenu.getItems().addAll(createMenuItem, updateMenuItem, deleteMenuItem, new SeparatorMenuItem(), clearMenuItem, requestMenu);
        requestMenu.getItems().addAll(getAllBookingsMenuItem, getFirstBookingMenuItem, getBookingByIdMenuItem, getBookingByAvailableMenuItem);

        Label userLabel = new Label("User: " + ConnectionFactory.DB_USER);
        userLabel.setId("userLb");
        userCustomMenuItem = new CustomMenuItem(userLabel);
        mainMenu.getItems().addAll(userCustomMenuItem, changeUserMenuItem, new SeparatorMenuItem(), exitMenuItem);

        changeUserMenuItem.setOnAction(e -> {
            boolean confirm = ConfirmationBox.show("Are you sure you want to change user?", "Change user confirmation", "Yes", "No");
            if (confirm) {
                Stage stage = Main.getStage();
                stage.close();
                stage.setWidth(450);
                stage.setHeight(240);
                stage.setScene(new Scene(new SignInUI()));
                stage.setTitle("Sign in to mySql");
                stage.show();
            } else return;
        });
        exitMenuItem.setOnAction(e -> {
            if (ConfirmationBox.show("Are you sure you want to quit?", "Exit confirmation", "Yes", "No"))
                Main.getStage().close();
        });

        roomsMenuItem.setOnAction(e -> roomsButton_Click());
        clientsMenuItem.setOnAction(e -> clientsButton_Click());

        createMenuItem.setOnAction(e -> createButton_Click());
        updateMenuItem.setOnAction(e -> updateButton_Click());
        deleteMenuItem.setOnAction(e -> deleteButton_Click());
        clearMenuItem.setOnAction(e -> clearButton_Click());

        getAllBookingsMenuItem.setOnAction(e -> requestButtons_Clicks(e));
        getFirstBookingMenuItem.setOnAction(e -> requestButtons_Clicks(e));
        getBookingByIdMenuItem.setOnAction(e -> requestButtons_Clicks(e));
        getBookingByAvailableMenuItem.setOnAction(e -> requestButtons_Clicks(e));

        return menuBar;
    }

    private Pane initButtons() {
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(25, 20, 20, 20));
        pane.setVgap(10);
        pane.setHgap(20);
        pane.setAlignment(Pos.TOP_CENTER);
        pane.add(createButton, 0, 0);
        pane.add(updateButton, 0, 1);
        pane.add(deleteButton, 0, 2);
        pane.add(requestButton, 0, 3);
        pane.add(roomButton, 3, 0);
        pane.add(clientsButton, 3, 1);
        pane.add(clearButton, 3, 3);

        createButton.setOnAction(e -> createButton_Click());
        updateButton.setOnAction(e -> updateButton_Click());
        deleteButton.setOnAction(e -> deleteButton_Click());
        requestButton.setOnAction(e -> requestButton_Click());
        clearButton.setOnAction(e -> clearButton_Click());

        roomButton.setOnAction(e -> roomsButton_Click());
        clientsButton.setOnAction(e -> clientsButton_Click());

        return pane;
    }

    private void clearButton_Click() {
        loadData();
        setTableData();
    }

    private void clientsButton_Click() {
        Stage stage = Main.getStage();
        stage.setScene(new Scene(new ClientsUI()));
        stage.setTitle("Hotel DataBase --- Clients");
    }

    private void roomsButton_Click() {
        Stage stage = Main.getStage();
        stage.setScene(new Scene(new RoomUI()));
        stage.setTitle("Hotel DataBase --- Rooms");
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

    private void requestButton_Click() {
        Stage stage = new Stage();
        VBox pane = new VBox();
        pane.setPadding(new Insets(20, 20, 20, 20));
        pane.setSpacing(20);
        pane.setAlignment(Pos.CENTER);
        pane.getChildren().addAll(getAllBookingsButton, getFirstBookingButton, getBookingByIdButton, getBookingByAvailableButton);

        getAllBookingsButton.setOnAction(e -> requestButtons_Clicks(e));
        getFirstBookingButton.setOnAction(e -> requestButtons_Clicks(e));
        getBookingByIdButton.setOnAction(e -> requestButtons_Clicks(e));
        getBookingByAvailableButton.setOnAction(e -> requestButtons_Clicks(e));

        Scene scene = new Scene(pane);
        scene.getStylesheets().add("com/hotel/resources/Boxes.css");

        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Requests");
        stage.getIcons().add(Main.getIcon());
        stage.setWidth(300);
        stage.show();
    }

    private void requestButtons_Clicks(ActionEvent e) {
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
        }
    }

    private Pane initFields() {
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
        masterData.clear();
        masterData.addAll(bookingDAO.getAllBookings());

        return masterData;
    }

    private void setTableData() {
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
