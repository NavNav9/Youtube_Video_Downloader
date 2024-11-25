package com.retardalert.stupidapp;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.request.RequestSearchContinuation;
import com.github.kiulian.downloader.downloader.request.RequestSearchResult;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.downloader.response.Response;
import com.github.kiulian.downloader.model.search.SearchResult;
import com.github.kiulian.downloader.model.search.SearchResultItem;
import com.github.kiulian.downloader.model.search.SearchResultShelf;
import com.github.kiulian.downloader.model.search.SearchResultVideoDetails;
import com.github.kiulian.downloader.model.search.field.TypeField;
import com.github.kiulian.downloader.model.videos.VideoDetails;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.EventListener;
import java.util.List;

public class youtubeSearchController {

    @FXML
    private ScrollPane scrollBar;

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
        searchBar.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent key) {
                if (key.getCode().equals(KeyCode.ENTER)){
                    String search = searchBar.getText();
                    searchResults(search);
                }
            }
        });


    }



    private void searchResults(String search){



        YoutubeDownloader downloader = new YoutubeDownloader();


        for (int i = 0; i < 5 ; i++) {


            RequestSearchResult request = new RequestSearchResult(search)
                    .type(TypeField.VIDEO);

            SearchResult result = downloader.search(request).data();

            List<SearchResultItem> items = result.items();
            List<SearchResultShelf> shelves = result.shelves();


            // item cast
            SearchResultItem item = items.get(i);


            String videoID = item.asVideo().videoId();

            RequestVideoInfo requestVideoInfo = new RequestVideoInfo(videoID);
            Response<VideoInfo> videoInfoResponse = downloader.getVideoInfo(requestVideoInfo);

            VideoInfo video = videoInfoResponse.data();

            VideoDetails details = video.details();
            System.out.println(details.thumbnails().get(3));

            String thumbnailURL = details.thumbnails().get(3);


            String title = item.title();


            Image image = new Image(thumbnailURL);

            System.out.println(item.asVideo().description());

            System.out.println("adding...");

            ImageView thumbnail = new ImageView();
            thumbnail.setImage(image);


            gridPane.add(thumbnail, 0, i);
            thumbnail.setFitHeight(120);
            thumbnail.setFitWidth(168);
            thumbnail.setPreserveRatio(true);





            if (image.isError()) {
                System.out.println("Error loading image from " + thumbnailURL);
                // if you need more details
                //thumbnail1Image.getException().printStackTrace();
            } else {
                System.out.println("Successfully loaded image from " + thumbnailURL);
            }

        }









       // thumbnailObject thumbnailObject = new thumbnailObject();


        scrollBar.vvalueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {

                 if (scrollBar.getVvalue() >= scrollBar.getVmax() - 0.2){
                    System.out.println("End");
                   // thumbnailObject.getThumbnailImages(search,gridPane);
                     Thread predownload = new Thread(new Runnable() {
                         @Override
                         public void run() {
                             System.out.println("Test");

                             System.out.println(searchBar.getText());
                         }
                     });
                     predownload.start();
                }

            }
        });






    }

}
