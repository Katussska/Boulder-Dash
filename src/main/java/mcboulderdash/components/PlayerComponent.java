package mcboulderdash.components;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.logging.Logger;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;

import static com.almasb.fxgl.dsl.FXGL.inc;

public class PlayerComponent extends Component {

    protected final Logger log = Logger.get(PlayerComponent.class);
    public CellMoveComponent cell;
    public AStarMoveComponent astar;
    private int score = 0;

    public void moveRight() {
        astar.moveToRightCell();
        log.info(String.format("Player grid position: [%d, %d]", cell.getCellX(), cell.getCellY()));
    }

    public void moveLeft() {
        astar.moveToLeftCell();
        log.info(String.format("Player grid position: [%d, %d]", cell.getCellX(), cell.getCellY()));
    }

    public void moveUp() {
        astar.moveToUpCell();
        log.info(String.format("Player grid position: [%d, %d]", cell.getCellX(), cell.getCellY()));
    }

    public void moveDown() {
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
