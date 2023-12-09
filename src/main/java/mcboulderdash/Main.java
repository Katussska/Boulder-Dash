package mcboulderdash;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.scene.SimpleGameMenu;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.entity.level.text.TextLevelLoader;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.pathfinding.CellState;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;
import javafx.scene.input.KeyCode;
import mcboulderdash.components.Player;

import static com.almasb.fxgl.dsl.FXGL.*;
import static mcboulderdash.Constants.TILE_SIZE;
import static mcboulderdash.EntityType.BRICK;
import static mcboulderdash.EntityType.WALL;

public class Main extends GameApplication {

    private AStarGrid grid;

    private Entity player;
    private Player playerComponent;

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
        settings.setWidth(600);
        settings.setHeight(600);
        settings.setSceneFactory(new SceneFactory() {
            @Override
            public FXGLMenu newGameMenu() {
                return new SimpleGameMenu();
            }
        });
    }

    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Move Up") {
            @Override
            protected void onActionBegin() {
                playerComponent.moveUp();
            }
        }, KeyCode.W);

        getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onActionBegin() {
                playerComponent.moveLeft();
            }
        }, KeyCode.A);

        getInput().addAction(new UserAction("Move Down") {
            @Override
            protected void onActionBegin() {
                playerComponent.moveDown();
            }
        }, KeyCode.S);

        getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onActionBegin() {
                playerComponent.moveRight();
            }
        }, KeyCode.D);

        getInput().addAction(new UserAction("Place Bomb") {
            @Override
            protected void onActionBegin() {
//                playerComponent.placeBomb(); 
            }
        }, KeyCode.F);
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new ObjectsFactory());

        Level level = getAssetLoader().loadLevel("0.txt", new TextLevelLoader(40, 40, '0'));
        getGameWorld().setLevel(level);

        spawn("BG");

        grid = AStarGrid.fromWorld(getGameWorld(), 15, 15, 40, 40, type -> {
            if (type.equals(WALL) || type.equals(BRICK))
                return CellState.NOT_WALKABLE;

            return CellState.WALKABLE;
        });

        player = spawn("Player");
        playerComponent = player.getComponent(Player.class);
    }

    @Override
    protected void initPhysics() {
//        onCollisionCollectible(PLAYER, POWERUP, powerup -> {
//            playerComponent.increaseMaxBombs();
//        });
    }

    public void onBrickDestroyed(Entity brick) {
        int cellX = (int) ((brick.getX() + 20) / TILE_SIZE);
        int cellY = (int) ((brick.getY() + 20) / TILE_SIZE);

        grid.get(cellX, cellY).setState(CellState.WALKABLE);

        if (FXGLMath.randomBoolean()) {
            spawn("Powerup", cellX * 40, cellY * 40);
        }
    }
}