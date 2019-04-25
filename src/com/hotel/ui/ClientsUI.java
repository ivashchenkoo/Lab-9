package com.hotel.ui;

import com.hotel.ConnectionFactory;
import com.hotel.DateUtil;
import com.hotel.Main;
import com.hotel.dao.ClientsDAO;
import com.hotel.dao.ClientsDAOImpl;
import com.hotel.domain.Clients;
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
import java.sql.Date;
import java.time.LocalDate;

public class ClientsUI extends BorderPane {
    private MenuBar menuBar;
    private Menu mainMenu = new Menu("_Main");
    private Menu dataBasesMenu = new Menu("_Data Bases");
    private Menu tableMenu = new Menu("_Table");
    private Menu requestMenu = new Menu("_Request...");

    private CustomMenuItem userCustomMenuItem;
    private MenuItem changeUserMenuItem = new MenuItem("_Change DB User");
    private MenuItem exitMenuItem = new MenuItem("_Exit");
    private MenuItem bookingMenuItem = new MenuItem("_Booking");
    private MenuItem roomsMenuItem = new MenuItem("_Rooms");
    private MenuItem createMenuItem = new MenuItem("_New...");
    private MenuItem updateMenuItem = new MenuItem("_Update...");
    private MenuItem deleteMenuItem = new MenuItem("_Delete...");
    private MenuItem clearMenuItem = new MenuItem("_Clear");
    private MenuItem getAllClientsMenuItem = new MenuItem("Get all clients");
    private MenuItem getFirstClientMenuItem = new MenuItem("Get first client");
    private MenuItem getClientByIdMenuItem = new MenuItem("Get client by ID");
    private MenuItem getClientByNameMenuItem = new MenuItem("Get client by name");

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
    private Button requestButton = new Button("Request...");
    private Button clearButton = new Button("Clear");

    private Button getAllClientsButton = new Button("Get all clients");
    private Button getFirstClientButton = new Button("Get first client");
    private Button getClientByIdButton = new Button("Get client by ID");
    private Button getClientByNameButton = new Button("Get client by name");

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
    private ObservableList<Clients> masterData = FXCollections.observableArrayList();

    private ClientsDAO clientsDAO = new ClientsDAOImpl();

