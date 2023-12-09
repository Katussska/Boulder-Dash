package mcboulderdash;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import mcboulderdash.components.Player;

import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;
import static mcboulderdash.EntityType.PLAYER;

public class ObjectsFactory implements EntityFactory {

    @Spawns("BG")
    public Entity newBackground(SpawnData data) {
        return entityBuilder()
                .at(0, 0)
                .view(new Rectangle(600, 600, Color.LIGHTGREEN))
                .zIndex(-1)
                .build();
    }

    @Spawns("Player")
    public Entity newPlayer(SpawnData data) {
        return entityBuilder(data)
                .atAnchored(new Point2D(20, 20), new Point2D(20, 20))
                .type(PLAYER)
                .viewWithBBox(new Rectangle(Constants.TILE_SIZE, Constants.TILE_SIZE, Color.BLUE))
                .with(new CollidableComponent(true))
                .with(new CellMoveComponent(40, 40, 150))
                .with(new AStarMoveComponent(FXGL.<Main>getAppCast().getGrid()))
                .with(new Player())
                .build();
    }
}
