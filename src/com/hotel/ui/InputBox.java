package com.hotel.ui;

import com.hotel.Main;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class InputBox {
    public static String show(String message, String title) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setMinWidth(350);
        stage.setMinHeight(150);

        Label lbl = new Label();
        lbl.setText(message);

        TextField inputField = new TextField();

        Button btnOK = new Button();
        btnOK.setText("OK");
        btnOK.setOnAction(e -> stage.close());

        VBox pane = new VBox(20);
        pane.getChildren().addAll(lbl, inputField, btnOK);
        pane.setPadding(new Insets(10, 20, 10, 20));
        pane.setSpacing(20);
        pane.setAlignment(Pos.CENTER);
        Scene scene = new Scene(pane);
        scene.getStylesheets().add("com/hotel/resources/Boxes.css");
        stage.setScene(scene);
        stage.getIcons().add(Main.getIcon());
        stage.showAndWait();

        String inputData = inputField.getText();
        return inputData;
    }
}
