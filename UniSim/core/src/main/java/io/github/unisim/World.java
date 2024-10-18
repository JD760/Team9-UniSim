package io.github.unisim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class World {
  private float unitScale = 1 / 32f;
  private OrthographicCamera camera = new OrthographicCamera();
  private Viewport viewport = new ScreenViewport(camera);
  private TiledMap map = new TmxMapLoader().load("map_2.tmx");
  private IsometricTiledMapRenderer renderer = new IsometricTiledMapRenderer(map, unitScale);
  private Vector2 camPosition = new Vector2(0f, 0f);
  private float zoomVelocity = 0f;

  public World() {
    camera.position.set(camera.viewportWidth / 2 + camPosition.x,
                        camera.viewportHeight / 2 + camPosition.y, 0);
  }

  public void dispose() {
    map.dispose();
  }

  public void render() {
    viewport.apply();

    ScreenUtils.clear(0.55f, 0.55f, 0.55f, 1f);

    updateZoom();
    camera.position.set(camPosition.x, camPosition.y, 0);
    camera.update();
    renderer.setView((OrthographicCamera)viewport.getCamera());
    renderer.render();

    Gdx.app.log("#INFO", new Vector2(camPosition).toString());
  }

  public void resize(int width, int height) {
    if (camera.viewportHeight > 0) {
      camera.zoom *= (float)camera.viewportHeight / height;
    }
    viewport.update(width, height);
  }

  public void pan(float x, float y) {
    camPosition.add(x * camera.zoom, y * camera.zoom);
  }

  public void zoom(float scrollAmount) {
    final float zoomAcceleration = 0.01f;
    zoomVelocity += scrollAmount * zoomAcceleration;
  }

  private void updateZoom() {
    final float minZoom = 0.02f;
    final float maxZoom = 100f;
    zoomVelocity *= 0.8f;
    float scaleFactor = (1f + zoomVelocity * (float)Math.sqrt(camera.zoom) / camera.zoom);
    if (camera.zoom * scaleFactor < minZoom) {
      scaleFactor = minZoom / camera.zoom;
    }
    if (camera.zoom * scaleFactor > maxZoom) {
      scaleFactor = maxZoom / camera.zoom;
    }
    pan(Gdx.input.getX() - camera.viewportWidth / 2, camera.viewportHeight / 2 - Gdx.input.getY());
    camera.zoom *= scaleFactor;
    pan(camera.viewportWidth / 2 - Gdx.input.getX(), Gdx.input.getY() - camera.viewportHeight / 2);
  }
}
