package io.github.unisim.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import io.github.unisim.Timer;

/**
 * Create a Title bar with basic info.
 */
public class InfoBar {
  private Stage stage; 
  private ShapeActor testActor;
  private Table table = new Table();
  private Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
  private Label scoreLabel = new Label("86%", skin);
  private Label titleLabel = new Label("UniSim", skin);
  private Label timerLabel;
  private Timer timer;

  /**
   * Create a new infoBar and draws its' components onto the provided stage.

   * @param stage - The stage on which to draw the InfoBar.
   */
  public InfoBar(Stage stage, Timer timer) {
    this.stage = stage;
    this.timer = timer;
    timerLabel = new Label(timer.getRemainingTime(), skin);
    //table.setDebug(true);
    table.center().center();
    titleLabel.setFontScale(2);
    table.add(scoreLabel);
    table.add(titleLabel).align(Align.center).pad(100);
    table.add(timerLabel).align(Align.right);

    testActor = new ShapeActor(Color.SLATE);
    stage.addActor(testActor);
    stage.addActor(table);
  }

  public void update() {
    timerLabel.setText(timer.getRemainingTime());
  }

  /**
   * Update the bounds of the background & table actors to fit the new size of the screen.

   * @param width - The new width of the screen in pixels.
   * @param height - The enw height of the screen in pixels.
   */
  public void resize(int width, int height) {
    stage.getViewport().update(width, height, true);
    testActor.setBounds(0, height * 0.95f, width, height * 0.05f);
    table.setBounds(0, height * 0.95f, width, height * 0.05f);
  }
}
