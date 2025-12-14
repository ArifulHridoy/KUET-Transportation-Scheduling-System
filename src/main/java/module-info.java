module com.example.kuet_transportation_and_schedueling_system {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.base;
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens com.example.kuet_transportation_and_schedueling_system to javafx.fxml;
    opens com.example.kuet_transportation_and_schedueling_system.controller to javafx.fxml;
    opens com.example.kuet_transportation_and_schedueling_system.model to javafx.base;
    exports com.example.kuet_transportation_and_schedueling_system;
    exports com.example.kuet_transportation_and_schedueling_system.controller;
    exports com.example.kuet_transportation_and_schedueling_system.model;
}