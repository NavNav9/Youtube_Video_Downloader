package com.apexdev.projectscarlet;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class sideBarController {

private String selectedUsername;

//Not gonna explain everything. Any button pressed from sidebar is gonna be redirecting user to the right page and use it's class (a.k.a controller)


    @FXML
    private VBox vbox;

    @FXML
    void home(MouseEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("homeScreen.fxml"));
        Scene newScene = new Scene(fxmlLoader.load(), 600, 400);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(newScene);

        stage.show();

        stage.setResizable(false);
        stage.setFullScreen(false);


    }

    @FXML
    void search(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("youtubeSearch.fxml"));
        Scene newScene = new Scene(fxmlLoader.load(), 720, 480);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(newScene);

        stage.show();

        stage.setResizable(false);
        stage.setFullScreen(false);
    }

    private Parent root;



    @FXML
    void settings(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("settingsScreen.fxml"));
        Scene newScene = new Scene(fxmlLoader.load(), 600, 400);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(newScene);

        stage.show();

        stage.setResizable(false);
        stage.setFullScreen(false);
    }

    @FXML
    void videoPlayer(MouseEvent event) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("newVideoPlayer.fxml"));
            Scene newScene = new Scene(fxmlLoader.load(), 720, 480);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(newScene);

            stage.show();

            stage.setResizable(true);
            stage.setFullScreen(false);


            newScene.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getClickCount() == 2){
                        stage.setFullScreen(true);
                    }
                }
            });

            newScene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

            final int control = 0;

            @Override
            public void handle(KeyEvent e) {


                if (e.getCode() == KeyCode.RIGHT) {
                    videoPlayerController.mediaPlayerControls(1);
                }
                else if (e.getCode() == KeyCode.LEFT) {
                    videoPlayerController.mediaPlayerControls(2);
                }
                else if (e.getCode() == KeyCode.SPACE){
                    videoPlayerController.mediaPlayerControls(5);
                }

            }
        });


    }
}


