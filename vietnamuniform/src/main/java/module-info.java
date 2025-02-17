module com.vietnamuniform {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.vietnamuniform to javafx.fxml;
    exports com.vietnamuniform;
}