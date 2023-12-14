package mcboulderdash;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.pathfinding.CellState;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;
import javafx.scene.input.KeyCode;
import mcboulderdash.components.Fallable;
import mcboulderdash.components.PlayerComponent;
import mcboulderdash.components.StarComponent;
import mcboulderdash.components.StoneComponent;
import mcboulderdash.types.Direction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;
import static mcboulderdash.Constants.SCORE_FILE;
import static mcboulderdash.Constants.TILE_SIZE;
import static mcboulderdash.types.EntityType.*;

public class Main extends GameApplication {

    private AStarGrid grid;
    private UI ui;
    private PlayerComponent playerComponent;
    private List<Fallable> fallableComponents;
    private String playerName = "default";


    public static void main(String[] args) {
        launch(args);
    }

    public AStarGrid getGrid() {
        return grid;
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Boulder Dash App");
        settings.setVersion("0.1");
        settings.setWidth(40 * TILE_SIZE);
        settings.setHeight(22 * TILE_SIZE);
//        settings.setDeveloperMenuEnabled(true);
        ui = new UI();
        settings.setSceneFactory(new SceneFactory() {
            @Override
            public FXGLMenu newGameMenu() {
                return ui.initGameMenu();
            }
        });
    }

    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Move Up") {
            @Override
            protected void onActionBegin() {
                playerComponent.moveUp();
                stoneFall();
            }
        }, KeyCode.W);

        getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onActionBegin() {
                playerComponent.moveLeft();
                stoneFall();
            }
        }, KeyCode.A);

        getInput().addAction(new UserAction("Move Down") {
            @Override
            protected void onActionBegin() {
                playerComponent.moveDown();
                stoneFall();
            }
        }, KeyCode.S);

        getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onActionBegin() {
                playerComponent.moveRight();
                stoneFall();
            }
        }, KeyCode.D);

        getInput().addAction(new UserAction("Set PlayerName") {
            @Override
            protected void onAction() {
                setPlayerName();
            }
        }, KeyCode.N);
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new ObjectsFactory());

        setLevelFromMap("01.tmx");

        grid = AStarGrid.fromWorld(getGameWorld(), 40, 22, TILE_SIZE, TILE_SIZE, type -> {
            if (type.equals(WALL))
                return CellState.NOT_WALKABLE;

            return CellState.WALKABLE;
        });

        Entity player = spawn("player");
        playerComponent = player.getComponent(PlayerComponent.class);

        fallableComponents = new ArrayList<>();
        getGameWorld()
                .getEntitiesByType(STONE)
                .stream()
                .map(e -> e.getComponent(StoneComponent.class))
                .forEach(s -> fallableComponents.add(s));
        getGameWorld()
                .getEntitiesByType(STAR)
                .stream()
                .map(e -> e.getComponent(StarComponent.class))
                .forEach(s -> fallableComponents.add(s));
    }

    @Override
    protected void initPhysics() {
        onCollision(PLAYER, DIRT, (p, d) -> {
            d.removeFromWorld();
        });

        onCollision(PLAYER, STAR, (p, s) -> {
            playerComponent.addScore();
            s.removeFromWorld();
            Sounds.playCollect();
        });

        onCollision(PLAYER, STONE, (p, s) -> {
//            // stone can be moved sideways only
//            if (s.getY() != p.getY() - 1) {
//                playerComponent.stopMovement();
//                return;
//            }
            var d = s.getX() > p.getX() ? Direction.RIGHT : Direction.LEFT;
            if (!s.getComponent(StoneComponent.class).push(d)) {
                playerComponent.stopMovement();
            }
        });

        onCollision(PLAYER, PORTAL, (p, po) -> {
            gameOver();
            Sounds.playPortal();
        });
    }

    @Override
    protected void initUI() {
        ui.initUI();
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("score", 0);
    }

    private void stoneFall() {
        fallableComponents.forEach(Fallable::fall);
    }

    private void saveScores() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCORE_FILE, true))) {
            File file = new File(SCORE_FILE);

            if (!file.exists()) {
                file.createNewFile();
            }

            writer.write(playerName + ";" + playerComponent.getScore());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gameOver() {
        saveScores();
        getDialogService()
                .showConfirmationBox(
                        String.format("Game over. Score: %d. Restart?", playerComponent.getScore()), res -> {
                            if (res) {
                                getGameController().startNewGame();
                                getGameController().resumeEngine();
                            } else
                                getGameController().exit();
                        });
        getGameController().pauseEngine();
    }

    private void setPlayerName() {
        getDialogService().showInputBox("Player name:", n -> this.playerName = n);
    }
}