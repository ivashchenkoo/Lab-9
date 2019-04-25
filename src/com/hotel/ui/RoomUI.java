package com.hotel.ui;

import com.hotel.ConnectionFactory;
import com.hotel.Main;
import com.hotel.dao.RoomDAO;
import com.hotel.dao.RoomDAOImpl;
import com.hotel.domain.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;

public class RoomUI extends BorderPane {
    private MenuBar menuBar;
    private Menu mainMenu = new Menu("_Main");
    private Menu dataBasesMenu = new Menu("_Data Bases");
    private Menu tableMenu = new Menu("_Table");
    private Menu requestMenu = new Menu("_Request...");

    private CustomMenuItem userCustomMenuItem;
    private MenuItem changeUserMenuItem = new MenuItem("_Change DB User");
    private MenuItem exitMenuItem = new MenuItem("_Exit");
    private MenuItem bookingMenuItem = new MenuItem("_Booking");
    private MenuItem clientsMenuItem = new MenuItem("_Clients");
    private MenuItem createMenuItem = new MenuItem("_New...");
    private MenuItem updateMenuItem = new MenuItem("_Update...");
    private MenuItem deleteMenuItem = new MenuItem("_Delete...");
    private MenuItem clearMenuItem = new MenuItem("_Clear");
    private MenuItem getAllRoomsMenuItem = new MenuItem("Get all rooms");
    private MenuItem getFirstRoomMenuItem = new MenuItem("Get first room");
    private MenuItem getRoomByIdMenuItem = new MenuItem("Get room by ID");
    private MenuItem getRoomBySeatsAndMaxPriceMenuItem = new MenuItem("Get room by seats and max price");

    private TextField idField = new TextField();
    private TextField numberField = new TextField();
    private TextField seatsField = new TextField();
    private TextField priceField = new TextField();
    private TextField descriptionField = new TextField();

    private Button createButton = new Button("New...");
    private Button updateButton = new Button("Update...");
    private Button deleteButton = new Button("Delete...");
    private Button requestButton = new Button("Request...");
    private Button clearButton = new Button("Clear");

    private Button getAllRoomsButton = new Button("Get all rooms");
    private Button getFirstRoomButton = new Button("Get first room");
    private Button getRoomByIdButton = new Button("Get room by ID");
    private Button getRoomBySeatsAndMaxPriceButton = new Button("Get room by seats and max price");

    private Button clientsButton = new Button("Clients...");
    private Button bookingButton = new Button("Booking...");

    private TableView<Room> roomTableView = new TableView<>();
    private TableColumn<Room, Long> idColumn = new TableColumn<>("ID");
    private TableColumn<Room, Integer> numberColumn = new TableColumn<>("Number");
    private TableColumn<Room, Integer> seatsColumn = new TableColumn<>("Seats");
    private TableColumn<Room, Integer> priceColumn = new TableColumn<>("Price");
    private TableColumn<Room, String> descriptionColumn = new TableColumn<>("Description");
    private ObservableList<Room> masterData = FXCollections.observableArrayList();


    private RoomDAO roomDAO = new RoomDAOImpl();

    public RoomUI() {
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
        dataBasesMenu.getItems().addAll(bookingMenuItem, clientsMenuItem);
        tableMenu.getItems().addAll(createMenuItem, updateMenuItem, deleteMenuItem, new SeparatorMenuItem(), clearMenuItem, requestMenu);
        requestMenu.getItems().addAll(getAllRoomsMenuItem, getFirstRoomMenuItem, getRoomByIdMenuItem, getRoomBySeatsAndMaxPriceMenuItem);

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

        bookingMenuItem.setOnAction(e -> bookingButton_Click());
        clientsMenuItem.setOnAction(e -> clientsButton_Click());

        createMenuItem.setOnAction(e -> createButton_Click());
        updateMenuItem.setOnAction(e -> updateButton_Click());
        deleteMenuItem.setOnAction(e -> deleteButton_Click());
        clearMenuItem.setOnAction(e -> clearButton_Click());

        getAllRoomsMenuItem.setOnAction(e -> requestButtons_Clicks(e));
        getFirstRoomMenuItem.setOnAction(e -> requestButtons_Clicks(e));
        getRoomByIdMenuItem.setOnAction(e -> requestButtons_Clicks(e));
        getRoomBySeatsAndMaxPriceMenuItem.setOnAction(e -> requestButtons_Clicks(e));

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
        pane.add(bookingButton, 3, 0);
        pane.add(clientsButton, 3, 1);
        pane.add(clearButton, 3, 3);

        createButton.setOnAction(e -> createButton_Click());
        updateButton.setOnAction(e -> updateButton_Click());
        deleteButton.setOnAction(e -> deleteButton_Click());
        requestButton.setOnAction(e -> requestButton_Click());
        clearButton.setOnAction(e -> clearButton_Click());

        bookingButton.setOnAction(e -> bookingButton_Click());
        clientsButton.setOnAction(e -> clientsButton_Click());

        return pane;
    }