    public ClientsUI() {
        setTop(initMenuBar());
        setCenter(initFields());
        setRight(initButtons());
        setBottom(initTable());
        loadData();
        setTableData();
        initListeners();
        initDatePickerEvents(birthDatePicker, birthDtField);
        getStylesheets().add("com/hotel/resources/Simple.css");
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

    private MenuBar initMenuBar() {
        menuBar = new MenuBar(mainMenu, dataBasesMenu, tableMenu);
        dataBasesMenu.getItems().addAll(bookingMenuItem, roomsMenuItem);
        tableMenu.getItems().addAll(createMenuItem, updateMenuItem, deleteMenuItem, new SeparatorMenuItem(), clearMenuItem, requestMenu);
        requestMenu.getItems().addAll(getAllClientsMenuItem, getFirstClientMenuItem, getClientByIdMenuItem, getClientByNameMenuItem);

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
        roomsMenuItem.setOnAction(e -> roomsButton_Click());

        createMenuItem.setOnAction(e -> createButton_Click());
        updateMenuItem.setOnAction(e -> updateButton_Click());
        deleteMenuItem.setOnAction(e -> deleteButton_Click());
        clearMenuItem.setOnAction(e -> clearButton_Click());

        getAllClientsMenuItem.setOnAction(e -> requestButtons_Clicks(e));
        getFirstClientMenuItem.setOnAction(e -> requestButtons_Clicks(e));
        getClientByIdMenuItem.setOnAction(e -> requestButtons_Clicks(e));
        getClientByNameMenuItem.setOnAction(e -> requestButtons_Clicks(e));

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
        pane.add(roomButton, 3, 1);
        pane.add(clearButton, 3, 3);

        createButton.setOnAction(e -> createButton_Click());
        updateButton.setOnAction(e -> updateButton_Click());
        deleteButton.setOnAction(e -> deleteButton_Click());
        requestButton.setOnAction(e -> requestButton_Click());
        clearButton.setOnAction(e -> clearButton_Click());

        bookingButton.setOnAction(e -> bookingButton_Click());
        roomButton.setOnAction(e -> roomsButton_Click());

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
        pane.getChildren().addAll(getAllClientsButton, getFirstClientButton, getClientByIdButton, getClientByNameButton);

        getAllClientsButton.setOnAction(e -> requestButtons_Clicks(e));
        getFirstClientButton.setOnAction(e -> requestButtons_Clicks(e));
        getClientByIdButton.setOnAction(e -> requestButtons_Clicks(e));
        getClientByNameButton.setOnAction(e -> requestButtons_Clicks(e));

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
        if (e.getSource().equals(getAllClientsButton) || e.getSource().equals(getAllClientsMenuItem)) {
            loadData();
            setTableData();
        } else if (e.getSource().equals(getFirstClientButton) || e.getSource().equals(getFirstClientMenuItem)) {
            masterData.clear();
            masterData.addAll(clientsDAO.getFirstClient());
            setTableData();
        } else if (e.getSource().equals(getClientByIdButton) || e.getSource().equals(getClientByIdMenuItem)) {
            long id;
            try {
                String idStr = InputBox.show("Enter client ID:", "Input data");
                if (idStr.equals("")) return;
                id = Long.parseLong(idStr);
                masterData.clear();
                masterData.addAll(clientsDAO.getClientByID(id));
                setTableData();
            } catch (NumberFormatException ex) {
                MessageBox.show("ID number must be a Long value", "NumberFormatException");
                requestButtons_Clicks(e);
            }
        } else if (e.getSource().equals(getClientByNameButton) || e.getSource().equals(getClientByNameMenuItem)) {
            String firstName = InputBox.show("Enter client`s first name:", "Input data");
            String lastName = InputBox.show("Enter client`s last name:", "Input data");
            masterData.clear();
            masterData.addAll(clientsDAO.getClientByName(firstName, lastName));
            setTableData();
        }
    }

    private void roomsButton_Click() {
        Stage stage = Main.getStage();
        stage.setScene(new Scene(new RoomUI()));
        stage.setTitle("Hotel DataBase --- Rooms");
    }

    private void bookingButton_Click() {
        Stage stage = Main.getStage();
        stage.setScene(new Scene(new BookingUI()));
        stage.setTitle("Hotel DataBase --- Booking");
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
            createMenuItem.setText("_New...");
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
            createMenuItem.setText("_Save");
        }
    }

    private Pane initFields() {
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.TOP_CENTER);
        pane.setPadding(new Insets(25, 10, 10, 10));
        pane.setHgap(5);
        pane.setVgap(10);

        Label firstNameLabel = new Label("First name");
        firstNameLabel.setTextFill(Color.BLUE);
        Label middleNameLabel = new Label("Middle name");
        middleNameLabel.setTextFill(Color.BLUE);
        Label lastNameLabel = new Label("Last name");
        lastNameLabel.setTextFill(Color.BLUE);
        Label birthDtLabel = new Label("Birth date");
        birthDtLabel.setTextFill(Color.BLUE);
        Label serialOfPassportLabel = new Label("Passport serial number");
        serialOfPassportLabel.setTextFill(Color.BLUE);
        Label numOfPassportLabel = new Label("Passport number");
        numOfPassportLabel.setTextFill(Color.BLUE);
        Label phoneLabel = new Label("Phone number");
        phoneLabel.setTextFill(Color.BLUE);

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
        masterData.clear();
        masterData.addAll(clientsDAO.getAllClients());

        return masterData;
    }

    private void setTableData() {
        clientsTableView.setItems(masterData);
    }

    private void refreshTable() {
        TableView.TableViewFocusModel<Clients> roomModelFocused = clientsTableView.getFocusModel();
        loadData();
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