package io.github.unisim.building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import io.github.unisim.GameState;
import io.github.unisim.Point;
import java.util.ArrayList;

/**
 * Manage the buildings placed in the world and methods common to all buildings.
 */
public class BuildingManager {
  private ArrayList<Building> buildings = new ArrayList<>();
  private Matrix4 isoTransform;

  public BuildingManager(Matrix4 isoTransform) {
    this.isoTransform = isoTransform;
    initBuildings();
  }

  /**
   * Determines if a region on the map is composed solely of buildable tiles.

   * @param btmLeft - The co-ordinates of the bottom left corner of the search region
   * @param topRight - The co-ordinates of the top right corner of the search region
   * @param tileLayer - A reference to the map layer containing all terrain tiles
   * @return - true if the region is made solely of buildable tiles, false otherwise
   */
  public static boolean isBuildable(Point btmLeft, Point topRight, TiledMapTileLayer tileLayer) {
    boolean buildable = true;
    // we iterate over each tile within the search region and check
    // for any non-buildable tiles.
    for (int x = btmLeft.x; x <= topRight.x && buildable; x++) {
      for (int y = btmLeft.y; y <= topRight.y && buildable; y++) {
        Cell currentCell = tileLayer.getCell(x, y);
        if (currentCell == null) {
          buildable = false;
          continue;
        }

        TiledMapTile currentTile = currentCell.getTile();
        if (!tileBuildable(currentTile)) {
          buildable = false;
        }
      }
    }
    // buildable will be true unless one or more non-buildable tiles are found within the region
    return buildable;
  }

  /**
   * Helper method that determines if the provided tile may be built on.

   * @param tile - A reference to a tile on the terrain layer of the map.
   * @return - true if the tile is buildable, false otherwise
   */
  private static boolean tileBuildable(TiledMapTile tile) {
    return GameState.buildableTiles.contains(tile.getId());
  }

  public void initBuildings() {
    Building houseTest = new Building(new Texture(
        Gdx.files.internal("building_2.png")), new Point(0, 0), new Point(3, 3)
    );
    buildings.add(houseTest);
  }

  /**
   * Draws each building from the building list onto the map.

   * @param batch - the SpriteBatch in which to draw
   */
  public void render(SpriteBatch batch) {
    for (Building building : buildings) {
      Vector3 worldPos = new Vector3(
          (float) building.location.x,
          (float) building.location.y,
          0f
      ).mul(isoTransform);
      batch.draw(
          building.texture, 
          worldPos.x, worldPos.y, 
          building.size.x, building.size.y
      );
    }
  }

  public void place(Building building) {
    // TODO: sort buildings in the list by position to ensure correct drawing order
    buildings.add(building);
  }
  
  /**
   * Draw the building texture at the position of the mouse cursor
   * when building mode is enabled.

   * @param building - The building to draw under the mouse cursor
   * @param batch - the SpriteBatch to draw into
   * @param cursorPos - The grid position of the cursor
   */
  public void drawAtCursor(Building building, SpriteBatch batch, Vector3 cursorPos) {

    // Vector3 worldPos = new Vector3(
    //       (float) cursorPos.x,
    //       (float) cursorPos.y,
    //       0f
    //   ).mul(isoTransform);
    batch.draw(building.texture, cursorPos.x, cursorPos.y, building.size.x, building.size.y);
  }
}
