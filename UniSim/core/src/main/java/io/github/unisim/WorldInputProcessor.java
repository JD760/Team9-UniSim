package io.github.unisim;

import com.badlogic.gdx.InputProcessor;

public class WorldInputProcessor implements InputProcessor {
  private World world;
  private int[] cursorPos = new int[2];
  private boolean dragging = false;

  public WorldInputProcessor(World world) {
    this.world = world;
  }

  public boolean keyDown (int keycode) {
    return false;
  }
  
  public boolean keyUp (int keycode) {
    return false;
  }
  
  public boolean keyTyped (char character) {
    return false;
  }
  
  public boolean touchDown (int x, int y, int pointer, int button) {
    dragging = true;
    cursorPos[0] = x;
    cursorPos[1] = y;
    return true;
  }
  
  public boolean touchUp (int x, int y, int pointer, int button) {
    dragging = false;
    return false;
  }
  
  public boolean touchDragged (int x, int y, int pointer) {
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
  
  public boolean mouseMoved (int x, int y) {
    return false;
  }
  
  public boolean scrolled (float amountX, float amountY) {
    world.zoom(amountY);
    return true;
  }
}