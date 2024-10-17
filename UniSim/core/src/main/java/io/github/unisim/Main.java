package io.github.unisim;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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
}
