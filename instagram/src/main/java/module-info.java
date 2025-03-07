module ubb.scs.map.laborator_6nou {
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
    requires java.sql;
    requires org.jgrapht.core;
    requires jdk.jfr;
    requires java.desktop;

    opens ubb.scs.map.laborator_6nou to javafx.fxml;
    exports ubb.scs.map.laborator_6nou;
    exports ubb.scs.map.laborator_6nou.controller;
    opens ubb.scs.map.laborator_6nou.controller to javafx.fxml;
}
