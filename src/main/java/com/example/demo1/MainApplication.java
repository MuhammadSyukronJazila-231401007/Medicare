package com.example.demo1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("FXML/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 555, 316);
        stage.setTitle("Medicare");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
//        System.out.println(MainApplication.class.getResource("FXML/login.fxml"));
        launch();
    }
}