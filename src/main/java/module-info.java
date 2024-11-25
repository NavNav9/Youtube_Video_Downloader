module com.apexdev.projectscarlet {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
   // requires com.almasb.fxgl.all;
    requires com.jfoenix;
    requires java.youtube.downloader;
    requires fontawesomefx;
    opens com.apexdev.projectscarlet to javafx.fxml;
    exports com.apexdev.projectscarlet;
}