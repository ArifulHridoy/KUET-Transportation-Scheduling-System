module com.example.trscsy {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    exports com.example.trscsy;
    exports com.example.trscsy.model;

    opens com.example.trscsy to javafx.fxml;
    opens com.example.trscsy.controller to javafx.fxml;
}
