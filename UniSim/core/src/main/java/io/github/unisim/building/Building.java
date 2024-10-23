package io.github.unisim.building;

import com.badlogic.gdx.graphics.Texture;
import io.github.unisim.Point;

public class Building {
  public Texture texture;
  // we can save memory by storing only the top-left corner and the size of the building.
  // This works as all buildings are rectangular.
  public Point location;
  public Point size;

  public Building(Texture texture, Point location, Point size) {
    this.texture = texture;
    this.location = location;
    this.size = size;
  }
}
