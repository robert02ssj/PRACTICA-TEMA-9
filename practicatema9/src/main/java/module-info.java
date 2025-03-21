module com.ssj {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires transitive java.sql;
    requires javafx.base;
    opens com.ssj to javafx.fxml;
    exports com.ssj;
}
