package com.hotel.ui;

import com.hotel.Main;
import com.hotel.dao.RoomDAO;
import com.hotel.dao.RoomDAOImpl;
import com.hotel.domain.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

public class RoomUI extends BorderPane {
    private TextField idField = new TextField();
    private TextField numberField = new TextField();
    private TextField seatsField = new TextField();
    private TextField priceField = new TextField();
    private TextField descriptionField = new TextField();

    private Button createButton = new Button("New...");
    private Button updateButton = new Button("Update...");
    private Button deleteButton = new Button("Delete...");

    private Button clientsButton = new Button("Clients...");
    private Button bookingButton = new Button("Booking...");

    private TableView<Room> roomTableView = new TableView<>();
    private TableColumn<Room, Long> idColumn = new TableColumn<>("ID");
    private TableColumn<Room, Integer> numberColumn = new TableColumn<>("Number");
    private TableColumn<Room, Integer> seatsColumn = new TableColumn<>("Seats");
    private TableColumn<Room, Integer> priceColumn = new TableColumn<>("Price");
    private TableColumn<Room, String> descriptionColumn = new TableColumn<>("Description");

    private RoomDAO roomDAO = new RoomDAOImpl();

    public RoomUI() {
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
        pane.add(bookingButton, 3, 0);
        pane.add(clientsButton, 3, 1);

        createButton.setOnAction(e -> createButton_Click());
        updateButton.setOnAction(e -> updateButton_Click());
        deleteButton.setOnAction(e -> deleteButton_Click());

        bookingButton.setOnAction(e -> {
            Stage stage = Main.getStage();
            stage.close();
            stage.setScene(new Scene(new BookingUI()));
            stage.setWidth(1500);
            stage.setTitle("Hotel DataBase --- Booking");
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
        if (numberField.getText().equals("") || seatsField.getText().equals("") || priceField.getText().equals("")) {
            MessageBox.show("Choose a row to delete!", "Delete info");
            return;
        }
        if (ConfirmationBox.show("Are you sure you want to delete this row?\n" +
                roomTableView.getSelectionModel().getSelectedItems(), "Delete confirmation", "Yes", "No")) {
            Room room = getFieldsData();
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
            Room room = getFieldsData();
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

            Room room = getFieldsData();
            if (roomDAO.insertRoom(room)) {
                MessageBox.show("New room created successfully!", "Create info");
            }

            createButton.setText("New...");
            refreshTable();
        } else if (createButton.getText().equals("New...")) {
            Room room = new Room();
            room.setNumber(0);
            room.setSeats(0);
            room.setPrice(0);
            room.setDescription("");

            setFieldsData(room);
            createButton.setText("Save");
        }
    }

    private Pane initFields() {
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setHgap(5);
        pane.setVgap(10);

        Label numberFieldLabel = new Label("Room number");
        numberFieldLabel.setTextFill(Color.BLUE);
        numberFieldLabel.setFont(Font.font("Times New Roman", 20));
        Label seatsFieldLabel = new Label("Seats");
        seatsFieldLabel.setTextFill(Color.BLUE);
        seatsFieldLabel.setFont(Font.font("Times New Roman", 20));
        Label priceFieldLabel = new Label("Price");
        priceFieldLabel.setTextFill(Color.BLUE);
        priceFieldLabel.setFont(Font.font("Times New Roman", 20));
        Label descriptionFieldLabel = new Label("Description");
        descriptionFieldLabel.setTextFill(Color.BLUE);
        descriptionFieldLabel.setFont(Font.font("Times New Roman", 20));

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

        seatsColumn.setCellValueFactory(new PropertyValueFactory<>("Seats"));
        seatsColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        seatsColumn.setOnEditCommit(e -> seatsColumn_OnEditCommit(e));

        priceColumn.setCellValueFactory(new PropertyValueFactory<>("Price"));
        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        priceColumn.setOnEditCommit(e -> priceColumn_OnEditCommit(e));

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
        ObservableList<Room> masterData = FXCollections.observableArrayList();
        masterData.addAll(roomDAO.getAllRooms());

        return masterData;
    }

    private void setTableData() {
        roomTableView.setItems(loadData());
    }

    private void refreshTable() {
        TableView.TableViewFocusModel<Room> roomModelFocused = roomTableView.getFocusModel();
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

    private Room getFieldsData() {
        Room room = new Room();
        room.setId(Long.parseLong(idField.getText()));
        room.setNumber(Integer.parseInt(numberField.getText()));
        room.setSeats(Integer.parseInt(seatsField.getText()));
        room.setPrice(Integer.parseInt(priceField.getText()));
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