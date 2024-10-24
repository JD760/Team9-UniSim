package io.github.unisim.building;

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
  private Building previewBuilding;

  public BuildingManager(Matrix4 isoTransform) {
    this.isoTransform = isoTransform;
  }

  /**
   * Determines if a region on the map is composed solely of buildable tiles.

   * @param btmLeft - The co-ordinates of the bottom left corner of the search region
   * @param topRight - The co-ordinates of the top right corner of the search region
   * @param tileLayer - A reference to the map layer containing all terrain tiles
   * @return - true if the region is made solely of buildable tiles, false otherwise
   */
  public boolean isBuildable(Point btmLeft, Point topRight, TiledMapTileLayer tileLayer) {
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
    if (!buildable) {
      return false;
    }

    // Next, iterate over the current buildings to see if any intersect the new building
    for (Building building : buildings) {
      // Use the seperating axis theorem to detect building overlap
      if (!(building.location.x > topRight.x
          || building.location.x + building.size.x - 1 < btmLeft.x
          || building.location.y > topRight.y
          || building.location.y + building.size.y - 1 < btmLeft.y)
      ) {
        if (building == previewBuilding) {
          continue;
        }
        buildable = false;
        break;
      }
    }

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

  /**
   * Draws each building from the building list onto the map.

   * @param batch - the SpriteBatch in which to draw
   */
  public void render(SpriteBatch batch) {
    for (Building building : buildings) {
      drawBuilding(building, batch);
    }
  }

  public int placeBuilding(Building building) {
    // Insert the building into the correct place in the arrayList to ensure it
    // gets rendered in top-down order
    int buildingHeight = building.location.y - building.location.x - building.size.x + 1;
    int i = 0;
    while (i < buildings.size()) {
      Building other = buildings.get(i);
      if (other.location.y - other.location.x - other.size.x + 1 > buildingHeight) {
        i++;
      } else {
        break;
      }
    }
    buildings.add(i, building);
    return i;
  }

  public void setPreviewBuilding(Building previewBuilding) {
    if (this.previewBuilding != null) {
      buildings.remove(this.previewBuilding);
    }
    this.previewBuilding = previewBuilding;
    if (previewBuilding != null) {
      placeBuilding(previewBuilding);
    }
  }
  
  /**
   * Draw the building texture at the position of the mouse cursor
   * when building mode is enabled.

   * @param building - The building to draw under the mouse cursor
   * @param batch - the SpriteBatch to draw into
   * @param cursorPos - The grid position of the cursor
   */
  public void drawBuilding(Building building, SpriteBatch batch) {
    Vector3 btmLeftPos = new Vector3(
        (float) building.location.x,
        (float) building.location.y,
        0f
    ).mul(isoTransform);
    Vector3 btmRightPos = new Vector3(
        (float) building.location.x + building.size.x,
        (float) building.location.y,
        0f
    ).mul(isoTransform);
    batch.draw(
        building.texture, 
        btmLeftPos.x, btmRightPos.y, 
        building.imageSize, building.imageSize
    );
  }
}
