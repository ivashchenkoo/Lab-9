package com.hotel.ui;

import com.hotel.ConnectionFactory;
import com.hotel.Main;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SignInUI extends BorderPane {
    private Stage stage = Main.getStage();

    public SignInUI() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        Label lblUserName = new Label("Username:");
        lblUserName.setTextFill(Color.GREEN);
        final TextField txtUserName = new TextField("root");
        Label lblPassword = new Label("Password:");
        lblPassword.setTextFill(Color.GREEN);
        final PasswordField pf = new PasswordField();
        Button btnSignIn = new Button("Sign In");
        final Label lblMessage = new Label();

        gridPane.add(lblUserName, 0, 0);
        gridPane.add(txtUserName, 1, 0);
        gridPane.add(lblPassword, 0, 1);
        gridPane.add(pf, 1, 1);
        gridPane.add(btnSignIn, 2, 4);
        gridPane.add(lblMessage, 0, 4);
        btnSignIn.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
            if (ev.getCode() == KeyCode.ENTER) {
                btnSignIn.fire();
                ev.consume();
            }
        });
        btnSignIn.setOnAction(e -> {
            try (Connection connection = DriverManager.getConnection(ConnectionFactory.DB_URL, txtUserName.getText(), pf.getText())) {
                System.out.println("Connection successfully");
                stage.close();
                stage.setTitle("Hotel DataBase --- Booking");
                stage.setWidth(1000);
                stage.setHeight(870);
                stage.setScene(new Scene(new BookingUI(), 900, 500));

                /*File f = new File("com\\hotel\\resources\\background.mp3");
                Media media = new Media(f.toURI().toString());
                MediaPlayer mplayer = new MediaPlayer(media);
                mplayer.setAutoPlay(true);
                mplayer.setVolume(0.3);*/

                stage.show();
            } catch (SQLException ex) {
                MessageBox.show("Incorrect login or password!", "SQLException");
                txtUserName.setText("");
                pf.setText("");
            }
        });

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10, 10, 10, 10));
        Text titleTxt = new Text("Hotel DataBase");
        titleTxt.setId("titleTxt");
        vbox.getChildren().addAll(titleTxt, gridPane);
        vbox.setAlignment(Pos.CENTER);

        setCenter(vbox);
        getStylesheets().add("com/hotel/resources/Login.css");
    }
}
