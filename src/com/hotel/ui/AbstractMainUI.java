package com.hotel.ui;

import com.hotel.ConnectionFactory;
import com.hotel.Main;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.text.DecimalFormat;

public abstract class AbstractMainUI extends BorderPane {
    public static DecimalFormat df = new DecimalFormat("0.0");

    private MenuBar menuBar;
    private Menu mainMenu = new Menu("_Main");
    private Menu dataBasesMenu = new Menu("_Data Base");
    private Menu tableMenu = new Menu("_Table");
    private Menu requestMenu = new Menu("_Request...");

    private CustomMenuItem volumeCustomMenuItem;
    private MenuItem audioPlayPauseMenuItem = new MenuItem("_Play");
    private  MediaPlayer mplayer;
    private String PATH;
    private Slider volumeSlider = new Slider(0,100,100);

    private CustomMenuItem userCustomMenuItem;
    private MenuItem changeUserMenuItem = new MenuItem("_Change DB User");
    private MenuItem exitMenuItem = new MenuItem("_Exit");
    protected MenuItem createMenuItem = new MenuItem("_New...");   // <=== protected
    private MenuItem updateMenuItem = new MenuItem("_Update...");
    private MenuItem deleteMenuItem = new MenuItem("_Delete...");
    private MenuItem clearMenuItem = new MenuItem("_Clear");
    private MenuItem bookingMenuItem;
    private MenuItem roomMenuItem;
    private MenuItem clientsMenuItem;

    protected Button createButton = new Button("New...");   // <=== protected
    private Button updateButton = new Button("Update...");
    private Button deleteButton = new Button("Delete...");
    private Button requestButton = new Button("Request...");
    private Button clearButton = new Button("Clear");
    private Button bookingButton;
    private Button roomButton;
    private Button clientsButton;