    private void clearButton_Click() {
        loadData();
        setTableData();
    }

    private void requestButton_Click() {
        Stage stage = new Stage();
        VBox pane = new VBox();
        pane.setPadding(new Insets(20, 20, 20, 20));
        pane.setSpacing(20);
        pane.setAlignment(Pos.CENTER);
        pane.getChildren().addAll(getAllRoomsButton, getFirstRoomButton, getRoomByIdButton, getRoomBySeatsAndMaxPriceButton);

        getAllRoomsButton.setOnAction(e -> requestButtons_Clicks(e));
        getFirstRoomButton.setOnAction(e -> requestButtons_Clicks(e));
        getRoomByIdButton.setOnAction(e -> requestButtons_Clicks(e));
        getRoomBySeatsAndMaxPriceButton.setOnAction(e -> requestButtons_Clicks(e));

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
        if (e.getSource().equals(getAllRoomsButton) || e.getSource().equals(getAllRoomsMenuItem)) {
            loadData();
            setTableData();
        } else if (e.getSource().equals(getFirstRoomButton) || e.getSource().equals(getFirstRoomMenuItem)) {
            masterData.clear();
            masterData.addAll(roomDAO.getFirstRoom());
            setTableData();
        } else if (e.getSource().equals(getRoomByIdButton) || e.getSource().equals(getRoomByIdMenuItem)) {
            long id;
            try {
                String idStr = InputBox.show("Enter room ID:", "Input data");
                if (idStr.equals("")) return;
                id = Long.parseLong(idStr);
                masterData.clear();
                masterData.addAll(roomDAO.getRoomByID(id));
                setTableData();
            } catch (NumberFormatException ex) {
                MessageBox.show("ID number must be a Long value", "NumberFormatException");
                requestButtons_Clicks(e);
            }
        } else if (e.getSource().equals(getRoomBySeatsAndMaxPriceButton) || e.getSource().equals(getRoomBySeatsAndMaxPriceMenuItem)) {
            int seats, price;
            try {
                String seatsStr = InputBox.show("Enter seats count:", "Input data");
                if (seatsStr.equals("")) return;
                seats = Integer.parseInt(seatsStr);
                String priceStr = InputBox.show("Enter max price:", "Input data");
                if (priceStr.equals("")) return;
                price = Integer.parseInt(priceStr);
                masterData.clear();
                masterData.addAll(roomDAO.getRoomBySeatsAndMaxPrice(seats, price));
                setTableData();
            } catch (NumberFormatException ex) {
                MessageBox.show("This value must be an Integer", "NumberFormatException");
                requestButtons_Clicks(e);
            }
        }
    }

    private void clientsButton_Click() {
        Stage stage = Main.getStage();
        stage.setScene(new Scene(new ClientsUI()));
        stage.setTitle("Hotel DataBase --- Clients");
    }

    private void bookingButton_Click() {
        Stage stage = Main.getStage();
        stage.setScene(new Scene(new BookingUI()));
        stage.setTitle("Hotel DataBase --- Booking");
    }

    private void deleteButton_Click() {
        if (numberField.getText().equals("") || seatsField.getText().equals("") || priceField.getText().equals("") ||
                numberField.getText().equals("0") || seatsField.getText().equals("0") || priceField.getText().equals("0")) {
            MessageBox.show("Choose a row to delete!", "Delete info");
            return;
        }
        if (ConfirmationBox.show("Are you sure you want to delete this row?\n" +
                roomTableView.getSelectionModel().getSelectedItems(), "Delete confirmation", "Yes", "No")) {
            Room room = null;
            try {
                room = getFieldsData();
            } catch (IOException e) {
                return;
            }
            room = roomDAO.getRoomByID(room.getId());
            if (roomDAO.deleteRoom(room.getId())) {
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
                roomTableView.getSelectionModel().getSelectedItems(), "Update confirmation", "Yes", "No")) {
            Room room = null;
            try {
                room = getFieldsData();
            } catch (IOException e) {
                return;
            }
            if (roomDAO.updateRoom(room)) {
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

            Room room = null;
            try {
                room = getFieldsData();
            } catch (IOException e) {
                return;
            }
            if (roomDAO.insertRoom(room)) {
                MessageBox.show("New room created successfully!", "Create info");
            }

            createButton.setText("New...");
            createMenuItem.setText("_New...");
            refreshTable();
        } else if (createButton.getText().equals("New...")) {
            Room room = new Room();
            room.setNumber(0);
            room.setSeats(0);
            room.setPrice(0);
            room.setDescription("");

            setFieldsData(room);
            createButton.setText("Save");
            createMenuItem.setText("_Save");
        }
    }

    private Pane initFields() {
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.TOP_CENTER);
        pane.setPadding(new Insets(25, 10, 10, 10));
        pane.setHgap(5);
        pane.setVgap(10);

        Label numberFieldLabel = new Label("Room number");
        numberFieldLabel.setTextFill(Color.BLUE);
        Label seatsFieldLabel = new Label("Seats");
        seatsFieldLabel.setTextFill(Color.BLUE);
        Label priceFieldLabel = new Label("Price");
        priceFieldLabel.setTextFill(Color.BLUE);
        Label descriptionFieldLabel = new Label("Description");
        descriptionFieldLabel.setTextFill(Color.BLUE);

        pane.add(numberFieldLabel, 0, 0);
        pane.add(numberField, 1, 0);
        pane.add(seatsFieldLabel, 0, 1);
        pane.add(seatsField, 1, 1);
        pane.add(priceFieldLabel, 0, 2);
        pane.add(priceField, 1, 2);
        pane.add(descriptionFieldLabel, 0, 3);
        pane.add(descriptionField, 1, 3);

        return pane;
    }

