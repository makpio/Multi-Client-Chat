module org.pm {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.pm to javafx.fxml;
    exports org.pm;
}

