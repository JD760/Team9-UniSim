package io.github.unisim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import io.github.unisim.menu.InfoBar;

/**
 * A class that holds all the user interface and gameplay for UniSim.
 */
public class Game {
  private World world = new World();
  private InfoBar bar = new InfoBar();
  private InputProcessor worldInputProcessor = new WorldInputProcessor(world);
  private InputMultiplexer inputMultiplexer = new InputMultiplexer();

  /**
   * Create a new Game.
   */
  public Game() {
    inputMultiplexer.addProcessor(worldInputProcessor);
    Gdx.input.setInputProcessor(inputMultiplexer);
  }

  /**
   * Releases all resources of this object.
   * Should be called when the Game object is no longer needed
   */
  public void dispose() {
    world.dispose();
    bar.dispose();
  }

  /**
   * Renders a frame of the game, including the user interface and the gameplay.
   */
  public void render() {
    world.render();
    bar.render();
  }

  /**
   * Resizes the game (usually to fit the size of the window).
   * This is mostly done by resizing the relevant viewports

   * @param width - The new width of the window
   * @param height - The new height of the window
   */
  public void resize(int width, int height) {
    world.resize(width, height);
    bar.resize(width, height);
  }
}