    private Pane initTable() {
        VBox pane = new VBox();
        pane.setPadding(new Insets(10, 10, 10, 10));

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        numberColumn.setCellValueFactory(new PropertyValueFactory<>("Number"));
        numberColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        numberColumn.setOnEditCommit(e -> numberColumn_OnEditCommit(e));
        numberColumn.setMinWidth(100);

        seatsColumn.setCellValueFactory(new PropertyValueFactory<>("Seats"));
        seatsColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        seatsColumn.setOnEditCommit(e -> seatsColumn_OnEditCommit(e));
        seatsColumn.setMinWidth(100);

        priceColumn.setCellValueFactory(new PropertyValueFactory<>("Price"));
        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        priceColumn.setOnEditCommit(e -> priceColumn_OnEditCommit(e));
        priceColumn.setMinWidth(100);

        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionColumn.setOnEditCommit(e -> descriptionColumn_OnEditCommit(e));

        roomTableView.setEditable(true);
        roomTableView.getColumns().addAll(idColumn, numberColumn, seatsColumn, priceColumn, descriptionColumn);
        pane.getChildren().add(roomTableView);

        return pane;
    }

    private void descriptionColumn_OnEditCommit(TableColumn.CellEditEvent<Room, String> e) {
        Room room = e.getRowValue();
        room.setDescription(e.getNewValue());
        roomDAO.updateRoom(room);
    }

    private void priceColumn_OnEditCommit(TableColumn.CellEditEvent<Room, Integer> e) {
        Room room = e.getRowValue();
        room.setPrice(e.getNewValue());
        roomDAO.updateRoom(room);
    }

    private void seatsColumn_OnEditCommit(TableColumn.CellEditEvent<Room, Integer> e) {
        Room room = e.getRowValue();
        room.setSeats(e.getNewValue());
        roomDAO.updateRoom(room);
    }

    private void numberColumn_OnEditCommit(TableColumn.CellEditEvent<Room, Integer> e) {
        Room room = e.getRowValue();
        room.setNumber(e.getNewValue());
        roomDAO.updateRoom(room);
    }

    private ObservableList<Room> loadData() {
        masterData.clear();
        masterData.addAll(roomDAO.getAllRooms());

        return masterData;
    }

    private void setTableData() {
        roomTableView.setItems(masterData);
    }

    private void refreshTable() {
        TableView.TableViewFocusModel<Room> roomModelFocused = roomTableView.getFocusModel();
        loadData();
        setTableData();
        roomTableView.setFocusModel(roomModelFocused);
    }

    private void initListeners() {
        roomTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Room room = newSelection;
                setFieldsData(room);
            }
        });
    }

    private void setFieldsData(Room room) {
        idField.setText(String.valueOf(room.getId()));
        numberField.setText(String.valueOf(room.getNumber()));
        seatsField.setText(String.valueOf(room.getSeats()));
        priceField.setText(String.valueOf(room.getPrice()));
        descriptionField.setText(room.getDescription());
    }

    private Room getFieldsData() throws IOException {
        Room room = new Room();
        try {
            room.setNumber(Integer.parseInt(numberField.getText()));
        } catch (NumberFormatException ex) {
            MessageBox.show("Room number must be an Integer", "NumberFormatException");
            numberField.clear();
            throw new IOException();
        }
        try {
            room.setSeats(Integer.parseInt(seatsField.getText()));
        } catch (NumberFormatException ex) {
            MessageBox.show("Seats number must be an Integer", "NumberFormatException");
            seatsField.clear();
            throw new IOException();
        }
        try {
            room.setPrice(Integer.parseInt(priceField.getText()));
        } catch (NumberFormatException ex) {
            MessageBox.show("Price number must be an Integer", "NumberFormatException");
            priceField.clear();
            throw new IOException();
        }
        room.setDescription(descriptionField.getText());

        return room;
    }

    private boolean isEmptyFieldData() {
        if (numberField.getText().equals("0") || seatsColumn.getText().equals("0") || priceField.getText().equals("0")) {
            return true;
        } else {
            return false;
        }
    }
}