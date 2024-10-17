package io.github.unisim;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Main extends ApplicationAdapter {
  private Timer timer;
  private SpriteBatch gameBatch;
  private SpriteBatch uiBatch;
  private TiledMap map;
  private Matrix4 projection;
  private float unitScale = 1 / 32f;
  private IsometricTiledMapRenderer renderer;
  private OrthographicCamera camera;
  private BitmapFont font;

  @Override
  public void create() {
    timer = new Timer(10_000);
    gameBatch = new SpriteBatch();
    camera = new OrthographicCamera(300, 200);
    uiBatch = new SpriteBatch();
    map = new TmxMapLoader().load("map_2.tmx");
    renderer = new IsometricTiledMapRenderer(map, unitScale);
    font = new BitmapFont();
  }

  @Override
  public void render() {
    ScreenUtils.clear(0.55f, 0.55f, 0.55f, 1f);

    //renderer.setView(projection, 0, 0, 300, 200);
    camera.update();
    renderer.setView(camera);
    renderer.render();

    uiBatch.begin();
    if (timer.isRunning()) {
      font.draw(uiBatch, timer.getRemainingTime(), 10, 20);
    } else {
      font.draw(uiBatch, "Game Over!", 10, 20);
    }
    timer.tick(Gdx.graphics.getDeltaTime() * 1000);
    uiBatch.end();

  }

  @Override
  public void dispose() {
    gameBatch.dispose();
    uiBatch.dispose();
    map.dispose();
  }
}
