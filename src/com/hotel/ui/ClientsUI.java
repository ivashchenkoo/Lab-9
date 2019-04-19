package com.hotel.ui;

import com.hotel.DateUtil;
import com.hotel.Main;
import com.hotel.dao.ClientsDAO;
import com.hotel.dao.ClientsDAOImpl;
import com.hotel.domain.Clients;
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

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

public class ClientsUI extends BorderPane {
    private TextField idField = new TextField();
    private TextField firstNameField = new TextField();
    private TextField middleNameField = new TextField();
    private TextField lastNameField = new TextField();

    private TextField birthDtField = new TextField();
    private DatePicker birthDatePicker = new DatePicker();

    private TextField serialOfPassportField = new TextField();
    private TextField numOfPassportField = new TextField();
    private TextField phoneField = new TextField();

    private Button createButton = new Button("New...");
    private Button updateButton = new Button("Update...");
    private Button deleteButton = new Button("Delete...");

    private Button roomButton = new Button("Rooms...");
    private Button bookingButton = new Button("Booking...");

    private TableView<Clients> clientsTableView = new TableView<>();
    private TableColumn<Clients, Long> idColumn = new TableColumn<>("ID");
    private TableColumn<Clients, String> firstNameColumn = new TableColumn<>("First name");
    private TableColumn<Clients, String> middleNameColumn = new TableColumn<>("Middle name");
    private TableColumn<Clients, String> lastNameColumn = new TableColumn<>("Last name");
    private TableColumn<Clients, Date> birthDtColumn = new TableColumn<>("Birth date");
    private TableColumn<Clients, String> serialOfPassportColumn = new TableColumn<>("Passport serial number");
    private TableColumn<Clients, Integer> numOfPassportColumn = new TableColumn<>("Passport number");
    private TableColumn<Clients, String> phoneColumn = new TableColumn<>("Phone number");

    private ClientsDAO clientsDAO = new ClientsDAOImpl();

    public ClientsUI() {
        setPadding(new Insets(10, 10, 10, 10));
        setTop(new Label());
        setCenter(initFields());
        setRight(initButtons());
        setBottom(initTable());
        setTableData();
        initListeners();
        initDatePickerEvents(birthDatePicker, birthDtField);
    }

    private void initDatePickerEvents(DatePicker datePicker, TextField textField) {
        datePicker.setOnAction(e ->
        {
            LocalDate date = datePicker.getValue();
            if (date != null) {
                textField.setText(date.toString());
            } else {
                textField.setText(null);
            }
        });
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
        pane.add(roomButton, 3, 1);

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
        roomButton.setOnAction(e -> {
            Stage stage = Main.getStage();
            stage.close();
            stage.setScene(new Scene(new RoomUI()));
            stage.setWidth(700);
            stage.setTitle("Hotel DataBase --- Rooms");
            stage.show();
        });

        return pane;
    }

