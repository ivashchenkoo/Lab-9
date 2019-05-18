package com.hotel.ui;

import com.hotel.Main;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;

public class MessageBox {
    public static void show(String message, String title) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setMinWidth(350);
        stage.setMinHeight(150);
        Label lbl = new Label();
        lbl.setText(message);
        Button btnOK = new Button();
        btnOK.setText("OK");
        btnOK.setOnAction(e -> stage.close());
        VBox pane = new VBox(20);
        pane.getChildren().addAll(lbl, btnOK);
        pane.setAlignment(Pos.CENTER);
        Scene scene = new Scene(pane);
        scene.getStylesheets().add("com/hotel/resources/Boxes.css");
        stage.setScene(scene);
        stage.getIcons().add(Main.getIcon());

        File f = new File("src/com/hotel/resources/erro.mp3");
        Media media = new Media(f.toURI().toString());
        MediaPlayer mplayer = new MediaPlayer(media);
        mplayer.setAutoPlay(true);

        stage.showAndWait();
    }
}