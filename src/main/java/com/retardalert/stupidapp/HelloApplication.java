package com.retardalert.stupidapp;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;


import java.io.IOException;

public class HelloApplication extends Application {


    public Scene scene;

    @FXML
    private MediaView mediaView;

    @Override
    public void start(Stage stage) throws IOException {


        webViewController webViewController = new webViewController();

    //Testing changed firstquestion.fxml to youtubeSearch.fxml
        FXMLLoader fxmlLoader = new FXMLLoader(QuestionController.class.getResource("youtubeSearch.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 863, 590);

        stage.setTitle("Youtube Premium!");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
        stage.setAlwaysOnTop(false);



        //Here's FullScreen

        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2){
                    stage.setFullScreen(true);
                }
            }
        });



        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

            final int control = 0;

            @Override
            public void handle(KeyEvent e) {


                if (e.getCode() == KeyCode.RIGHT) {
                    QuestionController.mediaPlayerControls(1);
                }
                else if (e.getCode() == KeyCode.LEFT) {
                    QuestionController.mediaPlayerControls(2);
                }
                else if (e.getCode() == KeyCode.SPACE){
                    QuestionController.mediaPlayerControls(5);
                }

            }
        });






    }






    public static void main(String[] args) {
        launch();
    }
}