package io.github.unisim.building;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import io.github.unisim.Point;


/**
 * Represents a building that can be placed on the map.
 */
public class Building {
  public Texture texture;
  // we can save memory by storing only the top-left corner and the size of the building.
  // This works as all buildings are rectangular.
  public Point location;
  public Point size;
  public float textureScale;
  public Vector2 textureOffset;
  public boolean flipped;
  public BuildingType type;
  public String name;

  /**
   * Creates a new building that is added to the building menu and placed on the map.

   * @param texture - The texture to draw on the map/preview at the given location
   * @param location - The grid location to place the bottom-left corner of the building
   * @param size - the dimensions (x, y) of the building
   * @param flipped - whether to render a flipped graphic
   */
  public Building(
      Texture texture, float textureScale, Vector2 textureOffset, Point location, Point size, Boolean flipped,
      BuildingType type, String name
  ) {
    this.texture = texture;
    this.location = location;
    this.size = size;
    this.textureScale = textureScale;
    this.textureOffset = textureOffset;
    this.flipped = flipped;
    this.type = type;
    this.name = name;
  }
}
