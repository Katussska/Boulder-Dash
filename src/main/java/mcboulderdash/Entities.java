package mcboulderdash;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.geometry.Point2D;

import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;

public class Entities implements EntityFactory {

    @Spawns("Player")
    public Entity newPlayer(SpawnData data) {
        return entityBuilder(data)
                .atAnchored(new Point2D(20, 20), new Point2D(20, 20))
    }
}
