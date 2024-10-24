package io.github.unisim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * A class that holds all the gameplay elements of the game UniSim
 * It has the ablity to render the game and update the state of the game
 */
public class World {
  private OrthographicCamera camera = new OrthographicCamera();
  private Viewport viewport = new ScreenViewport(camera);
  private TiledMap map = new TmxMapLoader().load("map.tmx");
  private float unitScale = 1f / 16f;
  private IsometricTiledMapRenderer renderer = new IsometricTiledMapRenderer(map, unitScale);
  private Vector2 camPosition = new Vector2(500f, 0f);
  private Vector2 panVelocity = new Vector2(0f, 0f);
  private float zoomVelocity = 0f;
  final private float timeStepSize = 0.001f;
  private float panDT = 0f;
  private float zoomDT = 0f;
  private SpriteBatch batch = new SpriteBatch();
  private Texture texture = new Texture(Gdx.files.internal("tileHighlight.png"));
  private Matrix4 isoTransform;
  private Matrix4 invIsoTransform;

  /**
   * Create a new World
   */
  public World() {
    camera.zoom = 100f / 480;
    initIsometricTransform();
  }

  /**
   * Releases all resources of this object
   * Should be called when the World object is no longer needed
   */
  public void dispose() {
    map.dispose();
  }

  /**
   * Steps the world forward by delta time and renders the world
   */
  public void render() {
    viewport.apply();

    ScreenUtils.clear(0.55f, 0.55f, 0.55f, 1f);

    updatePan();
    updateZoom();

    // Render the map tiles
    // Render the map 0.0624 units lower than the rest of the world to account for
    // the extra pixel
    // at the bottom of each tile. (The pixel is used to prevent tiny gaps between
    // the tiles caused
    // by floating point errors)
    camera.position.set(camPosition.x, camPosition.y + 0.0624f, 0);
    camera.update();
    renderer.setView((OrthographicCamera) viewport.getCamera());
    renderer.render();

    camera.position.set(camPosition.x, camPosition.y, 0);
    camera.update();

    batch.setProjectionMatrix(camera.combined);
    batch.begin();
    Vector2 cursorGridPos = getCursorGridPos();
    Gdx.app.log("#INFO", cursorGridPos.toString());
    Vector3 cursorWorldPos = new Vector3((float) Math.floor(cursorGridPos.x), (float) Math.floor(cursorGridPos.y), 0)
        .mul(isoTransform);
    batch.draw(texture, cursorWorldPos.x, cursorWorldPos.y, 1, 1);
    batch.end();
  }

  /**
   * Resizes the gameplay (usually to fit the size of the window)
   * This is mostly done by resizing the relevant viewports
   * 
   * @param width  - The new width of the window
   * @param height - The new height of the window
   */
  public void resize(int width, int height) {
    if (camera.viewportHeight > 0) {
      camera.zoom *= (float) camera.viewportHeight / height;
    }
    viewport.update(width, height);
  }

  /**
   * Pans the view of the game by translating the camera by a multiple of the
   * vector (x, y)
   * The view will continue to move in the same direction for a short period
   * afterwards
   * 
   * @param x - The distance to pan horizontally
   * @param y - The distance to pan vertically
   */
  public void pan(float x, float y) {
    camPosition.add(x * camera.zoom, y * camera.zoom);
    if (Gdx.input.isButtonPressed(0) || Gdx.input.isButtonPressed(1)
        || Gdx.input.isButtonPressed(2)) {
      panVelocity.set(x * timeStepSize / Gdx.graphics.getDeltaTime(),
          y * timeStepSize / Gdx.graphics.getDeltaTime());
    }
  }

  /**
   * Pans the view of the game by translating the camera by a multiple of the
   * vector (x, y)
   * 
   * @param x - The distance to pan horizontally
   * @param y - The distance to pan vertically
   */
  public void panWithoutInertia(float x, float y) {
    camPosition.add(x * camera.zoom, y * camera.zoom);
  }

  /**
   * Tell the game to zoom in or out by a certain amount
   * 
   * @param amount - The speed to zoom at; negative to zoom in and positive to
   *               zoom out
   */
  public void zoom(float amount) {
    final float zoomAcceleration = 0.0003f;
    zoomVelocity += amount * zoomAcceleration;
  }

  /**
   * Adjusts the zoom of the camera based on the zoomVelocity
   * Also slightly reduces the zoomVelocity to prevent infinite zooming
   * Limits the zoom of the camera to be between minZoom and maxZoom
   */
  private void updateZoom() {
    final float minZoom = 10f / camera.viewportHeight;
    final float maxZoom = 100f / camera.viewportHeight;
    zoomDT += Gdx.graphics.getDeltaTime();
    while (zoomDT > timeStepSize) {
      zoomDT -= timeStepSize;
      zoomVelocity *= 0.987f;
      float scaleFactor = (1f + zoomVelocity * (float) Math.sqrt(camera.zoom) / camera.zoom);
      if (camera.zoom * scaleFactor < minZoom) {
        scaleFactor = minZoom / camera.zoom;
      }
      if (camera.zoom * scaleFactor > maxZoom) {
        scaleFactor = maxZoom / camera.zoom;
      }
      panWithoutInertia(Gdx.input.getX() - camera.viewportWidth / 2, camera.viewportHeight / 2 - Gdx.input.getY());
      camera.zoom *= scaleFactor;
      panWithoutInertia(camera.viewportWidth / 2 - Gdx.input.getX(), Gdx.input.getY() - camera.viewportHeight / 2);
    }
  }

  /**
   * Adjusts the panning of the camera based on the panVelocity
   * Also slightly reduces the panVelocity to prevent infinite panning
   */
  private void updatePan() {
    panDT += Gdx.graphics.getDeltaTime();
    while (panDT > timeStepSize) {
      panDT -= timeStepSize;
      panVelocity.scl(0.987f);
      if (!(Gdx.input.isButtonPressed(0) || Gdx.input.isButtonPressed(1)
          || Gdx.input.isButtonPressed(2))) {
        panWithoutInertia(panVelocity.x, panVelocity.y);
      }
    }
  }

  /**
   * Calculates the position of the cursor in world space.
   * 
   * @return - A Vector2 containing the position of the cursor in world space
   */
  public Vector2 getCursorGridPos() {
    Vector3 unprojected = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)).mul(invIsoTransform);
    unprojected.add(0.45f, -0.45f, 0f);
    return new Vector2((float) Math.floor(unprojected.x), (float) Math.floor(unprojected.y));
  }

  /**
   * Calculates the matrices needed to transform a point into and outof isometric
   * world space
   */
  private void initIsometricTransform() {
    // create the isometric transform
    isoTransform = new Matrix4();
    isoTransform.idt();

    // isoTransform.translate(0, 32, 0);
    isoTransform.scale((float) (Math.sqrt(2.0) / 2.0), (float) (Math.sqrt(2.0) / 4.0), 1.0f);
    isoTransform.rotate(0.0f, 0.0f, 1.0f, -45);

    // ... and the inverse matrix
    invIsoTransform = new Matrix4(isoTransform);
    invIsoTransform.inv();
  }
}
