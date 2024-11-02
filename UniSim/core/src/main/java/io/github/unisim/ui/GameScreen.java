package io.github.unisim.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.unisim.GameState;
import io.github.unisim.Timer;
import io.github.unisim.world.UiInputProcessor;
import io.github.unisim.world.World;
import io.github.unisim.world.WorldInputProcessor;

/**
 * Game screen where the main game is rendered and controlled.
 * Supports pausing the game with a pause menu.
 */
public class GameScreen implements Screen {
  private World world = new World();
  private Stage stage = new Stage(new ScreenViewport());
  private InfoBar infoBar;
  private BuildingMenu buildingMenu;
  private Timer timer;
  private InputProcessor uiInputProcessor = new UiInputProcessor(stage);
  private InputProcessor worldInputProcessor = new WorldInputProcessor(world);
  private InputMultiplexer inputMultiplexer = new InputMultiplexer();
  private GameOverMenu gameOverMenu = new GameOverMenu();

  /**
   * Constructor for the GameScreen.
   */
  public GameScreen() {
    timer = new Timer(300_000);
    infoBar = new InfoBar(stage, timer, world);
    buildingMenu = new BuildingMenu(stage, world);

    inputMultiplexer.addProcessor(GameState.fullscreenInputProcessor);
    inputMultiplexer.addProcessor(stage);
    inputMultiplexer.addProcessor(uiInputProcessor);
    inputMultiplexer.addProcessor(worldInputProcessor);
  }

  @Override
  public void show() {
  }

  @Override
  public void render(float delta) {
    world.render();
    float dt = Gdx.graphics.getDeltaTime();
    if (!GameState.paused && !GameState.gameOver) {
      if (!timer.tick(dt * 1000)) {
        GameState.gameOver = true;
        Gdx.input.setInputProcessor(gameOverMenu.getInputProcessor());
      }
    }
    infoBar.update();
    stage.act(dt);
    stage.draw();
    if (GameState.gameOver) {
      world.zoom(0.1f);
      world.pan((150 - world.getCameraPos().x) / 10, -world.getCameraPos().y / 10);
      gameOverMenu.render(delta);
    }
  }

  @Override
  public void resize(int width, int height) {
    world.resize(width, height);
    stage.getViewport().update(width, height, true);
    infoBar.resize(width, height);
    buildingMenu.resize(width, height);
    gameOverMenu.resize(width, height);
  }

  @Override
  public void pause() {
  }

  @Override
  public void resume() {
    Gdx.input.setInputProcessor(inputMultiplexer);

    if (GameState.gameOver) {
      GameState.gameOver = false;
      GameState.paused = true;
      timer.reset();
      world.reset();
      infoBar.reset();
    }
  }
 
  @Override
  public void hide() {
  }

  @Override
  public void dispose() {
    world.dispose();
    stage.dispose();
  }
}
