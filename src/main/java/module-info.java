module mcboulderdash.temp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.almasb.fxgl.all;

    opens mcboulderdash to javafx.fxml;
    exports mcboulderdash;

    opens assets.textures;
    opens assets.levels;
}