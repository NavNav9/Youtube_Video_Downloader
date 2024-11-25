package com.apexdev.projectscarlet;

import com.github.kiulian.downloader.Config;
import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.YoutubeProgressCallback;
import com.github.kiulian.downloader.downloader.client.ClientType;
import com.github.kiulian.downloader.downloader.request.RequestSearchResult;
import com.github.kiulian.downloader.downloader.request.RequestVideoFileDownload;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.downloader.response.Response;
import com.github.kiulian.downloader.model.search.SearchResult;
import com.github.kiulian.downloader.model.search.SearchResultItem;
import com.github.kiulian.downloader.model.search.SearchResultShelf;
import com.github.kiulian.downloader.model.search.field.TypeField;
import com.github.kiulian.downloader.model.videos.VideoDetails;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import com.github.kiulian.downloader.model.videos.formats.Format;
import com.github.kiulian.downloader.model.videos.formats.VideoWithAudioFormat;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


import java.io.File;
import java.util.List;


public class YoutubeObject extends Thread {
    // What do I even call this ;)


    private Object isComplete;

    private String search;

    private GridPane gridPane;

    private int count = 0;

    public YoutubeObject(GridPane gridPane, String search, Object isComplete) {
        this.gridPane = gridPane;
        this.search = search;
        this.isComplete = isComplete;
    }


