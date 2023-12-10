package mcboulderdash.components;

import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class StarComponent extends FallingComponent implements Fallable {

    public void fall() {
        PauseTransition delay = new PauseTransition(Duration.seconds(0.3));
        delay.setOnFinished(e -> {
            if (this.entity == null)
                return;
            // je pod sutrem misto?
            if (isNotRelativeEntityPresent(0, 1)) {
                moveDown();
                this.fall();
            }
            // je pod sutrem sutr?
            fallSideways();
        });
        delay.play();
    }
}
