package io.github.unisim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * The start menu screen which presents the player with the option to start the
 * game
 * or access the settings menu.
 */
public class StartMenuScreen implements Screen {
  private Stage stage;
  private Skin skin;
  private TextButton playButton;
  private TextButton settingsButton;

  /**
   * Constructor for the StartMenuScreen.

   * @param main Reference to the Main game class to manage screen switching.
   */
  public StartMenuScreen(Main main) {
    stage = new Stage();
    Gdx.input.setInputProcessor(stage);

    skin = main.getDefaultSkin();

    // Play button
    playButton = new TextButton("Play", skin);
    playButton.setPosition(150, 200);
    playButton.setSize(200, 60);
    playButton.addListener(new ClickListener() {
      @Override
      public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
        // Switch to the game screen
        main.setScreen(new GameScreen(main));
      }
    });

    // Settings button
    settingsButton = new TextButton("Settings", skin);
    settingsButton.setPosition(150, 120);
    settingsButton.setSize(200, 60);
    settingsButton.addListener(new ClickListener() {
      @Override
      public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
        // Switch to the settings screen
        main.setScreen(new SettingsScreen(main));
      }
    });

    // Add buttons to the stage
    stage.addActor(playButton);
    stage.addActor(settingsButton);
  }

  @Override
  public void show() {
  }

  @Override
  public void render(float delta) {
    // Clear the screen
    Gdx.gl.glClearColor(0.55f, 0.55f, 0.55f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    // Draw the stage containing buttons
    stage.act(delta);
    stage.draw();
  }

  @Override
  public void resize(int width, int height) {
  }

  @Override
  public void pause() {
  }

  @Override
  public void resume() {
  }

  @Override
  public void hide() {
  }

  @Override
  public void dispose() {
    stage.dispose();
    skin.dispose();
  }
}