    @Override
    public void run() {

        //Searching YouTube, credits goes to SealedTX on GitHub. Saint for making this

        YoutubeDownloader downloader = new YoutubeDownloader();

        RequestSearchResult request = new RequestSearchResult(search)
                .type(TypeField.VIDEO)
                .forceExactQuery(false);

        SearchResult result = downloader.search(request).data();

        List<SearchResultItem> items = result.items();


        //Each thread  get's the items and sends it to be printed on the GUI
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                int finalI = 0;

                System.out.println("In Thread 1");

                for (int i = 0; i < 5; i++) {

                    // item cast
                    SearchResultItem item = items.get(i);


                    String videoID = item.asVideo().videoId();

                    System.out.println(videoID);

                    RequestVideoInfo requestVideoInfo = new RequestVideoInfo(videoID).clientType(ClientType.ANDROID_VR);
                    Response<VideoInfo> videoInfoResponse = downloader.getVideoInfo(requestVideoInfo);

                    VideoInfo video = videoInfoResponse.data();

                    VideoDetails details = video.details();

                    System.out.println(details.thumbnails().get(2));

                    String thumbnailURL = details.thumbnails().get(2);


                    String title = item.title();


                    Image image = new Image(thumbnailURL);

                    System.out.println(item.asVideo().description());

                    System.out.println("Done Getting Info");

                    finalI = i;

                    printThumbnailOnGUI(finalI, thumbnailURL, image, title, videoID);

                }
            }
        });


         Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                int finalI = 0;

                System.out.println("In Thread 1");

                for (int i = 0; i < 5; i++) {

                    // item cast
                    SearchResultItem item = items.get(i);


                    String videoID = item.asVideo().videoId();

                    RequestVideoInfo requestVideoInfo = new RequestVideoInfo(videoID).clientType(ClientType.ANDROID_VR);
                    Response<VideoInfo> videoInfoResponse = downloader.getVideoInfo(requestVideoInfo);

                    VideoInfo video = videoInfoResponse.data();

                    VideoDetails details = video.details();
                    System.out.println(details.thumbnails().get(2));

                    String thumbnailURL = details.thumbnails().get(2);


                    String title = item.title();


                    Image image = new Image(thumbnailURL);

                    System.out.println(item.asVideo().description());

                    System.out.println("Done Getting Info");

                    finalI = i;

                    printThumbnailOnGUI(finalI, thumbnailURL, image, title, videoID);
                }
            }
        });

        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                int finalI = 0;

                System.out.println("In Thread 3");

                for (int i = count; i < 5 + count; i++) {

                    // item cast
                    SearchResultItem item = items.get(i + 10);


                    String videoID = item.asVideo().videoId();

                    RequestVideoInfo requestVideoInfo = new RequestVideoInfo(videoID).clientType(ClientType.ANDROID_VR);
                    Response<VideoInfo> videoInfoResponse = downloader.getVideoInfo(requestVideoInfo);

                    VideoInfo video = videoInfoResponse.data();

                    VideoDetails details = video.details();
                    System.out.println(details.thumbnails().get(2));

                    String thumbnailURL = details.thumbnails().get(2);


                    String title = item.title();


                    Image image = new Image(thumbnailURL);

                    System.out.println(item.asVideo().description());

                    System.out.println("Done Getting Info");

                    finalI = i + 10;

                    printThumbnailOnGUI(finalI, thumbnailURL, image, title, videoID);

                }
            }

        });

        Thread thread4 = new Thread(new Runnable() {
            @Override
            public void run() {
                int finalI = 0;

                System.out.println("In Thread 3");

                for (int i = count; i < 5 + count; i++) {

                    // item cast
                    SearchResultItem item = items.get(i + 15);


                    String videoID = item.asVideo().videoId();

                    RequestVideoInfo requestVideoInfo = new RequestVideoInfo(videoID).clientType(ClientType.ANDROID_VR);
                    Response<VideoInfo> videoInfoResponse = downloader.getVideoInfo(requestVideoInfo);

                    VideoInfo video = videoInfoResponse.data();

                    VideoDetails details = video.details();
                    System.out.println(details.thumbnails().get(2));

                    String thumbnailURL = details.thumbnails().get(2);


                    String title = item.title();


                    Image image = new Image(thumbnailURL);

                    System.out.println(item.asVideo().description());

                    System.out.println("Done Getting Info");

                    finalI = i + 15;

                    printThumbnailOnGUI(finalI, thumbnailURL, image, title, videoID);

                }

            }
        });



        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();


        //Not needed anymore cause the api doesn't allow more than 20 results
        //Quite pathetic imo
        //Was supposed to allow infinite results

        /*   synchronized (isComplete) {
                try {
                    isComplete.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

         */

    }

    private void printThumbnailOnGUI(int finalI, String thumbnailURL, Image image, String title, String videoID) {

        Platform.runLater(() -> {

            //Print everything to the GUI (GridPane)

            System.out.println("adding...");

            //Make the thumbnail Images

            ImageView thumbnail = new ImageView();
            thumbnail.setImage(image);
            thumbnail.setFitHeight(120);
            thumbnail.setFitWidth(168);
            thumbnail.setPreserveRatio(true);


            //Make the titles
            Text titles = new Text(title);

            titles.setFont(Font.font("Sans Serif", FontWeight.BOLD, 15));
            titles.setWrappingWidth(424);
            titles.setTranslateY(-30);


            //Make download Progress

            ProgressBar downloadProgress = new ProgressBar();
            downloadProgress.setTranslateX(228);
            downloadProgress.setTranslateX(228);
            downloadProgress.setTranslateY(20);
            downloadProgress.setProgress(0);


            //Make Download Button
            FontAwesomeIcon downloadIcons = new FontAwesomeIcon();
            downloadIcons.setGlyphName("DOWNLOAD");
            downloadIcons.setSize("2em");
            downloadIcons.setTranslateX(360);
            downloadIcons.setTranslateY(30);
            downloadIcons.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    Thread downloadVideos = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println(videoID);

                            YoutubeDownloader downloader = new YoutubeDownloader();


                            // sync parsing, basically requests youtube info using youtube api dl (some garbage google made)
                            RequestVideoInfo requestVideoInfo = new RequestVideoInfo(videoID);
                            Response<VideoInfo> videoInfoResponse = downloader.getVideoInfo(requestVideoInfo);
                            VideoInfo video = videoInfoResponse.data();


                            // video details this gets the details such as thumbnails and
                            VideoDetails details = video.details();


                            //get title

                            String title = details.title();


                            // get videos formats only with audio
                            List<VideoWithAudioFormat> videoWithAudioFormats = video.videoWithAudioFormats();
                            videoWithAudioFormats.forEach(it -> {
                                System.out.println(it.audioQuality() + ", " + it.videoQuality() + " : " + it.url());
                            });


                            // get best format get's the best format
                            video.bestVideoWithAudioFormat();
                            video.bestVideoFormat();
                            video.bestAudioFormat();


                            File outputDir = new File("my_videos");
                            //Gets the best video and audio format (Highest I can access)
                            Format format = video.bestVideoWithAudioFormat();


// async downloading
                            RequestVideoFileDownload request = new RequestVideoFileDownload(format)
                                    .saveTo(outputDir) // by default "videos" directory
                                    .renameTo(title) // by default file name will be same as video title on youtube
                                    .overwriteIfExists(true) // if false and file with such name already exits sufix will be added video(1).mp4
                                    .callback(new YoutubeProgressCallback<File>() {

                                        @Override
                                        public void onDownloading(int progress) {
                                            //Setup the Progress Bar again

                                            System.out.printf("Downloaded %d%%\n", progress);

                                            Task<Void> task = new Task<Void>() {
                                                @Override
                                                protected Void call() throws Exception {
                                                    double currentProgress = (double) progress / 100;
                                                    updateProgress(currentProgress, 1);


                                                    return null;
                                                }
                                            };

                                            //Tell the Task thread to run

                                            task.run();


                                            //Bind the progressBar
                                            Platform.runLater(new Runnable() {
                                                @Override
                                                public void run() {
                                                    downloadProgress.progressProperty().bind(task.progressProperty());
                                                }
                                            });


                                        }


                                        @Override
                                        public void onFinished(File videoInfo) {
                                            //Say it's finished when done
                                            System.out.println("Finished Download");
                                        }

                                        @Override
                                        public void onError(Throwable throwable) {
                                            //Say it's failed
                                            System.out.println("Error: " + throwable.getLocalizedMessage());
                                        }
                                    })
                                    .async();
                            Response<File> response = downloader.downloadVideoFile(request);
                            File data = response.data(); // will block current thread


                        }
                    });

                    //Run thread
                    downloadVideos.start();
                }
            });


            //Print them out
            gridPane.add(thumbnail, 0, finalI);
            gridPane.add(titles, 1, finalI);
            gridPane.add(downloadIcons, 1, finalI);
            gridPane.add(downloadProgress, 1, finalI);




            //States if the Image worked or not
            if (image.isError()) {
                System.out.println("Error loading image from " + thumbnailURL);
                // if you need more details
                //thumbnail1Image.getException().printStackTrace();
            } else {
                System.out.println("Successfully loaded image from " + thumbnailURL);
            }


        });

    }


}







