package com.hotel;

import com.hotel.ui.ConfirmationBox;
import com.hotel.ui.SignInUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage stage;
    private static Image icon = new Image(Main.class.getResourceAsStream("/com/hotel/resources/icon.png"));

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        stage.setScene(new Scene(new SignInUI()));
        stage.setTitle("Sign in to mySql");
        stage.getIcons().add(icon);
        stage.setOnCloseRequest(e -> {
            e.consume();
            if (ConfirmationBox.show("Are you sure you want to quit?", "Exit confirmation", "Yes", "No"))
                stage.close();
        });

        stage.show();
    }

    public static Stage getStage() {
        return stage;
    }

    public static Image getIcon() {
        return icon;
    }
}