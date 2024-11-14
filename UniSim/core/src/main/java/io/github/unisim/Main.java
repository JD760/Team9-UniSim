package io.github.unisim;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Main extends Game {
  private Screen currentScreen;

  @Override
  public void create() {
    GameState.currentScreen = GameState.startScreen;
  }

  @Override
  public void render() {
    if (currentScreen != GameState.currentScreen) {
      currentScreen = GameState.currentScreen;
      setScreen(currentScreen);
      currentScreen.resume();
    }
    super.render(); // Ensures the active screen is rendered
  }

  @Override
  public void dispose() {}

  @Override
  public void resize(int width, int height) {
    if (width + height == 0) {
      return;
    }
    ((FullscreenInputProcessor) GameState.fullscreenInputProcessor).resize(width, height);
    GameState.gameScreen.resize(width, height);
    GameState.settingScreen.resize(width, height);
    GameState.startScreen.resize(width, height);
  }
}