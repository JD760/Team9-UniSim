package io.github.unisim;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class World {
  private TiledMap map;
  private float unitScale = 1 / 32f;
  private IsometricTiledMapRenderer renderer;
  private OrthographicCamera camera;

  public World() {
    camera = new OrthographicCamera(300, 200);
    map = new TmxMapLoader().load("map_2.tmx");
    renderer = new IsometricTiledMapRenderer(map, unitScale);
  }

  public void render() {
    ScreenUtils.clear(0.55f, 0.55f, 0.55f, 1f);

    //renderer.setView(projection, 0, 0, 300, 200);
    camera.update();
    renderer.setView(camera);
    renderer.render();
  }

  public void dispose() {
    map.dispose();
  }
}
