package com.apexdev.projectscarlet;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.client.ClientType;
import com.github.kiulian.downloader.downloader.request.RequestSearchResult;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.downloader.response.Response;
import com.github.kiulian.downloader.model.search.SearchResult;
import com.github.kiulian.downloader.model.search.SearchResultItem;
import com.github.kiulian.downloader.model.search.field.TypeField;
import com.github.kiulian.downloader.model.videos.VideoDetails;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.EventListener;
import java.util.List;

public class thumbnailGrabberForDashboard extends Thread {

    private GridPane videoList;



    @Override
    public void run() {
        File[] listOfFiles = getVideoList();

        //Now it should be back here so we can start coding the whole stupid interface in another class cause why not
        createDashboard(listOfFiles, videoList);
    }


    public thumbnailGrabberForDashboard(GridPane videoList) {
        this.videoList = videoList;
    }

    public thumbnailGrabberForDashboard() {
    }


    public static File[] getVideoList() {

        //Gonna thread this up

        File nameoFiles = new File("my_videos");
        File[] listOfFiles = nameoFiles.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                //System.out.println("File " + listOfFiles[i].getName());
            } else if (listOfFiles[i].isDirectory()) {
                //System.out.println("Directory " + listOfFiles[i].getName());
            }
        }

        return listOfFiles;
    }


    public static void createDashboard(File[] listOfFiles, GridPane videoList) {

        for (int i = 0; i < listOfFiles.length; i++) {

            //System.out.println("Numbers of iteration is : " + i);

            //Just for debugging this atrocity
           // videoList.setGridLinesVisible(true);


            //Find the name of the current file from the search of the folder
            String currentFileName = listOfFiles[i].toString();

            // Clean up the name
            currentFileName = currentFileName.replaceAll("my_videos/", "");

            //So apparently windows is a cunt and uses backslashes instead of forward
            currentFileName = currentFileName.replace("my_videos\\", "");

            currentFileName = currentFileName.replaceAll(".mp4", "");


            //Make a text node and add it to the gui and make sure it wraps
            Text fileName = new Text("\n\n" + currentFileName + "\n\n");
            fileName.setWrappingWidth(300);


            // Gonna make a thumbnail retriever so how this gonna work?

            int currentI = i;

            //BTW i'm too lazy to refactor this code so currentfile name is now called filenamesearch in get thumbnails :)

           //System.out.println("Before getThumbnailMethod");

            //Thread each of the thumbnail search
            String finalCurrentFileName = currentFileName;
            Thread printThumbnails = new Thread(new Runnable() {
                @Override
                public void run() {
                    getThumbnails(finalCurrentFileName, videoList, currentI,fileName,listOfFiles);
                }
            });

            printThumbnails.start();


        }
    }

    public static void getThumbnails(String fileNameSearch, GridPane videoList, int currentI, Text fileName, File[] listOfFiles) {

        YoutubeDownloader downloader = new YoutubeDownloader();

        RequestSearchResult request = new RequestSearchResult(fileNameSearch)
                .type(TypeField.VIDEO)
                .forceExactQuery(false);

        SearchResult result = downloader.search(request).data();

        List<SearchResultItem> items = result.items();
        SearchResultItem item = items.get(0);


        String videoID = item.asVideo().videoId();

        RequestVideoInfo requestVideoInfo = new RequestVideoInfo(videoID).clientType(ClientType.ANDROID_VR);
        Response<VideoInfo> videoInfoResponse = downloader.getVideoInfo(requestVideoInfo);

        VideoInfo video = videoInfoResponse.data();

        VideoDetails details = video.details();

        //System.out.println(details.thumbnails().get(2));

        String thumbnailURL = details.thumbnails().get(2);

        Image image = new Image(thumbnailURL);

        printOnGui(image, videoList, currentI,fileName,listOfFiles);

    }
    public static void printOnGui(Image image, GridPane videoList, int currentI, Text fileName, File[] listOfFiles) {
        Platform.runLater(() -> {

            //Just make image into thumbnail node :)

            ImageView thumbnail = new ImageView();
            thumbnail.setImage(image);
            thumbnail.setFitHeight(600);
            thumbnail.setFitWidth(400);
            thumbnail.setPreserveRatio(true);


            //Add delete button



            FontAwesomeIcon deleteButton = new FontAwesomeIcon();
            deleteButton.setGlyphName("TRASH");
            deleteButton.setSize("2em");

            //Make borderpane

            BorderPane deletePane = new BorderPane();

            deletePane.setBottom(deleteButton);

            deleteButton.setTranslateX(270);

            videoList.add(deletePane, 0, currentI+1);

            deleteButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    File deletedFile = listOfFiles[currentI];
                    Boolean isDeleted = deletedFile.delete();

                    //After deleted we will reload the thumbnails
                    if (isDeleted){
                        deleteButton.setGlyphName("CHECK");
                        //Delete everything in videoList so we can remake it
                        videoList.getChildren().clear();
                        //This has to be the worst optimized piece of shit

                        createDashboard(getVideoList(),videoList);
                    }


                }
            });


            thumbnail.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @FXML


                public void handle(MouseEvent mouseEvent) {


                    System.out.println("Setting videoFile Name");

                    //Here we gonna add the .mp4 at the end to allow the video be played
                    // We have to make currentFile again this is so unoptimized it's ridiculous

                    String currentFileName = listOfFiles[currentI].toString();

                    //currentFileName = currentFileName.replace("..mp4", ".mp4");


                    //Now make object pass the file name

                    thumbnailGrabberForDashboard sendFile = new thumbnailGrabberForDashboard();

                    sendFile.setFileToPlay(currentFileName);

                    //Get the previous Scene from videoDashboard

                    // videoDashboard previousSceneFinder = new videoDashboard();

                    // Scene previousScene = previousSceneFinder.getPreviousScene();

                    //Set fileToPlay on videoPlayerController

                    System.out.println("Current File Name in thumbnail Grabber is: " + currentFileName);

                    videoPlayerController fileSelcted = new videoPlayerController();

                    fileSelcted.setFileToPlay(currentFileName);


                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("newVideoPlayer.fxml"));

                    Scene newScene = null;
                    try {
                        newScene = new Scene(fxmlLoader.load(), 720, 480);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                    stage.setScene(newScene);
                    stage.show();




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
            });

            videoList.add(thumbnail, 1, currentI + 1);

            videoList.add(fileName, 0, currentI + 1);
        });
}

    public void setFileToPlay(String currentFileName){
        this.fileToPlay = currentFileName;
    }

    public String getFileName(){
        return fileToPlay;
    }

    private String fileToPlay;



}



