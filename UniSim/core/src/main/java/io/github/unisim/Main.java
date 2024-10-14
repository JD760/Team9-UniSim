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
  private SpriteBatch batch;
  private TiledMap map;
  private float unitScale = 1 / 32f;
  private OrthogonalTiledMapRenderer renderer;
  private OrthographicCamera camera;
  private BitmapFont font;

  @Override
  public void create() {
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
    font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
    batch.end();
  }

  @Override
  public void dispose() {
    batch.dispose();
    map.dispose();
  }
}
