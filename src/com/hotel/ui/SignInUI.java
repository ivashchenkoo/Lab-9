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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SignInUI extends BorderPane {
    private Stage stage = Main.getStage();

    public SignInUI() {
        //Adding GridPane
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        //Implementing Nodes for GridPane
        Label lblUserName = new Label("Username:");
        lblUserName.setTextFill(Color.BLUE);
        lblUserName.setFont(Font.font("Times New Roman", 20));
        final TextField txtUserName = new TextField("root");
        Label lblPassword = new Label("Password:");
        lblPassword.setTextFill(Color.BLUE);
        lblPassword.setFont(Font.font("Times New Roman", 20));
        final PasswordField pf = new PasswordField();
        Button btnSignIn = new Button("Sign In");
        final Label lblMessage = new Label();
        //Adding Nodes to GridPane layout
        gridPane.add(lblUserName, 0, 0);
        gridPane.add(txtUserName, 1, 0);
        gridPane.add(lblPassword, 0, 1);
        gridPane.add(pf, 1, 1);
        gridPane.add(btnSignIn, 2, 4);
        gridPane.add(lblMessage, 0, 4);

        btnSignIn.setOnAction(e -> {
            try (Connection connection = DriverManager.getConnection(ConnectionFactory.DB_URL, txtUserName.getText(), pf.getText())) {
                System.out.println("Connection successfully");
                stage.close();
                stage.setMinWidth(700);
                stage.setMinHeight(700);
                stage.setTitle("Hotel DataBase --- Booking");
                stage.setWidth(1500);
                stage.setScene(new Scene(new BookingUI(), 900, 500));
                stage.show();
            } catch (SQLException ex) {
                MessageBox.show("Incorrect login or password!", "SQLException");
                txtUserName.setText("");
                pf.setText("");
            }
        });

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        Label titleLb = new Label("Hotel DataBase");
        titleLb.setTextFill(Color.GREEN);
        titleLb.setFont(Font.font("Broadway", FontWeight.findByWeight(60), 20));
        vbox.getChildren().addAll(titleLb, gridPane);

        setCenter(vbox);
    }
}
