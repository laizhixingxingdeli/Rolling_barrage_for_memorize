module com.furj.danmu {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    opens com.furj.danmu to javafx.fxml;
    exports com.furj.danmu;
}