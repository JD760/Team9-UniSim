package io.github.unisim.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Menu used to place buildings in the world by clicking and dragging them
 * from the list onto the map.
 */
public class BuildingMenu {
  private Stage stage;
  private ShapeActor bar = new ShapeActor(Color.CHARTREUSE);

  /**
   * Create a Building Menu and attach its actors and components to the provided stage.

   * @param stage - The stage on which to draw the menu.
   */
  public BuildingMenu(Stage stage) {
    this.stage = stage;

    stage.addActor(bar);
  }

  public void resize(int width, int height) {
    stage.getViewport().update(width, height, true);
    bar.setBounds(0, 0, width, height * 0.1f);
  }
}
