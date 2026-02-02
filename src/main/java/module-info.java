module levi.calcolatrice {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens levi.calcolatrice to javafx.fxml;
    exports levi.calcolatrice;
    exports levi.calcolatrice.controller;
    opens levi.calcolatrice.controller to javafx.fxml;
}