    private Menu initAudioMenu(){
        Menu audioMenu = new Menu("_Audio");

        File dir = new File("src/com/hotel/resources/sounds/");
        File[] files = dir.listFiles();
        PATH = "src/com/hotel/resources/sounds/" + (int)(Math.random()*files.length + 1) + ".mp3";
        File f = new File(PATH);

        MenuItem chooseAudioPATHMenuItem = new MenuItem("_Choose Audio");
        chooseAudioPATHMenuItem.setOnAction(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new java.io.File("."));
            chooser.setDialogTitle("Choose a file...");

            chooser.setAcceptAllFileFilterUsed(false);

            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                mplayer.stop();
                audioPlayPauseMenuItem.setText("_Play");

                File f1 = chooser.getSelectedFile();
                System.out.println(f1.toURI().toString().substring(6));
                Media media = new Media(f1.toURI().toString());
                mplayer = new MediaPlayer(media);
                mplayer.setAutoPlay(false);
            }
        });
        System.out.println(f.toURI().toString().substring(6));
        Media media = new Media(f.toURI().toString());
        mplayer = new MediaPlayer(media);
        mplayer.setAutoPlay(false);

        Text sliderText = new Text("Volume: " + df.format(volumeSlider.getValue()).replace(',','.'));
        CustomMenuItem sliderTextCustomMenuItem = new CustomMenuItem(sliderText);
        volumeSlider.valueProperty().addListener((observable, oldvalue, newvalue) -> {
            mplayer.setVolume(newvalue.doubleValue()/100);
            sliderText.setText("Volume: " + df.format(newvalue.doubleValue()).replace(',','.'));
        });
        volumeCustomMenuItem = new CustomMenuItem(volumeSlider);
        volumeCustomMenuItem.setOnAction(e -> mplayer.setVolume(volumeSlider.getValue()/100));
        volumeCustomMenuItem.setHideOnClick(false);

        audioPlayPauseMenuItem.setOnAction(e -> {
            if (audioPlayPauseMenuItem.getText().equals("_Play")) {
                mplayer.play();
                mplayer.setVolume(volumeSlider.getValue()/100);
                audioPlayPauseMenuItem.setText("_Pause");
            } else if (audioPlayPauseMenuItem.getText().equals("_Pause")){
                mplayer.pause();
                audioPlayPauseMenuItem.setText("_Play");
            }
        });
        audioMenu.getItems().addAll(audioPlayPauseMenuItem, sliderTextCustomMenuItem, volumeCustomMenuItem, new SeparatorMenuItem(), chooseAudioPATHMenuItem);

        return audioMenu;
    }

    protected MenuBar initMenuBar(MenuItem... requestMenuItems) {
        Label userLabel = new Label("User: " + ConnectionFactory.DB_USER);
        userLabel.setId("userLb");
        userCustomMenuItem = new CustomMenuItem(userLabel);
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
        mainMenu.getItems().addAll(userCustomMenuItem, changeUserMenuItem, new SeparatorMenuItem(), exitMenuItem);

        if (!this.getClass().equals(BookingUI.class)) {
            bookingMenuItem = new MenuItem("_Booking...");
            bookingMenuItem.setOnAction(e -> dataBasesButton_Click(e));
            dataBasesMenu.getItems().add(bookingMenuItem);
        }
        if (!this.getClass().equals(RoomUI.class)) {
            roomMenuItem = new MenuItem("_Rooms...");
            roomMenuItem.setOnAction(e -> dataBasesButton_Click(e));
            dataBasesMenu.getItems().add(roomMenuItem);
        }
        if (!this.getClass().equals(ClientsUI.class)) {
            clientsMenuItem = new MenuItem("_Clients...");
            clientsMenuItem.setOnAction(e -> dataBasesButton_Click(e));
            dataBasesMenu.getItems().add(clientsMenuItem);
        }

        createMenuItem.setOnAction(e -> createButton_Click());
        updateMenuItem.setOnAction(e -> updateButton_Click());
        deleteMenuItem.setOnAction(e -> deleteButton_Click());
        clearMenuItem.setOnAction(e -> clearButton_Click());
        tableMenu.getItems().addAll(createMenuItem, updateMenuItem, deleteMenuItem, new SeparatorMenuItem(), clearMenuItem, requestMenu);

        requestMenu.getItems().addAll(requestMenuItems);
        for (MenuItem menuItem : requestMenuItems)
            menuItem.setOnAction(e -> requestButtons_Clicks(e));

        menuBar = new MenuBar(mainMenu, dataBasesMenu, tableMenu, initAudioMenu());
        return menuBar;
    }

    protected Pane initButtons(Button... requestButtons) {
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(25, 20, 20, 20));
        pane.setVgap(10);
        pane.setHgap(20);
        pane.setAlignment(Pos.TOP_CENTER);

        pane.add(createButton, 0, 0);
        pane.add(updateButton, 0, 1);
        pane.add(deleteButton, 0, 2);
        pane.add(requestButton, 0, 3);

        int i = 0;
        if (!this.getClass().equals(BookingUI.class)) {
            bookingButton = new Button("Booking...");
            pane.add(bookingButton, 3, i++);
            bookingButton.setOnAction(e -> dataBasesButton_Click(e));
        }
        if (!this.getClass().equals(RoomUI.class)) {
            roomButton = new Button("Rooms...");
            pane.add(roomButton, 3, i++);
            roomButton.setOnAction(e -> dataBasesButton_Click(e));
        }
        if (!this.getClass().equals(ClientsUI.class)) {
            clientsButton = new Button("Clients...");
            pane.add(clientsButton, 3, i++);
            clientsButton.setOnAction(e -> dataBasesButton_Click(e));
        }

        pane.add(clearButton, 3, 3);

        createButton.setOnAction(e -> createButton_Click());
        updateButton.setOnAction(e -> updateButton_Click());
        deleteButton.setOnAction(e -> deleteButton_Click());
        requestButton.setOnAction(e -> requestButton_Click(requestButtons));
        clearButton.setOnAction(e -> clearButton_Click());

        return pane;
    }

    private void dataBasesButton_Click(ActionEvent e) {
        if (e.getSource().equals(bookingButton) || e.getSource().equals(bookingMenuItem)) {
            Stage stage = Main.getStage();
            stage.setScene(new Scene(new BookingUI()));
            stage.setTitle("Hotel DataBase --- Booking");
        } else if (e.getSource().equals(roomButton) || e.getSource().equals(roomMenuItem)) {
            Stage stage = Main.getStage();
            stage.setScene(new Scene(new RoomUI()));
            stage.setTitle("Hotel DataBase --- Rooms");
        } else if (e.getSource().equals(clientsButton) || e.getSource().equals(clientsMenuItem)) {
            Stage stage = Main.getStage();
            stage.setScene(new Scene(new ClientsUI()));
            stage.setTitle("Hotel DataBase --- Clients");
        }
    }

    private void requestButton_Click(Button... requestButtons) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Requests");
        stage.getIcons().add(Main.getIcon());
        stage.setWidth(300);

        VBox pane = new VBox();
        pane.setPadding(new Insets(20, 20, 20, 20));
        pane.setSpacing(20);
        pane.setAlignment(Pos.CENTER);

        pane.getChildren().addAll(requestButtons);
        for (Button button : requestButtons)
            button.setOnAction(e -> requestButtons_Clicks(e));

        Scene scene = new Scene(pane);
        scene.getStylesheets().add("com/hotel/resources/Boxes.css");
        stage.setScene(scene);
        stage.show();
    }

    protected void clearButton_Click() {
        loadData();
        setTableData();
    }

    protected abstract void requestButtons_Clicks(ActionEvent e);

    protected abstract void deleteButton_Click();

    protected abstract void updateButton_Click();

    protected abstract void createButton_Click();

    protected abstract Pane initFields();

    protected abstract Pane initTable();

    protected abstract void loadData();

    protected abstract void setTableData();
}
