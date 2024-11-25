package com.apexdev.projectscarlet;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class videoPlayerController extends HelloApplication implements Initializable {


    private Scene preScene;

    @FXML
    private FontAwesomeIcon playButton;

    @FXML
    private MediaView mediaView;

    private static MediaPlayer mediaPlayer;

    @FXML
    private Slider progressBar;

    @FXML
    private JFXSlider volume;

    @FXML
    private JFXHamburger hamburger;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private Text timeDone, fullTime;



    @FXML
    void play(MouseEvent event) {
        //Detect if playing then play or stop


      boolean playing = mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING);

        System.out.println(playing);



        if (playing) {
            mediaPlayer.pause();

        } else {
            mediaPlayer.play();
        }

    }




    @FXML
    void chooseFile(MouseEvent event) throws Exception {

        // FIX THE FACT THAT I CANNOT SEND DATA FROM VIDEODASHBOARD TO VIDEOPLAYER. JUST GO THROUGH ORIGINAL CODE AND COMPARE AND FIGURE IT OUT. IMMA GO TO SLEEP

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("videoDashboard.fxml"));

        Scene newScene = new Scene(fxmlLoader.load(), 720, 480);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(newScene);

        stage.show();

        stage.setResizable(true);
        stage.setFullScreen(false);





        File file = new File(fileToPlay);


        if (path == null) {
            System.out.println("Invalid Video Link");
        }

            progressBar.setValue(0);
            mediaPlayer.seek(mediaPlayer.getStartTime());
            mediaPlayer.stop();
            Media media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);

    }




    String path;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

       //Get videoController to do OOP coding

       videoPlayerController videoPlayerController = new videoPlayerController();

       String fileToPlayOnVideo = videoPlayerController.getFileToPlay();

       System.out.println("Current File Name in videoPlayer is: " + fileToPlayOnVideo);


        //Getting the URL

        File file = new File("defaultVideo.mp4");
        path = file.toURI().toString();

        if(fileToPlayOnVideo != null) {

            file = new File(fileToPlayOnVideo);
            path = file.toURI().toString();
        }




        if (path != null) {



            Media media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);

            //This here is what Apparently does the responsive resizing


            //Double property is basically making a property value into a double

            DoubleProperty widthProp = mediaView.fitWidthProperty();

            DoubleProperty heightProp = mediaView.fitHeightProperty();

            widthProp.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
            heightProp.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));







            //I have no clue why it works but it just makes the progressbar work when moving
            progressBar.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    mediaPlayer.seek(Duration.seconds(progressBar.getValue()));
                }
            });

            //Check and fix everything when time is changing
            mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observableValue, Duration duration, Duration t1) {
                    progressBar.setValue(t1.toSeconds());

                    int timeDoneMinutes = (int)(t1.toMinutes());
                    int timeDoneSeconds = (int)(t1.toSeconds()%60);

                    String timeDoneFormat = String.format("%d:%02d", timeDoneMinutes, timeDoneSeconds);

                    timeDone.setText(timeDoneFormat);



                }
            });


            mediaPlayer.setOnReady(new Runnable() {
                @Override
                public void run() {
                    //Well just make the time run right
                    int fullTimeMinutes = (int) Math.round(mediaPlayer.getTotalDuration().toMinutes());
                    int fullTimeSeconds = (int) Math.round(mediaPlayer.getTotalDuration().toSeconds()/10);
                    String fullTimeFormat = String.format("%d:%02d", fullTimeMinutes, fullTimeSeconds);
                    System.out.println(fullTimeSeconds);

                    fullTime.setText(fullTimeFormat);

                    //Progress Bar

                    //When ready make the progress bar link with the time done
                    progressBar.maxProperty().bind(Bindings.createDoubleBinding(
                            () -> mediaPlayer.getTotalDuration().toSeconds(),
                            mediaPlayer.totalDurationProperty()));


                }
            });


            //Allow movement of the progress bar
            progressBar.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    mediaPlayer.pause();
                    mediaPlayer.seek(Duration.seconds(progressBar.getValue()));
                    mediaPlayer.play();
                }
            });


            mediaPlayer.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                }
            });

            //IF playing make the glyph the play button else make it pause

            mediaPlayer.setOnPlaying(new Runnable() {
                @Override
                public void run() {
                    playButton.setGlyphName("PAUSE");
                }
            });

            mediaPlayer.setOnPaused(new Runnable() {
                @Override
                public void run() {
                    playButton.setGlyphName("PLAY");
                }
            });





        }

        //SideBar BABY

        try {
            VBox vbox = FXMLLoader.load(getClass().getResource("sidePane.fxml"));
            drawer.setSidePane(vbox);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Animations and button
        HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(hamburger);
        transition.setRate(transition.getCurrentRate() - 1);
        transition.play();


        //Hide and show the button (so it doesn't become annoying)
        hamburger.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (hamburger.getOpacity() == 0) {
                    hamburger.setOpacity(1);
                }



            }
        });

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



//Hide and show the sidebar
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


        //Volume Settings


        volume.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                mediaPlayer.setVolume((Double) t1 /100);

            }
        });






    }


    public static void mediaPlayerControls(int control) {

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



    public videoPlayerController(){

    }


    public void setFileToPlay(String fileToPlay) {
        System.out.println("File name is: " + fileToPlay + " under setFileToPlay");
        this.fileToPlay = fileToPlay;
        System.out.println("File name of Object fileToPlay is now: " + this.fileToPlay);

    }

    public String getFileToPlay(){

        System.out.println("File name is: " + this.fileToPlay + " under getFileToPlay");
        return this.fileToPlay;
    }

    private static String fileToPlay;

}



