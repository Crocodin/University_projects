module MAP.IR.Social.Network.main {
    requires java.sql;
    requires javafx.base;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.controls;

    requires static lombok;

    opens com.ubb.gui to javafx.fxml;
    exports com.ubb.gui;
}