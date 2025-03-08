module com.vietnamuniform {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    exports com.vietnamuniform.model;
    opens com.vietnamuniform to javafx.fxml;
    exports com.vietnamuniform;
    opens com.vietnamuniform.model to javafx.base;
    exports com.vietnamuniform.controller;
    opens com.vietnamuniform.controller to javafx.fxml;
}