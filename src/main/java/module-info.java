module com.example.bitebook {
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
    requires javafx.graphics;
    requires java.desktop;
    requires java.sql;
    requires javafx.base;
    requires java.logging;
    requires com.example.bitebook;

    exports com.example.bitebook.model;
    exports com.example.bitebook.model.enums;
    exports com.example.bitebook.model.bean;
    exports com.example.bitebook.controller.view1;
    exports com.example.bitebook.controller.view2;
    opens com.example.bitebook.controller.view1 to javafx.fxml;
    opens com.example.bitebook.controller.view2 to javafx.fxml;
}