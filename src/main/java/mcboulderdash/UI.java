package mcboulderdash;

import com.almasb.fxgl.app.scene.SimpleGameMenu;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.*;
import static mcboulderdash.Constants.SCORE_FILE;

public class UI {

    public UI() {
    }

    public void initUI() {
        Text scoreText = new Text();
        scoreText.setTranslateX(32);
        scoreText.setTranslateY(18);
        scoreText.setLayoutY(10);
        scoreText.textProperty().bind(getWorldProperties().intProperty("score").asString());
        scoreText.setFont(Font.font("Impact", 25));
        scoreText.setFill(Color.WHITE);
        getGameScene().addUINode(scoreText);
    }

    public SimpleGameMenu initGameMenu() {
        var menu = new SimpleGameMenu();
        Button restartBtn = getUIFactoryService().newButton("RESTART");
        restartBtn.setTranslateX(558);
        restartBtn.setTranslateY(268);
        restartBtn.setOnAction(e -> getGameController().startNewGame());
        Button scoresBtn = getUIFactoryService().newButton("SCORES");
        scoresBtn.setTranslateX(558);
        scoresBtn.setTranslateY(468);
        scoresBtn.setOnAction(e -> showScores());
        menu.addChild(restartBtn);
        menu.addChild(scoresBtn);
        return menu;
    }

    private void showScores() {
        var scores = loadScores().stream().limit(20).sorted(Comparator.comparingInt(Score::score).reversed()).toList();
        VBox content = new VBox();

        int index = 0;
        for (Score s : scores) {
            content.getChildren().add(index, getUIFactoryService().newText(String.format("%s\t%d", s.playerName(), s.score())));
            index++;
        }

        Button btnClose = getUIFactoryService().newButton("OK");
        btnClose.setPrefWidth(300);
        getDialogService().showBox("HIGH SCORES", content, btnClose);
    }

    private List<Score> loadScores() {
        List<Score> scores = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(SCORE_FILE))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");

                scores.add(new Score(parts[0].trim(), Integer.parseInt(parts[1].trim())));
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return scores;
    }
}
