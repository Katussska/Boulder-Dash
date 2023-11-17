package mcboulderdash;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;

public class Main extends GameApplication {
//    @Override
//    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        stage.setTitle("Hello!");
//        stage.setScene(scene);
//        stage.show();
//    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("MC Boulder Dash");
        settings.setWidth(1500);
        settings.setHeight(1000);
    }

    public static void main(String[] args) {
        launch(args);
    }
}