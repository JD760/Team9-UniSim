package io.github.unisim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.unisim.menu.InfoBar;

/**
 * A class that holds all the user interface and gameplay for UniSim.
 */
public class Game {
  private World world = new World();
  private Timer timer = new Timer(10_000);
  private SpriteBatch uiBatch = new SpriteBatch();
  private BitmapFont font = new BitmapFont();
  private Camera uiCamera = new OrthographicCamera();
  private Viewport uiViewport = new ScreenViewport(uiCamera);
  private InputProcessor uiInputProcessor = new UIInputProcessor();
  private InputProcessor worldInputProcessor = new WorldInputProcessor(world);
  private InputMultiplexer inputMultiplexer = new InputMultiplexer();
  private InfoBar infoBar;

  /**
   * Create a new Game.
   */
  public Game() {
    inputMultiplexer.addProcessor(uiInputProcessor);
    inputMultiplexer.addProcessor(worldInputProcessor);
    Gdx.input.setInputProcessor(inputMultiplexer);
    uiCamera.position.set(uiCamera.viewportWidth / 2, uiCamera.viewportHeight / 2, 0);
    infoBar = new InfoBar(uiBatch, timer);

  }

  /**
   * Releases all resources of this object.
   * Should be called when the Game object is no longer needed
   */
  public void dispose() {
    uiBatch.dispose();
    world.dispose();
  }

  /**
   * Renders a frame of the game, including the user interface and the gameplay.
   */
  public void render() {
    world.render();
    timer.tick(Gdx.graphics.getDeltaTime());
    infoBar.update();

    uiViewport.apply();
    uiCamera.update();

    uiBatch.begin();
    uiBatch.setProjectionMatrix(infoBar.stage.getCamera().combined);
    uiBatch.end();

    infoBar.stage.draw();
  }

  /**
   * Resizes the game (usually to fit the size of the window).
   * This is mostly done by resizing the relevant viewports

   * @param width - The new width of the window
   * @param height - The new height of the window
   */
  public void resize(int width, int height) {
    world.resize(width, height);
    uiViewport.update(width, height);
    uiBatch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
  }
}