package com.apexdev.projectscarlet;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;


import java.io.IOException;

public class HelloApplication extends Application {

    //Assignment: CPT
    //Date: 19 Jan 2024
    //By: Navid
    //I love this program with my heart


    public Scene scene;

    @FXML
    private MediaView mediaView;

    @Override
    public void start(Stage stage) throws IOException {

        //Just starts the app


        webViewController webViewController = new webViewController();

   //Shows Main Screen
        FXMLLoader fxmlLoader = new FXMLLoader(videoPlayerController.class.getResource("homeScreen.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        //Lie about what this is by setting title as youtube premium and don't resize
        stage.setTitle("Youtube Premium!");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
        stage.setAlwaysOnTop(false);



    }





//Launch the App
    public static void main(String[] args) {
        launch();
    }
}