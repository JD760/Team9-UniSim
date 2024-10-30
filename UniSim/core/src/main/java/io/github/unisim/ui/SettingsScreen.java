package io.github.unisim.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.unisim.GameState;

/**
 * The settings screen that allows the player to adjust the volume.
 */
public class SettingsScreen implements Screen {
  private Stage stage;
  private Table table;
  private Skin skin = GameState.defaultSkin;;
  private Slider volumeSlider;
  private Label volumeLabel;
  private TextButton backButton;
  private InputMultiplexer inputMultiplexer = new InputMultiplexer();

  /**
   * Create a new Settings screen and draw the initial UI layout.
   */
  public SettingsScreen() {
    stage = new Stage();
    table = new Table();

    // Volume label
    volumeLabel = new Label("Volume: ", skin);
    volumeLabel.setColor(new Color(0.9f, 0.9f, 0.9f, 1.0f));

    // Volume slider
    volumeSlider = new Slider(0.0f, 1.0f, 0.1f, false, skin);
    volumeSlider.setValue(GameState.settings.getVolume()); // Set current volume
    volumeSlider.setPosition(150, 150);
    volumeSlider.setSize(200, 50);
    volumeSlider.addListener(event -> {
      // Adjust the game volume based on slider value
      GameState.settings.setVolume(volumeSlider.getValue());
      return false;
    });

    // Back button
    backButton = new TextButton("Back", skin);
    backButton.setPosition(150, 80);
    backButton.setSize(200, 60);
    backButton.addListener(new ClickListener() {
      @Override
      public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
        // Go back to the start menu
        GameState.currentScreen = GameState.startScreen;
      }
    });

    // Add UI elements to stage
    table.setFillParent(true);
    table.center().center();
    table.pad(100, 100, 100, 100);
    table.add(backButton).center().width(250).height(67).padBottom(10);
    table.row();
    table.add(volumeLabel).center();
    table.row();
    table.add(volumeSlider).center().width(250).height(67);
    stage.addActor(table);

    inputMultiplexer.addProcessor(GameState.fullscreenInputProcessor);
    inputMultiplexer.addProcessor(stage);
  }

  @Override
  public void show() {}

  @Override
  public void render(float delta) {
    // Clear the screen
    ScreenUtils.clear(GameState.UISecondaryColour);

    // Draw the stage containing the volume slider and buttons
    stage.act(delta);
    stage.draw();
  }

  @Override
  public void resize(int width, int height) {
    stage.getViewport().update(width, height, true);
  }

  @Override
  public void pause() {}

  @Override
  public void resume() {
    Gdx.input.setInputProcessor(inputMultiplexer);
  }

  @Override
  public void hide() {}

  @Override
  public void dispose() {
    stage.dispose();
    skin.dispose();
  }
}
