package io.github.unisim;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class World {
  private TiledMap map;
  private float unitScale = 1 / 32f;
  private IsometricTiledMapRenderer renderer;
  private OrthographicCamera camera;
  private Viewport viewport;

  public World() {
    camera = new OrthographicCamera();
    viewport = new ScreenViewport(camera);
    camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
    map = new TmxMapLoader().load("map_2.tmx");
    renderer = new IsometricTiledMapRenderer(map, unitScale);
  }

  public void dispose() {
    map.dispose();
  }

  public void render() {
    viewport.apply();
    camera.update();

    ScreenUtils.clear(0.55f, 0.55f, 0.55f, 1f);
    renderer.setView((OrthographicCamera)viewport.getCamera());
    renderer.render();
  }

  public void resize(int width, int height) {
    viewport.update(width,height);
    camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
  }
}
