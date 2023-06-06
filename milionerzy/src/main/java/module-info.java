module com.example.milionerzy {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.milionerzy to javafx.fxml;
    exports com.example.milionerzy;
}