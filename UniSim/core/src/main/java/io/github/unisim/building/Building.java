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
   * Create a new building object that may be placed in the world or displayed on the building menu.

  * @param texture - The texture to draw over the building.
  * @param textureScale - The scale to display the texture at relative to the source image
  * @param textureOffset - The texture offset in grid tiles
  * @param location - The (x, y) co-ordinates of the building in the grid
  * @param size - The size (width, height) of the building in grid tiles
  * @param flipped - Whether to render a flipped building graphic
  * @param type - The type of building to create
  * @param name - The name of the building, displayed when selected
  */
  public Building(
      Texture texture, float textureScale, Vector2 textureOffset, Point location,
      Point size, Boolean flipped, BuildingType type, String name
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
