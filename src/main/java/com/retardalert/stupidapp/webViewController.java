package com.retardalert.stupidapp;


import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.request.RequestVideoFileDownload;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.downloader.response.Response;
import com.github.kiulian.downloader.model.Extension;
import com.github.kiulian.downloader.model.Filter;
import com.github.kiulian.downloader.model.videos.VideoDetails;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import com.github.kiulian.downloader.model.videos.formats.Format;
import com.github.kiulian.downloader.model.videos.formats.VideoFormat;
import com.github.kiulian.downloader.model.videos.formats.VideoWithAudioFormat;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.util.*;
import java.net.URLConnection;

public class webViewController extends HelloApplication implements Initializable   {
    @FXML
    private WebView webView;

    @FXML
    private TextField urlField;

    @FXML
    private WebEngine webEngine;

    @FXML
    private Button loadButton, converterButton;






    @Override
    public void initialize(URL location, ResourceBundle resources) {

        webEngine = webView.getEngine();
        webView.getEngine().setJavaScriptEnabled(true);
        loadPage();



    }


    public void loadPage(){

        webEngine.load("http://youtube.com");

    }






    @FXML
    void converterButton(javafx.event.ActionEvent event) throws IOException, InterruptedException {


        //Converter thingy

        //Get the url from the textfield that contains the url :)

        String videoUrl = urlField.getText();

        videoUrl = videoUrl.substring(32);

        System.out.println(videoUrl);

        YoutubeDownloader downloader = new YoutubeDownloader();


        // sync parsing, basically requests youtube info using youtube api dl (some garbage google made)
        RequestVideoInfo requestVideoInfo = new RequestVideoInfo(videoUrl);
        Response<VideoInfo> videoInfoResponse = downloader.getVideoInfo(requestVideoInfo);
        VideoInfo video = videoInfoResponse.data();


        // video details this gets the details such as thumbnails and
        VideoDetails details = video.details();
        System.out.println("Title: " + details.title());
        //See if I can get the images, maybe temp image files
        details.thumbnails().forEach(image -> System.out.println("Thumbnail: " + image));



        // get videos formats only with audio
        List<VideoWithAudioFormat> videoWithAudioFormats = video.videoWithAudioFormats();
        videoWithAudioFormats.forEach(it -> {
            System.out.println(it.audioQuality() + ", " + it.videoQuality() + " : " + it.url());
        });


                    String videoId = videoUrl; // uses the stuff in the textfield of the url thingy of my crappy web engine

        // get best format get's the best format
        video.bestVideoWithAudioFormat();
        video.bestVideoFormat();
        video.bestAudioFormat();



        File outputDir = new File("my_videos");
                    //Gets the best video and audio format (Highest I can access)
                    Format format = video.bestVideoWithAudioFormat();




// sync downloading
                    RequestVideoFileDownload requestVideoFileDownload = new RequestVideoFileDownload(format)
                            // optional params
                            .saveTo(outputDir) // by default "videos" directory
                            .renameTo(details.title()) //Renames the File to the Title of the video
                            .overwriteIfExists(true); // if false and file with such name already exits sufix will be added video(1).mp4
                    Response<File> responses = downloader.downloadVideoFile(requestVideoFileDownload);
                    File data = responses.data();

        System.out.println(responses.status());

        




                    // Load back the video player


        Thread.sleep(5000);

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("firstquestion.fxml"));
            Scene newScene = new Scene(fxmlLoader.load(), 640, 480);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(newScene);

            stage.show();



    }









    @FXML
    void loadWebsite(){
        webEngine.load("http://" + urlField.getText());

        //Linking the properties to it always updates the url
        urlField.textProperty().bind(webEngine.locationProperty());

    }




    public class YoutubeVideoDownloader {


    }

}
