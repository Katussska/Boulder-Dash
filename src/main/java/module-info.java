module mcboulderdash.temp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.almasb.fxgl.all;

    opens mcboulderdash.temp to javafx.fxml;
    exports mcboulderdash.temp;
}