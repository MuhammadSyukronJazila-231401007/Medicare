module com.example.demo1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;

    opens com.example.demo1.Controller to javafx.fxml;
    exports com.example.demo1.Controller;

    opens com.example.demo1 to javafx.fxml;
    exports com.example.demo1;
    opens com.example.demo1.Model to javafx.base;

}