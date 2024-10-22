package io.github.unisim.building;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

import io.github.unisim.GameState;
import io.github.unisim.Point;

/**
 * Manage the buildings placed in the world and methods common to all buildings.
 */
public class BuildingManager {
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
}
