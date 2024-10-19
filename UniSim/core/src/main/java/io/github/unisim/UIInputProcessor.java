package io.github.unisim;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Graphics.Monitor;
import com.badlogic.gdx.Input.Keys;

public class UIInputProcessor implements InputProcessor {
  int[] windowSize = new int[2];
  boolean fullscreen = false;

  /**
  * Inform the object about the current window size
   * 
   * @param width - The new width of the window
   * @param height - The new height of the window
   */
  public void resize (int width, int height) {
    if (!fullscreen) {
      windowSize[0] = width;
      windowSize[1] = height;
    }
  }
  
  public boolean keyDown (int keycode) {
    switch (keycode) {
      // Toggle fullscreen
      case Keys.F11:
        Monitor currMonitor = Gdx.graphics.getMonitor();
        DisplayMode displayMode = Gdx.graphics.getDisplayMode(currMonitor);
        if (fullscreen) {
          Gdx.graphics.setWindowedMode(windowSize[0], windowSize[1]);
        } else {
          Gdx.graphics.setFullscreenMode(displayMode);
        }
        fullscreen = !fullscreen;
        return true;

      default:
        return false;
    }
  }
 
  public boolean keyUp (int keycode) {
    return false;
  }
 
  public boolean keyTyped (char character) {
    return false;
  }
 
  public boolean touchDown (int x, int y, int pointer, int button) {
    return false;
  }
 
  public boolean touchUp (int x, int y, int pointer, int button) {
    return false;
  }
 
  public boolean touchDragged (int x, int y, int pointer) {
    return false;
  }

  public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
   return false;
  }
 
  public boolean mouseMoved (int x, int y) {
    return false;
  }
 
  public boolean scrolled (float amountX, float amountY) {
    return false;
  }
}
