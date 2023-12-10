package mcboulderdash.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import mcboulderdash.Main;
import mcboulderdash.types.EntityType;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;

public class Bomb extends Component {

    private final int radius;

    public Bomb(int radius) {
        this.radius = radius;
    }

    public void explode() {
        BoundingBoxComponent bbox = entity.getBoundingBoxComponent();

        getGameWorld()
                .getEntitiesInRange(bbox.range(radius, radius))
                .stream()
                .filter(e -> e.isType(EntityType.WALL))
                .forEach(e -> {
                    FXGL.<Main>getAppCast().onBrickDestroyed(e);
                    e.removeFromWorld();
                });

        entity.removeFromWorld();
    }
}
