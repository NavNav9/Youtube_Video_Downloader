package com.apexdev.projectscarlet;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class youtubeSearchController extends Thread implements Initializable {

    @FXML
    private ScrollPane scrollBar;

    @FXML
    private JFXHamburger hamburger;

    @FXML
    private JFXDrawer drawer;


    @FXML
    private ImageView Logo;

    @FXML
    private TextField searchBar;

    @FXML
    private FontAwesomeIcon searchButton;

    @FXML
    private GridPane gridPane;

    @FXML
    void search(MouseEvent event) throws IOException {

        String search = searchBar.getText();

        searchResults(search);
    }

    @FXML
    void enter(KeyEvent key){
        //Search whatever in the search bar
        searchBar.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent key) {
                if (key.getCode().equals(KeyCode.ENTER)){
                    gridPane.getChildren().clear();
                    String search = searchBar.getText();
                    searchResults(search);
                }
            }
        });


    }




    private void searchResults(String search){

        // No need to lock the sync anymore doesn't do much (adding something for it later when this assignment is submitted)
        Object isComplete = new Object();

       // YoutubeObject YoutubeObject = new YoutubeObject();
        Thread preDownload = new YoutubeObject(gridPane,search,isComplete);

        //THREADING BABY

       preDownload.start();



//Whenever the scrollbar is changing just run different threads to speed things up and be more efficient
        scrollBar.vvalueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {



                //If threads running don't go more down to run more threads (breaks program if done)

                if (preDownload.getState().equals(State.RUNNABLE) && scrollBar.getVvalue() >= 0.7){
                    scrollBar.setVvalue(0.7);
                }


                else if (scrollBar.getVvalue() >= 0.9){
                    System.out.println("End");
                   // YoutubeObject.getThumbnailImages(search,gridPane);

                    if (preDownload.getState().equals(State.TERMINATED)) {
                    //Max is 20 searches so if threads are dead, they aren't coming back
                        System.out.println("Can't print anymore");
                    }

                    else if (!preDownload.isAlive()){
                        preDownload.start();
                    }

                }

            }
        });



    }


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

                if (drawer.isHidden()){
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
    }
}
