package com.hotel.ui;

import com.hotel.Main;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmationBox {
    static Stage stage;
    static boolean btnYesClicked;

    public static boolean show(String message, String title, String textYes, String textNo) {
        btnYesClicked = false;
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setMinWidth(350);
        stage.setMinHeight(150);

        Label lbl = new Label();
        lbl.setText(message);

        Button btnYes = new Button();
        btnYes.setText(textYes);
        btnYes.setOnAction(e -> btnYes_Clicked());
        btnYes.setId("btnYes");

        Button btnNo = new Button();
        btnNo.setText(textNo);
        btnNo.setOnAction(e -> btnNo_Clicked());
        btnNo.setId("btnNo");

        HBox paneBtn = new HBox(20);
        paneBtn.getChildren().addAll(btnYes, btnNo);
        paneBtn.setAlignment(Pos.CENTER);

        VBox pane = new VBox(20);
        pane.setPadding(new Insets(15, 20, 15, 20));
        pane.getChildren().addAll(lbl, paneBtn);
        pane.setAlignment(Pos.CENTER);

        Scene scene = new Scene(pane);
        scene.getStylesheets().add("com/hotel/resources/Boxes.css");
        stage.setScene(scene);
        stage.getIcons().add(Main.getIcon());
        stage.showAndWait();

        return btnYesClicked;
    }

    private static void btnYes_Clicked() {
        stage.close();
        btnYesClicked = true;
    }

    private static void btnNo_Clicked() {
        stage.close();
        btnYesClicked = false;
    }
}