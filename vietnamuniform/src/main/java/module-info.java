module com.vietnamuniform {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.vietnamuniform to javafx.fxml;
    exports com.vietnamuniform;
    exports com.vietnamuniform.controller;
    opens com.vietnamuniform.controller to javafx.fxml;
}