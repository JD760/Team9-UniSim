package io.github.unisim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Game {
  private World world;
  private Timer timer;
  private SpriteBatch uiBatch;
  private BitmapFont font;

  public Game() {
    timer = new Timer(10_000);
    uiBatch = new SpriteBatch();
    font = new BitmapFont();
    world = new World();
  }

  public void dispose() {
    uiBatch.dispose();
    world.dispose();
  }

  public void render() {
    timer.tick(Gdx.graphics.getDeltaTime() * 1000);

    world.render();

    uiBatch.begin();
    if (timer.isRunning()) {
      font.draw(uiBatch, timer.getRemainingTime(), 10, 20);
    } else {
      font.draw(uiBatch, "Game Over!", 10, 20);
    }
    uiBatch.end();
  }
}