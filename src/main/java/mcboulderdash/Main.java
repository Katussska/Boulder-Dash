package mcboulderdash;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.scene.SimpleGameMenu;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.logging.Logger;
import com.almasb.fxgl.pathfinding.CellState;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import mcboulderdash.components.Fallable;
import mcboulderdash.components.PlayerComponent;
import mcboulderdash.components.StarComponent;
import mcboulderdash.components.StoneComponent;
import mcboulderdash.types.Direction;

import java.util.ArrayList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.*;
import static mcboulderdash.Constants.TILE_SIZE;
import static mcboulderdash.types.EntityType.*;

public class Main extends GameApplication {

    private final Logger log = Logger.get(Main.class);
    private AStarGrid grid;
    private Entity player;
    private PlayerComponent playerComponent;
    private List<Fallable> fallableComponents;


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
        settings.setSceneFactory(new SceneFactory() {
            @Override
            public FXGLMenu newGameMenu() {
                var menu = new SimpleGameMenu();
                Button btn = getUIFactoryService().newButton("lol");
                menu.addChild(btn);
                return menu;
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

        player = spawn("player");
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
        onCollision(PLAYER, DIRT, (p, d) -> d.removeFromWorld());

        onCollision(PLAYER, STAR, (p, s) -> {
            playerComponent.addScore();
            s.removeFromWorld();
        });

        onCollision(PLAYER, STONE, (p, s) -> {
            // stone can be moved sideways only
            if (s.getY() != p.getY() - 1) {
                playerComponent.stopMovement();
                return;
            }
            var d = s.getX() > p.getX() ? Direction.RIGHT : Direction.LEFT;
            if (!s.getComponent(StoneComponent.class).push(d)) {
                playerComponent.stopMovement();
            }
        });

        onCollision(PLAYER, PORTAL, (p, po) -> gameOver());
    }

    private void stoneFall() {
        fallableComponents.forEach(Fallable::fall);
    }

    public void gameOver() {
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
}