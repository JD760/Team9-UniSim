package io.github.unisim.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;

/**
 * Create a Title bar with basic info.
 */
public class InfoBar {
  private Stage stage;
  private ShapeActor testActor;

  public InfoBar(Stage stage) {
    this.stage = stage;

    //Gdx.input.setInputProcessor(stage);
    testActor = new ShapeActor(Color.SLATE);
    stage.addActor(testActor);
  }

  public void resize(int width, int height) {
    stage.getViewport().update(width, height, true);
    testActor.setBounds(0, height * 0.95f, width, height * 0.05f);
  }
}