    private void deleteButton_Click() {
        if (firstNameField.getText().equals("") || lastNameField.getText().equals("") || numOfPassportField.getText().equals("") || birthDtField.getText().isEmpty()) {
            MessageBox.show("Choose a row to delete!", "Delete info");
            return;
        }
        if (ConfirmationBox.show("Are you sure you want to delete this row?\n" +
                clientsTableView.getSelectionModel().getSelectedItems(), "Delete confirmation", "Yes", "No")) {
            Clients clients = null;
            try {
                clients = getFieldsData();
            } catch (IOException e) {
                return;
            }
            clients = clientsDAO.getClientByID(clients.getId());
            if (clientsDAO.deleteClient(clients.getId())) {
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
                clientsTableView.getSelectionModel().getSelectedItems(), "Update confirmation", "Yes", "No")) {
            Clients clients = null;
            try {
                clients = getFieldsData();
            } catch (IOException e) {
                return;
            }
            if (clientsDAO.updateClient(clients)) {
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

            Clients clients = null;
            try {
                clients = getFieldsData();
            } catch (IOException e) {
                return;
            }
            if (clientsDAO.insertClient(clients)) {
                MessageBox.show("New Client created successfully!", "Create info");
            }

            createButton.setText("New...");
            refreshTable();
        } else if (createButton.getText().equals("New...")) {
            Clients clients = new Clients();
            clients.setFirstName("");
            clients.setMiddleName("");
            clients.setLastName("");
            clients.setBirthDate(null);
            clients.setSerialOfPassport("");
            clients.setNumOfPassport(0);
            clients.setPhone("");

            setFieldsData(clients);
            createButton.setText("Save");
        }
    }

    private Pane initFields() {
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setHgap(5);
        pane.setVgap(10);

        Label firstNameLabel = new Label("First name");
        firstNameLabel.setTextFill(Color.BLUE);
        firstNameLabel.setFont(Font.font("Times New Roman", 20));
        Label middleNameLabel = new Label("Middle name");
        middleNameLabel.setTextFill(Color.BLUE);
        middleNameLabel.setFont(Font.font("Times New Roman", 20));
        Label lastNameLabel = new Label("Last name");
        lastNameLabel.setTextFill(Color.BLUE);
        lastNameLabel.setFont(Font.font("Times New Roman", 20));
        Label birthDtLabel = new Label("Birth date");
        birthDtLabel.setTextFill(Color.BLUE);
        birthDtLabel.setFont(Font.font("Times New Roman", 20));
        Label serialOfPassportLabel = new Label("Passport serial number");
        serialOfPassportLabel.setTextFill(Color.BLUE);
        serialOfPassportLabel.setFont(Font.font("Times New Roman", 20));
        Label numOfPassportLabel = new Label("Passport number");
        numOfPassportLabel.setTextFill(Color.BLUE);
        numOfPassportLabel.setFont(Font.font("Times New Roman", 20));
        Label phoneLabel = new Label("Phone number");
        phoneLabel.setTextFill(Color.BLUE);
        phoneLabel.setFont(Font.font("Times New Roman", 20));

        pane.add(firstNameLabel, 0, 0);
        pane.add(firstNameField, 1, 0);
        pane.add(middleNameLabel, 0, 1);
        pane.add(middleNameField, 1, 1);
        pane.add(lastNameLabel, 0, 2);
        pane.add(lastNameField, 1, 2);
        pane.add(birthDtLabel, 0, 3);
        pane.add(birthDatePicker, 1, 3);
        pane.add(serialOfPassportLabel, 0, 4);
        pane.add(serialOfPassportField, 1, 4);
        pane.add(numOfPassportLabel, 0, 5);
        pane.add(numOfPassportField, 1, 5);
        pane.add(phoneLabel, 0, 6);
        pane.add(phoneField, 1, 6);

        return pane;
    }

    private Pane initTable() {
        VBox pane = new VBox();
        pane.setPadding(new Insets(10, 10, 10, 10));

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        firstNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        firstNameColumn.setOnEditCommit(e -> firstNameColumn_OnEditCommit(e));

        middleNameColumn.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        middleNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        middleNameColumn.setOnEditCommit(e -> middleNameColumn_OnEditCommit(e));

        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        lastNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameColumn.setOnEditCommit(e -> lastNameColumn_OnEditCommit(e));

        birthDtColumn.setCellValueFactory(new PropertyValueFactory<>("birthDate"));

        serialOfPassportColumn.setCellValueFactory(new PropertyValueFactory<>("serialOfPassport"));
        serialOfPassportColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        serialOfPassportColumn.setOnEditCommit(e -> serialOfPassportColumn_OnEditCommit(e));

        numOfPassportColumn.setCellValueFactory(new PropertyValueFactory<>("numOfPassport"));
        numOfPassportColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        numOfPassportColumn.setOnEditCommit(e -> numOfPassportColumn_OnEditCommit(e));

        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        phoneColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneColumn.setOnEditCommit(e -> phoneColumn_OnEditCommit(e));

        clientsTableView.setEditable(true);
        clientsTableView.getColumns().addAll(idColumn, firstNameColumn, middleNameColumn, lastNameColumn, birthDtColumn, serialOfPassportColumn, numOfPassportColumn, phoneColumn);
        pane.getChildren().add(clientsTableView);

        return pane;
    }

    private void phoneColumn_OnEditCommit(TableColumn.CellEditEvent<Clients, String> e) {
        Clients client = e.getRowValue();
        client.setPhone(e.getNewValue());
        clientsDAO.updateClient(client);
    }

    private void numOfPassportColumn_OnEditCommit(TableColumn.CellEditEvent<Clients, Integer> e) {
        Clients client = e.getRowValue();
        client.setNumOfPassport(e.getNewValue());
        clientsDAO.updateClient(client);
    }

    private void serialOfPassportColumn_OnEditCommit(TableColumn.CellEditEvent<Clients, String> e) {
        Clients client = e.getRowValue();
        client.setSerialOfPassport(e.getNewValue());
        clientsDAO.updateClient(client);
    }

    private void lastNameColumn_OnEditCommit(TableColumn.CellEditEvent<Clients, String> e) {
        Clients client = e.getRowValue();
        client.setLastName(e.getNewValue());
        clientsDAO.updateClient(client);
    }

    private void middleNameColumn_OnEditCommit(TableColumn.CellEditEvent<Clients, String> e) {
        Clients client = e.getRowValue();
        client.setMiddleName(e.getNewValue());
        clientsDAO.updateClient(client);
    }

    private void firstNameColumn_OnEditCommit(TableColumn.CellEditEvent<Clients, String> e) {
        Clients client = e.getRowValue();
        client.setFirstName(e.getNewValue());
        clientsDAO.updateClient(client);
    }

    private ObservableList<Clients> loadData() {
        ObservableList<Clients> masterData = FXCollections.observableArrayList();
        masterData.addAll(clientsDAO.getAllClients());

        return masterData;
    }

    private void setTableData() {
        clientsTableView.setItems(loadData());
    }

    private void refreshTable() {
        TableView.TableViewFocusModel<Clients> roomModelFocused = clientsTableView.getFocusModel();
        setTableData();
        clientsTableView.setFocusModel(roomModelFocused);
    }

    private void initListeners() {
        clientsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Clients clients = newSelection;
                setFieldsData(clients);
            }
        });
    }

    private void setFieldsData(Clients clients) {
        idField.setText(String.valueOf(clients.getId()));
        firstNameField.setText(clients.getFirstName());
        middleNameField.setText(clients.getMiddleName());
        lastNameField.setText(clients.getLastName());

        if (clients.getBirthDate() != null) {
            birthDatePicker.setValue(clients.getBirthDate().toLocalDate());
        } else {
            birthDatePicker.setValue(null);
        }

        serialOfPassportField.setText(clients.getSerialOfPassport());
        numOfPassportField.setText(String.valueOf(clients.getNumOfPassport()));
        phoneField.setText(clients.getPhone());
    }

    private Clients getFieldsData() throws IOException {
        Clients clients = new Clients();
        clients.setId(Long.parseLong(idField.getText()));
        clients.setFirstName(firstNameField.getText());
        clients.setMiddleName(middleNameField.getText());
        clients.setLastName(lastNameField.getText());
        clients.setBirthDate(DateUtil.convertStringIntoSqlDate(birthDtField.getText()));
        clients.setSerialOfPassport(serialOfPassportField.getText());
        try {
            clients.setNumOfPassport(Integer.parseInt(numOfPassportField.getText()));
        } catch (NumberFormatException ex) {
            MessageBox.show("Passport number must be an Integer", "NumberFormatException");
            numOfPassportField.clear();
            throw new IOException();
        }
        clients.setPhone(phoneField.getText());

        return clients;
    }

    private boolean isEmptyFieldData() {
        if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() || birthDtField.getText().isEmpty() || numOfPassportField.getText().equals("0")) {
            return true;
        } else {
            return false;
        }
    }
}