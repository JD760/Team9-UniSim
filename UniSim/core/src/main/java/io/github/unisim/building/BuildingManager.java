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
import java.util.HashMap;
import java.util.Map;

/**
 * Manage the buildings placed in the world and methods common to all buildings.
 */
public class BuildingManager {
  // create a list of buildings which will be sorted by a height metric derived from
  // the locations of the corners of the buildings.
  private ArrayList<Building> buildings = new ArrayList<>();
  private Map<BuildingType, Integer> buildingCounts = new HashMap<>();
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

  /**
   * Handle placement of a building into the world by determining
   * the correct draw order and updating the building counters.

   * @param building - A reference to a building object to be placed
   * @return - The location in the buildings array that the building was placed at
   */
  public int placeBuilding(Building building) {
    // Insert the building into the correct place in the arrayList to ensure it
    // gets rendered in top-down order
    // Start by calculating the 'height' values for the left and right corners of the new building
    // where height is the taxi-cab distance from the top of the map
    int buildingHeightLeftSide = building.location.y - building.location.x;
    int buildingHeightRightSide = buildingHeightLeftSide + building.size.y - building.size.x + 1;
    Point leftCorner = building.location;

    // Move up the array, until the pointer is in the correct place for the new building so the
    // array is sorted by height
    int i = 0;
    while (i < buildings.size()) {
      Building other = buildings.get(i);
      int otherHeightLeftSide = other.location.y - other.location.x;
      // Calculate the taxi-cab distance between the new building's left corner and the other
      // building's right corner
      int leftDistance = Math.abs(leftCorner.x - other.location.x - other.size.x + 1)
          + Math.abs(leftCorner.y - other.location.y - other.size.y + 1);
      // If the distance is small, compare the height of the new buildin'g left corner to the
      // height of the other buildings right corner
      if (leftDistance < Math.min(building.size.x + building.size.y, other.size.x + other.size.y)) {
        int otherHeightRightSide = otherHeightLeftSide + other.size.y - other.size.x + 1;
        if (otherHeightRightSide > buildingHeightLeftSide) {
          i++;
          continue;
        } else {
          break;
        }
      }
      // Otherwise, compare the distance of the new building's right corner to the other building's
      // left corner
      if (otherHeightLeftSide > buildingHeightRightSide) {
        i++;
      } else {
        break;
      }
    }
    buildings.add(i, building);
    updateCounters(building);
    return i;
  }

  /**
   * Creates a counter for the building's type if it is the first to be placed,
   * otherwise increments the counter for that type by one.

   * @param building - A reference to the building object that was placed
   */
  private void updateCounters(Building building) {
    if (building == previewBuilding) {
      return;
    }
    if (!buildingCounts.containsKey(building.type)) {
      buildingCounts.put(building.type, 1);
      return;
    }
    buildingCounts.put(building.type, buildingCounts.get(building.type) + 1);
  }

  /**
   * Returns the number of buildings of a certain type that have been placed
   * in the world.
   *
   * @param type - The type of building
   * @return - The number of buildings of that type that have been placed
   */
  public int getBuildingCount(BuildingType type) {
    if (!buildingCounts.containsKey(type)) {
      return 0;
    }
    return buildingCounts.get(type);
  }

  /**
   * Sets the building to render as a 'preview' on the map prior to placement.

   * @param previewBuilding - The building to draw as a preview
   */
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
   */
  public void drawBuilding(Building building, SpriteBatch batch) {
    Vector3 btmLeftPos = new Vector3(
        (float) building.location.x + (
          building.flipped ? building.textureOffset.x : building.textureOffset.x
        ),
        (float) building.location.y + (
          building.flipped ? building.textureOffset.y : building.textureOffset.y
        ),
        0f
    );
    Vector3 btmRightPos = new Vector3(btmLeftPos).add(new Vector3(building.size.x - 1, 0f, 0f));
    btmLeftPos.mul(isoTransform);
    btmRightPos.mul(isoTransform);
    batch.draw(
        building.texture,
        btmLeftPos.x, btmRightPos.y,
        building.texture.getWidth() * building.textureScale,
        building.texture.getHeight() * building.textureScale,
        0, 0, building.texture.getWidth(), building.texture.getHeight(),
        building.flipped, false
    );
  }
}
