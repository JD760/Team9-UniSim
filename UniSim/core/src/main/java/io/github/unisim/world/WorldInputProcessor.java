package io.github.unisim.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import io.github.unisim.GameState;
import io.github.unisim.Point;
import io.github.unisim.building.BuildingManager;

/**
 * Handles input events related to the world, after they have passed through the UiInputProcessor.
 */
public class WorldInputProcessor implements InputProcessor {
  private World world;
  private int[] cursorPos = new int[2];
  private boolean dragging = false;

  public WorldInputProcessor(World world) {
    this.world = world;
  }


  @Override
  public boolean keyDown(int keycode) {
    switch (keycode) {
      case Keys.SPACE:
        GameState.paused = !GameState.paused;
        break;
      default:
        break;
    }
    return false;
  }


  public boolean keyUp(int keycode) {
    return false;
  }


  public boolean keyTyped(char character) {
    return false;
  }

  /**
   * Detect when the mouse has been clicked and record the cursor postion.
   * Sets the dragging flag, if the mouse has been clicked in a valid
   * start location.
   */
  public boolean touchDown(int x, int y, int pointer, int button) {
    dragging = true;
    cursorPos[0] = x;
    cursorPos[1] = y;

    if (GameState.buildingMode) {
      world.placeBuilding();
    }
    return true;
  }

  /**
   * When the mouse is released, stop tracking the dragging events.
   */
  public boolean touchUp(int x, int y, int pointer, int button) {
    dragging = false;
    return false;
  }

  /**
   * If the mouse has been clicked in a valid location, allow the map to be panned 
   * by clicking and holding the mouse button.
   */
  public boolean touchDragged(int x, int y, int pointer) {
    if (dragging) {
      world.pan(cursorPos[0] - x, y - cursorPos[1]);
      cursorPos[0] = x;
      cursorPos[1] = y;
      return true;
    }
    return false;
  }

  public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
    return false;
  }

  public boolean mouseMoved(int x, int y) {
    return false;
  }

  /**
   * Zoom in on the map when the mouse wheel is scrolled.
   */
  public boolean scrolled(float amountX, float amountY) {
    world.zoom(amountY);
    return true;
  }
}