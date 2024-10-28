package io.github.unisim.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

/**
 * Runs before the WorldInputProcessor and handles any input events generated from the UI.
 */
@SuppressWarnings("OuterTypeFilename")
public class UiInputProcessor implements InputProcessor {
  private Stage stage;

  public UiInputProcessor(Stage stage) {
    this.stage = stage;
  }

  /**
   * Called when a key is pressed and handles logic related to keypresses
   * within UI components.

   * @param keycode - The unique identifier for the Key pressed.
   * 
   * @return whether the event has been handled and needs to be further processed.
   */
  public boolean keyDown(int keycode) {
    return false;
  }

  public boolean keyUp(int keycode) {
    return false;
  }

  public boolean keyTyped(char character) {
    return false;
  }

  @Override
  public boolean touchDown(int x, int y, int pointer, int button) {
    return validateMouseClick(x, y);
  }

  public boolean touchUp(int x, int y, int pointer, int button) {
    return false;
  }

  public boolean touchDragged(int x, int y, int pointer) {
    return false;
  }

  public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
    return false;
  }

  public boolean mouseMoved(int x, int y) {
    return false;
  }

  public boolean scrolled(float amountX, float amountY) {
    return false;
  }

  /**
   * Only allow a mouse click to be passed down to the next processor stage
   * if and only if it occurs within a UI component.

   * @param x - The x co-ordinate of the mouse pointer
   * @param y - The y co-ordinate of the mouse pointer
   * @return - true if the click occurred within a UI component, false otherwise
   */
  private boolean validateMouseClick(int x, int y) {
    // determine whether the point (x, y) is within the world or UI components
    boolean inWorld = true;
    Vector2 bottomLeft;
    Vector2 topRight;

    // transform the y co-ordinate into the co-ordinate space we are working in
    // such that (0, 0) becomes the bottom left corner.
    y = Gdx.graphics.getHeight() - y;

    // for each actor, check whether the mouse was clicked within their bounds.
    Array<Actor> actors = stage.getActors();
    for (Actor actor : actors) {
      bottomLeft = new Vector2(actor.getX(), actor.getY());
      topRight = new Vector2(actor.getWidth(), actor.getHeight()).add(bottomLeft);

      // check if the mouse click occurred in a rectangular region
      if (x > bottomLeft.x && x < topRight.x && y > bottomLeft.y && y < topRight.y) {
        inWorld = false;
      }
    }
    // return whether the click occurred within the world or within the UI.
    return !inWorld;
  }
}
