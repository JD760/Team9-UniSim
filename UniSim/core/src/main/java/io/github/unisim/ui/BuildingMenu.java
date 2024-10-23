package io.github.unisim.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.unisim.GameState;

/**
 * Menu used to place buildings in the world by clicking and dragging them
 * from the list onto the map.
 */
public class BuildingMenu {
  private ShapeActor bar = new ShapeActor(Color.GRAY);
  private Table table;
  private Image image;


  /**
   * Create a Building Menu and attach its actors and components to the provided stage.

   * @param stage - The stage on which to draw the menu.
   */
  public BuildingMenu(Stage stage) {
    table = new Table();
    table.setDebug(true);
    image = new Image(new Texture(Gdx.files.internal("building_test.png")));
    image.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent e, float x, float y) {
        Gdx.app.log("#INFO", "Building Clicked!");
        GameState.buildingMode = !GameState.buildingMode;
      }
    });
    table.add(image).width(64).height(64);
    stage.addActor(bar);
    stage.addActor(table);
  }

  public void resize(int width, int height) {
    table.setBounds(0, 0, width, height * 0.1f);
    bar.setBounds(0, 0, width, height * 0.1f);
  }
}
