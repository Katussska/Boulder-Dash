package mcboulderdash.components;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.logging.Logger;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import javafx.geometry.Point2D;
import mcboulderdash.types.Direction;
import mcboulderdash.types.EntityType;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.inc;
import static mcboulderdash.Constants.TILE_SIZE;

public class PlayerComponent extends Component {

    protected final Logger log = Logger.get(PlayerComponent.class);
    public CellMoveComponent cell;
    public AStarMoveComponent astar;
    private int score = 0;

    public void moveRight() {
        // check for stones on the right
        var stone = getGameWorld()
                .getEntitiesAt(new Point2D(entity.getX() + TILE_SIZE - 1, entity.getY() - 1))
                .stream().findFirst().orElse(null);
        if (stone != null) {
            if (stone.isType(EntityType.STONE) && !stone.getComponent(StoneComponent.class).push(Direction.RIGHT)) {
                return;
            }
        }
        astar.moveToRightCell();
        log.info(String.format("Player grid position: [%d, %d]", cell.getCellX(), cell.getCellY()));
    }

    public void moveLeft() {
        // check for stones on the right
        var stone = getGameWorld()
                .getEntitiesAt(new Point2D(entity.getX() - TILE_SIZE - 1, entity.getY() - 1))
                .stream().findFirst().orElse(null);
        if (stone != null) {
            if (stone.isType(EntityType.STONE) && !stone.getComponent(StoneComponent.class).push(Direction.LEFT)) {
                return;
            }
        }
        astar.moveToLeftCell();
        log.info(String.format("Player grid position: [%d, %d]", cell.getCellX(), cell.getCellY()));
    }

    public void moveUp() {
        // check for stones above
        var stone = getGameWorld()
                .getEntitiesAt(new Point2D(entity.getX() - 1, entity.getY() - TILE_SIZE - 1))
                .stream().findFirst().orElse(null);
        if (stone != null) {
            if (stone.isType(EntityType.STONE)) {
                return;
            }
        }
        astar.moveToUpCell();
        log.info(String.format("Player grid position: [%d, %d]", cell.getCellX(), cell.getCellY()));
    }

    public void moveDown() {
        // check for stones underneath
        var stone = getGameWorld()
                .getEntitiesAt(new Point2D(entity.getX() - 1, entity.getY() + TILE_SIZE - 1))
                .stream().findFirst().orElse(null);
        if (stone != null) {
            if (stone.isType(EntityType.STONE)) {
                return;
            }
        }
        astar.moveToDownCell();
        log.info(String.format("Player grid position: [%d, %d]", cell.getCellX(), cell.getCellY()));
    }

    public void stopMovement() {
        astar.stopMovement();
        log.info(String.format("Player grid position: [%d, %d]", cell.getCellX(), cell.getCellY()));
    }

    public void addScore() {
        inc("score", +10);
        score += 10;
    }

    public int getScore() {
        return score;
    }
}
