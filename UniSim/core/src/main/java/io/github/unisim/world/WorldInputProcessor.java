package io.github.unisim.world;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import io.github.unisim.GameState;

/**
 * Handles input events related to the world, after they have passed through the UiInputProcessor.
 */
public class WorldInputProcessor implements InputProcessor {
  private World world;
  private int[] cursorPos = new int[2];
  private int[] cursorPosWhenClicked = new int[2];
  private boolean clickedOnWorld = false;
  private boolean draggedSinceClick = true;

  public WorldInputProcessor(World world) {
    this.world = world;
  }


  @Override
  public boolean keyDown(int keycode) {
    switch (keycode) {
      case Keys.SPACE:
        GameState.paused = !GameState.paused;
        break;
      case Keys.R:
        // Flip the selected building
        if (world.selectedBuilding != null) {
          world.selectedBuilding.flipped = !world.selectedBuilding.flipped;
          int temp = world.selectedBuilding.size.x;
          world.selectedBuilding.size.x = world.selectedBuilding.size.y;
          world.selectedBuilding.size.y = temp;
          world.selectedBuildingUpdated = true;
        }
        break;
      default:
        break;
    }
    return false;
  }


  @Override
  public boolean keyUp(int keycode) {
    return false;
  }

  @Override
  public boolean keyTyped(char character) {
    return false;
  }

  /**
   * Detect when the mouse has been clicked and record the cursor postion.
   * Sets the clickedOnWorld flag, if the mouse has been clicked in a valid
   * start location.
   */
  @Override
  public boolean touchDown(int x, int y, int pointer, int button) {
    clickedOnWorld = true;
    draggedSinceClick = false;
    cursorPos[0] = cursorPosWhenClicked[0] = x;
    cursorPos[1] = cursorPosWhenClicked[1] = y;
    return true;
  }

  /**
   * When the mouse is released, stop tracking the dragging events.
   */
  @Override
  public boolean touchUp(int x, int y, int pointer, int button) {
    clickedOnWorld = false;
    if (!draggedSinceClick && world.selectedBuilding != null) {
      if (world.placeBuilding()) {
        draggedSinceClick = true;
      }
    }
    return false;
  }

  /**
   * If the mouse has been clicked in a valid location, allow the map to be panned
   * by clicking and holding the mouse button.
   */
  @Override
  public boolean touchDragged(int x, int y, int pointer) {
    if (clickedOnWorld) {
      if (Math.max(Math.abs(cursorPos[0] - cursorPosWhenClicked[0]),
          Math.abs(cursorPos[1] - cursorPosWhenClicked[1])) > 5) {
        draggedSinceClick = true;
      }
      world.pan(cursorPos[0] - x, y - cursorPos[1]);
      cursorPos[0] = x;
      cursorPos[1] = y;
      return true;
    }
    return false;
  }

  @Override
  public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
    return false;
  }

  @Override
  public boolean mouseMoved(int x, int y) {
    return false;
  }

  /**
   * Zoom in on the map when the mouse wheel is scrolled.
   */
  @Override
  public boolean scrolled(float amountX, float amountY) {
    world.zoom(amountY);
    return true;
  }
}
