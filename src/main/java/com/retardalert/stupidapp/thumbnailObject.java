package com.retardalert.stupidapp;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.request.RequestSearchResult;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.downloader.response.Response;
import com.github.kiulian.downloader.model.search.SearchResult;
import com.github.kiulian.downloader.model.search.SearchResultItem;
import com.github.kiulian.downloader.model.search.SearchResultShelf;
import com.github.kiulian.downloader.model.search.field.TypeField;
import com.github.kiulian.downloader.model.videos.VideoDetails;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.spreadsheet.Grid;


import java.util.List;


public class thumbnailObject extends Thread implements Runnable {
    // What do I even call this ;)


    private String videoID;

    private static int count = 6;

    private String search;

    private GridPane gridPane;
    public thumbnailObject(GridPane gridPane, String search) {
        this.gridPane = gridPane;
        this.search = search;
    }



    public static void getThumbnailImages(String search, GridPane gridPane) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                    System.out.println("In Thread");

                    YoutubeDownloader downloader = new YoutubeDownloader();


                    RequestSearchResult request = new RequestSearchResult(search)
                            .type(TypeField.VIDEO);

                    SearchResult result = downloader.search(request).data();

                    List<SearchResultItem> items = result.items();
                    List<SearchResultShelf> shelves = result.shelves();


                    // item cast
                    SearchResultItem item = items.get(count);


                    String videoID = item.asVideo().videoId();

                    RequestVideoInfo requestVideoInfo = new RequestVideoInfo(videoID);
                    Response<VideoInfo> videoInfoResponse = downloader.getVideoInfo(requestVideoInfo);

                    VideoInfo video = videoInfoResponse.data();

                    VideoDetails details = video.details();
                    System.out.println(details.thumbnails().get(2));

                    String thumbnailURL = details.thumbnails().get(2);


                    String title = item.title();


                    Image image = new Image(thumbnailURL);

                    System.out.println(item.asVideo().description());

                    System.out.println("adding...");
                    Label test = new Label("test");

                    ImageView thumbnail = new ImageView();
                    thumbnail.setImage(image);


                    gridPane.add(thumbnail, 0, count);
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


        });





    }


}
