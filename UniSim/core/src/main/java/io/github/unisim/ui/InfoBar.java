package io.github.unisim.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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
import io.github.unisim.building.BuildingType;
import io.github.unisim.world.World;

/**
 * Create a Title bar with basic info.
 */
public class InfoBar {
  private ShapeActor bar;
  private Table infoTable = new Table();
  private Table titleTable = new Table();
  private Table buildingCountersTable = new Table();
  private Label[] buildingCounterLabels = new Label[4];
  private Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
  private Label scoreLabel = new Label("86%", skin);
  private Label titleLabel = new Label("UniSim", skin);
  private Label timerLabel;
  private Texture pauseTexture = new Texture("pause.png");
  private Texture playTexture = new Texture("play.png");
  private Image pauseImage = new Image(pauseTexture);
  private Image playImage = new Image(playTexture);
  private Timer timer;
  private Cell<Label> titleLabelCell;
  private Cell<Label> timerLabelCell;
  private Cell<Label> scoreLabelCell;
  private Cell<Image> pauseButtonCell;
  private Cell<Table> buildingCountersTableCell;
  private Cell[] buildingCounterCells;
  private World world;
  /**
   * Create a new infoBar and draws its' components onto the provided stage.

   * @param stage - The stage on which to draw the InfoBar.
   */
  public InfoBar(Stage stage, Timer timer, World world) {
    this.timer = timer;
    this.world = world;
    buildingCounterCells = new Cell[4];

    // Building counter table
    for (int i = 0; i < 4; i++) {
      buildingCounterLabels[i] = new Label("", skin);
    }
    buildingCounterCells[0] = buildingCountersTable.add(buildingCounterLabels[0]);
    buildingCounterCells[1] = buildingCountersTable.add(buildingCounterLabels[1]);
    buildingCountersTable.row();
    buildingCounterCells[2] = buildingCountersTable.add(buildingCounterLabels[2]);
    buildingCounterCells[3] = buildingCountersTable.add(buildingCounterLabels[3]);

    // Info Table
    timerLabel = new Label(timer.getRemainingTime(), skin);
    infoTable.center().center();
    pauseButtonCell = infoTable.add(playImage).align(Align.center);
    timerLabelCell = infoTable.add(timerLabel).align(Align.center);
    scoreLabelCell = infoTable.add(scoreLabel).align(Align.center);
    buildingCountersTableCell = infoTable.add(buildingCountersTable).expandX().align(Align.right);

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

    // Title table
    titleLabelCell = titleTable.add(titleLabel).expandX().align(Align.center);

    bar = new ShapeActor(new Color(0.635f, 0.345f, 0.125f, 1.0f));
    stage.addActor(bar);
    stage.addActor(infoTable);
    stage.addActor(titleTable);
  }

  /**
   * Called when the UI needs to be updated, usually on every frame.
   */
  public void update() {
    timerLabel.setText(timer.getRemainingTime());
    buildingCounterLabels[0].setText("Recreation: "
        + Integer.toString(world.getBuildingCount(BuildingType.RECREATION)));
    buildingCounterLabels[1].setText("Learning: "
        + Integer.toString(world.getBuildingCount(BuildingType.LEARNING)));
    buildingCounterLabels[2].setText("Eating: "
        + Integer.toString(world.getBuildingCount(BuildingType.EATING)));
    buildingCounterLabels[3].setText("Sleeping: "
        + Integer.toString(world.getBuildingCount(BuildingType.SLEEPING)));
    pauseButtonCell.setActor(GameState.paused ? playImage : pauseImage);
  }

  /**
   * Update the bounds of the background & table actors to fit the new size of the screen.

   * @param width - The new width of the screen in pixels.
   * @param height - The enw height of the screen in pixels.
   */
  public void resize(int width, int height) {
    bar.setBounds(0, height * 0.95f, width, height * 0.05f);
    infoTable.setBounds(0, height * 0.95f, width, height * 0.05f);
    titleTable.setBounds(0, height * 0.95f, width, height * 0.05f);

    float counterTableWidth = height * 0.27f;
    buildingCountersTableCell.width(counterTableWidth).height(height * 0.05f);
    for (int i = 0; i < 4; i++) {
      buildingCounterLabels[i].setFontScale(height * 0.0015f);
      buildingCounterCells[i].width(counterTableWidth * 0.5f).height(height * 0.025f);
    }

    timerLabel.setFontScale(height * 0.002f);
    timerLabelCell.width(height * 0.08f).height(height * 0.05f);
    timerLabelCell.padLeft(height * 0.005f);
    scoreLabel.setFontScale(height * 0.002f);
    scoreLabelCell.width(height * 0.04f).height(height * 0.05f);
    scoreLabelCell.padLeft(Math.min(width, height * 2) * 0.14f);
    pauseButtonCell.width(height * 0.03f).height(height * 0.03f)
    .padLeft(height * 0.01f).padRight(height * 0.01f);

    titleLabel.setFontScale(height * 0.003f);
    titleLabelCell.width(height * 0.11f).height(height * 0.05f);
  }

  public void reset() {
    pauseButtonCell.setActor(playImage);
  }
}
