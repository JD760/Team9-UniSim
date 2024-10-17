package io.github.unisim;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Main extends ApplicationAdapter {
  private Timer timer;
  private SpriteBatch batch;
  private TiledMap map;
  private float unitScale = 1 / 32f;
  private OrthogonalTiledMapRenderer renderer;
  private OrthographicCamera camera;
  private BitmapFont font;

  @Override
  public void create() {
    timer = new Timer(10_000);
    batch = new SpriteBatch();
    map = new TmxMapLoader().load("UniSim map test.tmx");
    renderer = new OrthogonalTiledMapRenderer(map, unitScale);
    camera = new OrthographicCamera();
    camera.setToOrtho(false, 30, 20);
    font = new BitmapFont();
  }

  @Override
  public void render() {
    ScreenUtils.clear(0.55f, 0.55f, 0.55f, 1f);
    camera.update();
    renderer.setView(camera);
    renderer.render();
    batch.begin();
    if (timer.isRunning()) {
      font.draw(batch, timer.getRemainingTime(), 10, 20);
    } else {
      font.draw(batch, "Game Over!", 10, 20);
    }
    timer.tick(Gdx.graphics.getDeltaTime() * 1000);
    batch.end();
  }

  @Override
  public void dispose() {
    batch.dispose();
    map.dispose();
  }
}
