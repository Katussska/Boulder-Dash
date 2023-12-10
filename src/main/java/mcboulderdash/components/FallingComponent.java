package mcboulderdash.components;

import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;
import mcboulderdash.types.Direction;
import mcboulderdash.types.EntityType;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static mcboulderdash.Constants.TILE_SIZE;

public class FallingComponent extends Component {
    protected void fallSideways() {
        // je pod sutrem sutr?
        if (getGameWorld().getEntitiesAt(new Point2D(entity.getX(), entity.getY() + TILE_SIZE)).stream().anyMatch(s -> s.isType(EntityType.STONE))) {
            if (isNotRelativeEntityPresent(1, 0)
                    && isNotRelativeEntityPresent(1, 1)) {
                moveRight();
            }
            if (isNotRelativeEntityPresent(-1, 0)
                    && isNotRelativeEntityPresent(-1, 1)) {
                moveLeft();
            }
        }
    }

    protected boolean isNotRelativeEntityPresent(int x, int y) {
        return getGameWorld().getEntitiesAt(new Point2D(entity.getX() + x * TILE_SIZE, entity.getY() + y * TILE_SIZE)).isEmpty()
                && getGameWorld().getEntitiesAt(new Point2D(entity.getX() + 1 + x * TILE_SIZE, entity.getY() + 1 + y * TILE_SIZE)).isEmpty();
    }

    protected void moveRight() {
        entity.setPosition(new Point2D(entity.getX() + TILE_SIZE, entity.getY()));
    }

    protected void moveLeft() {
        entity.setPosition(new Point2D(entity.getX() - TILE_SIZE, entity.getY()));
    }

    protected void moveDown() {
        entity.setPosition(new Point2D(entity.getX(), entity.getY() + TILE_SIZE));
    }

    protected void moveX(Direction d) {
        entity.setPosition(new Point2D(entity.getX() + d.getValue() * TILE_SIZE, entity.getY()));
    }
}
