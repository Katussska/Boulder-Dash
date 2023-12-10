package mcboulderdash.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.logging.Logger;
import javafx.animation.PauseTransition;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import mcboulderdash.Main;
import mcboulderdash.types.Direction;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static mcboulderdash.Constants.TILE_SIZE;

public class StoneComponent extends FallingComponent implements Fallable {
    private final Logger log = Logger.get(StoneComponent.class);
    private boolean lethal = false;

    public void fall() {
        PauseTransition delay = new PauseTransition(Duration.seconds(0.3));
        delay.setOnFinished(e -> {
            if (!getGameWorld().getEntitiesAt(new Point2D(entity.getX() + 1, entity.getY() + TILE_SIZE + 1)).isEmpty() && lethal) {
                FXGL.<Main>getAppCast().gameOver();
            }
            // je pod sutrem misto?
            if (isNotRelativeEntityPresent(0, 1)) {
                moveDown();
                lethal = true;
                this.fall();
                return;
            }
            // je pod sutrem sutr?
            fallSideways();
            lethal = false;
        });
        delay.play();
    }

    public boolean push(Direction d) {
        if (isNotRelativeEntityPresent(d.getValue(), 0)) {
            moveX(d);
            return true;
        }
        return false;
    }
}
