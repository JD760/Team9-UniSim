package io.github.unisim;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * Game screen where the main game is rendered and controlled.
 * Supports pausing the game with a pause menu.
 */
public class GameScreen implements Screen {
  private Main main;
  private Stage stage;
  private Skin skin;
  private TextButton resumeButton;
  private TextButton mainMenuButton;

  private boolean isPaused = false;

  private Timer timer;
  private SpriteBatch uiBatch;
  private TiledMap map;
  private IsometricTiledMapRenderer renderer;
  private OrthographicCamera camera;
  private BitmapFont font;
  private float unitScale = 1 / 32f;

  /**
   * Constructor for the GameScreen.

   * @param main Reference to the Main game class to manage screen switching and volume.
   */
  public GameScreen(Main main) {
    this.main = main;

    // Initialize game-related objects
    timer = new Timer(10_000);
    uiBatch = new SpriteBatch();
    camera = new OrthographicCamera(300, 200);
    map = new TmxMapLoader().load("map_2.tmx");
    renderer = new IsometricTiledMapRenderer(map, unitScale);
    font = new BitmapFont();

    // Initialize UI for pause menu
    stage = new Stage();
    Gdx.input.setInputProcessor(stage);
    skin = new Skin(Gdx.files.internal("uiskin.json"));

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
      stage.act(delta);
      stage.draw();
      return; // Don't update game while paused
    }

    // Check if ESC is pressed to toggle pause
    if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
      isPaused = true;
      Gdx.input.setInputProcessor(stage); // Capture input for the pause menu
    }

    // Clear the screen and render the game
    ScreenUtils.clear(0.55f, 0.55f, 0.55f, 1f);
    camera.update();
    renderer.setView(camera);
    renderer.render();

    // Render UI (timer and other text)
    uiBatch.begin();
    if (timer.isRunning()) {
      font.draw(uiBatch, timer.getRemainingTime(), 10, 20);
    } else {
      font.draw(uiBatch, "Game Over!", 10, 20);
    }
    timer.tick(Gdx.graphics.getDeltaTime() * 1000);
    uiBatch.end();
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
    uiBatch.dispose();
    map.dispose();
    renderer.dispose();
    font.dispose();
    stage.dispose();
    skin.dispose();
  }
}
