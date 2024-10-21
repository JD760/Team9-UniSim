package io.github.unisim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Game screen where the main game is rendered and controlled.
 * Supports pausing the game with a pause menu.
 */
public class GameScreen implements Screen {
  private Stage stage;
  private Skin skin;
  private TextButton resumeButton;
  private TextButton mainMenuButton;

  private boolean isPaused = false;

  /**
   * Constructor for the GameScreen.

   * @param main Reference to the Main game class to manage screen switching and volume.
   */
  public GameScreen(Main main) {
    // Initialize UI for pause menu
    stage = new Stage();
    Gdx.input.setInputProcessor(stage);
    skin = main.getDefaultSkin();

    // Resume button
    resumeButton = new TextButton("Resume", skin);
    resumeButton.setPosition(150, 200);
    resumeButton.setSize(200, 60);
    resumeButton.addListener(new ClickListener() {
      @Override
      public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
        // Unpause the game
        isPaused = false;
        Gdx.input.setInputProcessor(null); // Restore input to the game
      }
    });

    // Main Menu button
    mainMenuButton = new TextButton("Main Menu", skin);
    mainMenuButton.setPosition(150, 120);
    mainMenuButton.setSize(200, 60);
    mainMenuButton.addListener(new ClickListener() {
      @Override
      public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
        // Go back to the main menu
        main.setScreen(new StartMenuScreen(main));
      }
    });

    // Add buttons to the stage
    stage.addActor(resumeButton);
    stage.addActor(mainMenuButton);
  }

  @Override
  public void show() {
  }

  @Override
  public void render(float delta) {
    if (isPaused) {
      // Clear the screen for the pause menu
      Gdx.gl.glClearColor(0.55f, 0.55f, 0.55f, 1);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
      return; // Don't update game while paused
    } else {
      stage.act(delta);
      stage.draw();
    }

    // Check if ESC is pressed to toggle pause
    if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
      isPaused = true;
      Gdx.input.setInputProcessor(stage); // Capture input for the pause menu
    }
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
