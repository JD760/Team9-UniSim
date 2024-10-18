package io.github.unisim;

import com.badlogic.gdx.ApplicationAdapter;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Main extends ApplicationAdapter {
  private Game game;

  @Override
  public void create() {
    game = new Game();
  }

  @Override
  public void render() {
    game.render();
  }

  @Override
  public void dispose() {
    game.dispose();
  }

  @Override
  public void resize(int width, int height) {
    game.resize(width, height);
  }
}
