package com.apexdev.projectscarlet;

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
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class homeScreenController implements Initializable {


    //Not gonna explain each but basically, any button pressed from the home screen will redirect you to that screen and
    //use it's class

    @FXML
    Text name;

    private String selectedUsername;



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


    @FXML
    void settings(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("settingsScreen.fxml"));
        Scene newScene = new Scene(fxmlLoader.load(), 600, 400);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(newScene);

        stage.show();

        stage.setResizable(true);
        stage.setFullScreen(false);

    }

    @FXML
    void users(MouseEvent event) {

    }

    @FXML
    void videoPlayer(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("newVideoPlayer.fxml"));

        Scene newScene = new Scene(fxmlLoader.load(), 720, 480);

        videoDashboard controller = new videoDashboard();
        controller.setPreviousScene(newScene);

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            //Make sure to use the current user upon startup so the welcome screen works
            FileReader fr = new FileReader("CurrentUser.txt");
            Scanner fileScanner = new Scanner(fr);
            if (!fileScanner.hasNext()){
                PrintWriter pw = new PrintWriter("CurrentUser.txt");
                pw.println(System.getProperty("user.name"));
                pw.close();
                fr.close();

                FileReader frNew = new FileReader("CurrentUser.txt");
                Scanner fileScannerNew = new Scanner(frNew);

                selectedUsername = fileScannerNew.nextLine();
                name.setText(selectedUsername);
            }
            else {
                selectedUsername = fileScanner.nextLine();
                name.setText(selectedUsername);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}


