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
import mcboulderdash.components.PlayerComponent;
import mcboulderdash.components.StarComponent;
import mcboulderdash.components.StoneComponent;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static mcboulderdash.Constants.TILE_SIZE;
import static mcboulderdash.types.EntityType.*;

public class ObjectsFactory implements EntityFactory {

    @Spawns("bg")
    public Entity newBackground(SpawnData data) {
        return entityBuilder()
                .at(0, 0)
                .view(new Rectangle(40 * TILE_SIZE, 22 * TILE_SIZE, Color.LIGHTGREEN))
                .zIndex(-1)
                .build();
    }

    @Spawns("player")
    public Entity newPlayer(SpawnData data) {
        return entityBuilder(data)
                .atAnchored(new Point2D(15, 15), new Point2D(108, 80))
//                .at(new Point2D(92, 64))
                .type(PLAYER)
                .viewWithBBox(new Rectangle(TILE_SIZE - 2, TILE_SIZE - 2, Color.BLUE))
                .with(new CollidableComponent(true))
                .with(new CellMoveComponent(TILE_SIZE, TILE_SIZE, 1000))
                .with(new AStarMoveComponent(FXGL.<Main>getAppCast().getGrid()))
                .with(new PlayerComponent())
                .build();
    }

    @Spawns("brick")
    public Entity newBrick(SpawnData data) {
        return entityBuilder(data)
                .type(WALL)
                .viewWithBBox("netherBricks.png")
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("stone")
    public Entity newStone(SpawnData data) {
        return entityBuilder(data)
//                .atAnchored(new Point2D(16, 16), new Point2D(data.getX() + 16, data.getY() + 16))
                .type(STONE)
                .viewWithBBox("obsidian.png")
                .with(new CollidableComponent(true))
                .with(new StoneComponent())
                .build();
    }

    @Spawns("dirt")
    public Entity newDirt(SpawnData data) {
        return entityBuilder(data)
                .type(DIRT)
                .viewWithBBox("netherrack.png")
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("star")
    public Entity newStar(SpawnData data) {
        return entityBuilder(data)
                .type(STAR)
                .viewWithBBox("netherStar.gif")
                .with(new CollidableComponent(true))
                .with(new StarComponent())
                .build();
    }

    @Spawns("portal")
    public Entity newPortal(SpawnData data) {
        return entityBuilder(data)
                .type(PORTAL)
                .viewWithBBox("netherPortal.gif")
                .with(new CollidableComponent(true))
                .build();
    }
}
