package io.github.unisim.building;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public abstract class Building {
  private Sprite sprite;
  // we can save memory by storing only the top-left corner and the size of the building.
  // This works as all buildings are rectangular.
  private Vector2 location;
  private Vector2 size;
}
