module com.ssj {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;
    requires javafx.base;
    opens com.ssj to javafx.fxml;
    exports com.ssj;
}
