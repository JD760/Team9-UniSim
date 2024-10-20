package io.github.unisim.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class BuildingMenu {
  private Stage stage;
  private ShapeActor bar = new ShapeActor(Color.CHARTREUSE);

  public BuildingMenu(Stage stage) {
    this.stage = stage;

    stage.addActor(bar);
  }

  public void resize(int width, int height) {
    stage.getViewport().update(width, height, true);
    bar.setBounds(0, 0, width, height * 0.1f);
  }
}
