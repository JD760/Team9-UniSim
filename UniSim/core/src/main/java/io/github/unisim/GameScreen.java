package io.github.unisim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.unisim.menu.BuildingMenu;
import io.github.unisim.menu.InfoBar;

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

  /**
   * Constructor for the GameScreen.

   * @param main Reference to the Main game class to manage screen switching and volume.
   */
  public GameScreen(Main main) {
    timer = new Timer(300_000);
    infoBar = new InfoBar(stage, timer);
    buildingMenu = new BuildingMenu(stage);
    inputMultiplexer.addProcessor(uiInputProcessor);
    inputMultiplexer.addProcessor(worldInputProcessor);
    Gdx.input.setInputProcessor(inputMultiplexer);
  }

  @Override
  public void show() {
  }

  @Override
  public void render(float delta) {
    world.render();
    float dt = Gdx.graphics.getDeltaTime();
    timer.tick(dt * 1000);
    infoBar.update();
    stage.act(dt);
    stage.draw();
  }

  @Override
  public void resize(int width, int height) {
    world.resize(width, height);
    infoBar.resize(width, height);
    buildingMenu.resize(width, height);
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
    world.dispose();
    stage.dispose();
  }
}
