package com.retardalert.stupidapp;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.request.RequestSearchContinuation;
import com.github.kiulian.downloader.downloader.request.RequestSearchResult;
import com.github.kiulian.downloader.downloader.request.RequestSearchable;
import com.github.kiulian.downloader.model.search.SearchResult;
import com.github.kiulian.downloader.model.search.SearchResultVideoDetails;
import com.github.kiulian.downloader.model.search.field.TypeField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.Key;
import java.util.Objects;
import java.util.ResourceBundle;

public class QuestionController extends HelloApplication implements Initializable {


    @FXML
    private FontAwesomeIcon chooseFileButton, playButton, stopButton, searchButton;

    @FXML
    private MediaView mediaView;

    private static MediaPlayer mediaPlayer;

    @FXML
    private Slider progressBar;


    @FXML
    void play(MouseEvent event) {


        boolean playing = mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING);

        System.out.println("Is playing: " + playing);


        if (playing) {
            playButton.setGlyphName("PLAY");
            mediaPlayer.pause();

        } else {
            playButton.setGlyphName("PAUSE");
            mediaPlayer.play();
        }

    }

    @FXML
    void reset(MouseEvent event) {
        mediaPlayer.stop();

    }

    @FXML
    void searchYoutube(MouseEvent event) throws IOException {

        mediaPlayer.stop();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("webView.fxml"));
        Scene newScene = new Scene(fxmlLoader.load(), 640, 480);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(newScene);

        stage.show();
    }

    @FXML
    void stop(MouseEvent event) {
        mediaPlayer.pause();

    }

    @FXML
    void chooseFile(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = file = fileChooser.showOpenDialog(null);
        path = file.toURI().toString();

        if (path != null) {
            progressBar.setValue(0);
            mediaPlayer.seek(mediaPlayer.getStartTime());
            mediaPlayer.stop();
            Media media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);


            mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observableValue, Duration duration, Duration t1) {
                    progressBar.setValue(t1.toSeconds());
                }
            });

            progressBar.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    mediaPlayer.pause();
                    mediaPlayer.seek(Duration.seconds(progressBar.getValue()));
                    mediaPlayer.play();
                }
            });


        }

        mediaPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                progressBar.maxProperty().bind(Bindings.createDoubleBinding(
                        () -> mediaPlayer.getTotalDuration().toSeconds(),
                        mediaPlayer.totalDurationProperty()));

            }
        });

    }


    String path;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Getting the URL


        FileChooser fileChooser = new FileChooser();


        File file = new File("Shitpost.mp4");
        path = file.toURI().toString();
        if (path != null) {


            Media media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);

            //This here is what Apparently does the responsive resizing

            //*

            //Double property is basically making a property value into a double

            DoubleProperty widthProp = mediaView.fitWidthProperty();

            DoubleProperty heightProp = mediaView.fitHeightProperty();

            widthProp.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
            heightProp.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));

            //*

            //I have no clue why it works


            progressBar.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    mediaPlayer.seek(Duration.seconds(progressBar.getValue()));
                }
            });

            mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observableValue, Duration duration, Duration t1) {
                    progressBar.setValue(t1.toSeconds());
                }
            });


            mediaPlayer.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                }
            });


        }

    }

        public static void mediaPlayerControls( int control){

            if (control == 1) {
                mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(+5)));
            } else if (control == 2) {
                mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(-5)));
            } else if (control == 3) {
                mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(+10)));
                System.out.println("Forward (10 Seconds)");
                mediaPlayer.play();
            } else if (control == 4) {
                mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(-10)));
                System.out.println("Backward (10 Seconds)");
                mediaPlayer.play();
            } else if (control == 5) {
                if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) {
                    mediaPlayer.pause();

                } else {
                    mediaPlayer.play();
                }
            }

            if (control == 6) {

                if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) {
                    mediaPlayer.pause();


                } else if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PAUSED)) {
                    mediaPlayer.play();
                }
            }
        }
    }



