package io.github.unisim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.unisim.menu.BuildingMenu;
import io.github.unisim.menu.InfoBar;

/**
 * A class that holds all the user interface and gameplay for UniSim.
 */
public class Game {
  private World world = new World();
  private Stage UiStage;
  private InfoBar infoBar;
  private BuildingMenu buildingMenu;
  private InputProcessor worldInputProcessor = new WorldInputProcessor(world);
  private InputMultiplexer inputMultiplexer = new InputMultiplexer();

  /**
   * Create a new Game.
   */
  public Game() {
    UiStage = new Stage(new ScreenViewport());
    infoBar = new InfoBar(UiStage);
    buildingMenu = new BuildingMenu(UiStage);

    inputMultiplexer.addProcessor(UiStage);
    inputMultiplexer.addProcessor(worldInputProcessor);
    Gdx.input.setInputProcessor(inputMultiplexer);
  }

  /**
   * Releases all resources of this object.
   * Should be called when the Game object is no longer needed
   */
  public void dispose() {
    world.dispose();
    UiStage.dispose();
  }

  /**
   * Renders a frame of the game, including the user interface and the gameplay.
   */
  public void render() {
    world.render();

    float dt = Gdx.graphics.getDeltaTime();
    UiStage.act(dt);
    UiStage.draw();
  }

  /**
   * Resizes the game (usually to fit the size of the window).
   * This is mostly done by resizing the relevant viewports

   * @param width - The new width of the window
   * @param height - The new height of the window
   */
  public void resize(int width, int height) {
    world.resize(width, height);
    infoBar.resize(width, height);
    buildingMenu.resize(width, height);
  }
}