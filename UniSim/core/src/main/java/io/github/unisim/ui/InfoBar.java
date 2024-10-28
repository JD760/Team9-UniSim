package io.github.unisim.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import io.github.unisim.GameState;
import io.github.unisim.Timer;

/**
 * Create a Title bar with basic info.
 */
public class InfoBar {
  private ShapeActor testActor;
  private Table table = new Table();
  private Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
  private Label scoreLabel = new Label("86%", skin);
  private Label titleLabel = new Label("UniSim", skin);
  private Label timerLabel;
  private Texture pauseTexture = new Texture("pause.png");
  private Texture playTexture = new Texture("play.png");
  private Image pauseImage = new Image(pauseTexture);
  private Image playImage = new Image(playTexture);
  private Timer timer;
  private Cell<Label> titleLableCell;
  private Cell<Label> timerLableCell;
  private Cell<Label> scoreLabelCell;
  private Cell<Image> pauseButtonCell;

  /**
   * Create a new infoBar and draws its' components onto the provided stage.

   * @param stage - The stage on which to draw the InfoBar.
   */
  public InfoBar(Stage stage, Timer timer) {
    this.timer = timer;
    timerLabel = new Label(timer.getRemainingTime(), skin);
    table.setDebug(true);
    table.center().center();
    table.add(new Actor()).expandX().align(Align.center);
    scoreLabelCell = table.add(scoreLabel).expandX().align(Align.center);
    titleLableCell = table.add(titleLabel).expandX().align(Align.center);
    timerLableCell = table.add(timerLabel).expandX().align(Align.center);
    pauseButtonCell = table.add(playImage).expandX().align(Align.center);

    // Pause button
    pauseImage.addListener(new ClickListener() {
      @Override
      public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
        GameState.paused = true;
        pauseButtonCell.setActor(playImage);
      }
    });

    // Play button
    playImage.addListener(new ClickListener() {
      @Override
      public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
        GameState.paused = false;
        pauseButtonCell.setActor(pauseImage);
      }
    });

    testActor = new ShapeActor(new Color(0.635f, 0.345f, 0.125f, 1.0f));
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
  @SuppressWarnings("unchecked")
  public void resize(int width, int height) {
    testActor.setBounds(0, height * 0.95f, width, height * 0.05f);
    table.setBounds(0, height * 0.95f, width, height * 0.05f);

    // we must perform an unchecked type conversion here
    // this is acceptable as we know our table only contains instances of Actors
    for (Cell<Actor> cell : table.getCells()) {
      cell.height(height * 0.05f).width(height * 0.05f);
    }
    titleLabel.setFontScale(height * 0.003f);
    //titleLableCell.pad(height * 0.2f);
    scoreLabel.setFontScale(height * 0.002f);
    timerLabel.setFontScale(height * 0.002f);
    //timerLableCell.padLeft(height * 0.2f);
    //scoreLabelCell.pad(height * 0.2f);
  }
}
