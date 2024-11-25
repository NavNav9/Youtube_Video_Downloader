package com.apexdev.projectscarlet;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class videoDashboard extends Thread implements Initializable {

    @FXML
    private GridPane videoList;

    @FXML
    private JFXHamburger hamburger;

    @FXML
    private JFXDrawer drawer;


    //Lowkey I forgot 90% of java so this gonna be a struggle


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        //SideBar BABY

        try {
            VBox vbox = FXMLLoader.load(getClass().getResource("sidePane.fxml"));
            drawer.setSidePane(vbox);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Animations
        HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(hamburger);
        transition.setRate(transition.getCurrentRate() - 1);
        transition.play();

        hamburger.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (hamburger.getOpacity() == 0) {
                    hamburger.setOpacity(1);
                }


            }
        });

        //hiding and showing the sidebar
        hamburger.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                if (drawer.isHiding()) {
                    hamburger.setOpacity(0);
                }

                if (drawer.isHidden()) {
                    hamburger.setOpacity(0);
                }


            }
        });


        //What to do if sidebar button is pressed
        hamburger.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (drawer.isHidden()) {
                    drawer.open();
                } else {
                    drawer.close();
                }

            }
        });

        //Grabbing thumbnails

        Thread thumbnails = new thumbnailGrabberForDashboard(videoList);

        thumbnails.start();

        //Now Grab the file name

        thumbnailGrabberForDashboard FileNameGrabber = new thumbnailGrabberForDashboard();


        fileToPlay = FileNameGrabber.getFileName();



    }

    private String fileToPlay;

    public String getFileToPlay(){
        return fileToPlay;
    }


    private Scene previousScene;

    public void setPreviousScene(Scene previousScene){
        this.previousScene = previousScene;
    }

    public Scene getPreviousScene(){
        return previousScene;
    }


}
