package io.github.unisim.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.unisim.Point;
import io.github.unisim.building.Building;
import io.github.unisim.building.BuildingManager;
import io.github.unisim.building.BuildingType;

/**
 * A class that holds all the gameplay elements of the game UniSim.
 * It has the ablity to render the game and update the state of the game
 */
public class World {
  private OrthographicCamera camera = new OrthographicCamera();
  private Viewport viewport = new ScreenViewport(camera);
  private TiledMap map = new TmxMapLoader().load("medium_map.tmx");
  private float unitScale = 1f / 16f;
  private IsometricTiledMapRenderer renderer = new IsometricTiledMapRenderer(map, unitScale);
  private Vector2 camPosition = new Vector2(150f, 0f);
  private Vector2 panVelocity = new Vector2(0f, 0f);
  private float zoomVelocity = 0f;
  private final float timeStepSize = 0.001f;
  private float panDt = 0f;
  private float zoomDt = 0f;
  private SpriteBatch tileHighlightBatch = new SpriteBatch();
  private SpriteBatch buildingBatch = new SpriteBatch();
  private Texture tileHighlight = new Texture(Gdx.files.internal("tileHighlight.png"));
  private Texture errTileHighlight = new Texture(Gdx.files.internal("errTileHighlight.png"));
  private Matrix4 isoTransform;
  private Matrix4 invIsoTransform;
  private BuildingManager buildingManager;
  private boolean canBuild;
  private Point mousePosInWorld;
  private Point btmLeft;
  private Point topRight;
  public Building selectedBuilding;
  public boolean selectedBuildingUpdated;

  /**
   * Create a new World.
   */
  public World() {
    camera.zoom = 0.05f;
    initIsometricTransform();
    buildingManager = new BuildingManager(isoTransform);
    selectedBuilding = null;
  }

  /**
   * Releases all resources of this object.
   * Should be called when the World object is no longer needed
   */
  public void dispose() {
    map.dispose();
  }

  /**
   * Steps the world forward by delta time and renders the world.
   */
  public void render() {
    viewport.apply();

    ScreenUtils.clear(0.59f, 0.72f, 1f, 1f);

    updatePan();
    updateZoom();

    // Render the map tiles
    // Render the map 0.0624 units lower than the rest of the world to account for
    // the extra pixel at the bottom of each tile. (The pixel is used to prevent
    // tiny gaps between the tiles caused by floating point errors)
    camera.position.set(camPosition.x, camPosition.y + 0.0624f, 0);
    camera.update();
    renderer.setView((OrthographicCamera) viewport.getCamera());
    renderer.render();

    // Reset the camera position to the correct value for the rest of the world
    camera.position.set(camPosition.x, camPosition.y, 0);
    camera.update();

    // Update the mouse grid pos and the buildable flag
    Point mouseGridPos = getCursorGridPos();
    if (!mouseGridPos.equals(mousePosInWorld) || selectedBuildingUpdated) {
      mousePosInWorld = mouseGridPos;
      btmLeft = mousePosInWorld;
      Point buildingSize = selectedBuilding == null ? new Point(1, 1) : selectedBuilding.size;
      btmLeft.x -= buildingSize.x / 2;
      btmLeft.y -= buildingSize.y / 2;
      topRight = new Point(btmLeft.x + buildingSize.x - 1, btmLeft.y + buildingSize.y - 1);
      canBuild = buildingManager.isBuildable(btmLeft, topRight, getMapTiles());
      if (selectedBuilding != null) {
        selectedBuilding.location = btmLeft;
      }
      buildingManager.setPreviewBuilding(selectedBuilding);
    }

    // Render the tile highlight
    if (selectedBuilding != null) {
      tileHighlightBatch.setProjectionMatrix(camera.combined);
      tileHighlightBatch.begin();
      highlightRegion(btmLeft, topRight, canBuild ? tileHighlight : errTileHighlight);
      tileHighlightBatch.end();
    }

    // render buildings after all map related rendering
    buildingBatch.setProjectionMatrix(camera.combined);
    buildingBatch.begin();
    buildingManager.render(buildingBatch);
    buildingBatch.end();
  }

  /**
   * Resizes the gameplay (usually to fit the size of the window)
   * This is mostly done by resizing the relevant viewports.

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
   * vector (x, y).
   * The view will continue to move in the same direction for a short period
   * afterwards

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
   * vector (x, y).

   * @param x - The distance to pan horizontally
   * @param y - The distance to pan vertically
   */
  public void panWithoutInertia(float x, float y) {
    camPosition.add(x * camera.zoom, y * camera.zoom);
  }

  /**
   * Tell the game to zoom in or out by a certain amount.

   * @param amount - The speed to zoom at; negative to zoom in and positive to
   *               zoom out
   */
  public void zoom(float amount) {
    final float zoomAcceleration = 0.0003f;
    zoomVelocity += amount * zoomAcceleration;
  }

  /**
   * Adjusts the zoom of the camera based on the zoomVelocity.
   * Also slightly reduces the zoomVelocity to prevent infinite zooming
   * Limits the zoom of the camera to be between minZoom and maxZoom
   */
  private void updateZoom() {
    final float minZoom = 10f / camera.viewportHeight;
    final float maxZoom = 100f / camera.viewportHeight;
    zoomDt += Gdx.graphics.getDeltaTime();
    while (zoomDt > timeStepSize) {
      zoomDt -= timeStepSize;
      zoomVelocity *= 0.987f;
      float scaleFactor = (1f + zoomVelocity * (float) Math.sqrt(camera.zoom) / camera.zoom);
      if (camera.zoom * scaleFactor < minZoom) {
        scaleFactor = minZoom / camera.zoom;
      }
      if (camera.zoom * scaleFactor > maxZoom) {
        scaleFactor = maxZoom / camera.zoom;
      }
      panWithoutInertia(
          Gdx.input.getX() - camera.viewportWidth / 2, camera.viewportHeight / 2 - Gdx.input.getY()
      );
      camera.zoom *= scaleFactor;
      panWithoutInertia(
          camera.viewportWidth / 2 - Gdx.input.getX(), Gdx.input.getY() - camera.viewportHeight / 2
      );
    }
  }

  /**
   * Adjusts the panning of the camera based on the panVelocity.
   * Also slightly reduces the panVelocity to prevent infinite panning
   */
  private void updatePan() {
    panDt += Gdx.graphics.getDeltaTime();
    while (panDt > timeStepSize) {
      panDt -= timeStepSize;
      panVelocity.scl(0.987f);
      if (!(Gdx.input.isButtonPressed(0) || Gdx.input.isButtonPressed(1)
          || Gdx.input.isButtonPressed(2))) {
        panWithoutInertia(panVelocity.x, panVelocity.y);
      }
    }
  }

  /**
   * Return the (x, y) co-ordinates of the grid cell that the mouse pointer
   * is currently within.

   * @return - A Vector2 containing the position of the cursor in grid space
   */
  public Point getCursorGridPos() {
    Vector3 unprojected = camera.unproject(
        new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)).mul(invIsoTransform);
    unprojected.add(0.45f, -0.45f, 0f);
    return new Point((int) Math.floor(unprojected.x), (int) Math.floor(unprojected.y));
  }

  /**
   * Highlight a rectangular region about the cursor with a given highlight texture.

   * @param btmLeft - The bottom left edge of the region
   * @param topRight - The top right edge of the region
   * @param highlightTexture - The texture to highlight the squares with
   */
  public void highlightRegion(Point btmLeft, Point topRight, Texture highlightTexture) {
    Point tilePos = new Point();
    for (tilePos.x = btmLeft.x; tilePos.x <= topRight.x; tilePos.x++) {
      for (tilePos.y = btmLeft.y; tilePos.y <= topRight.y; tilePos.y++) {
        Vector3 worldPos = gridPosToWorldPos(tilePos);
        tileHighlightBatch.draw(highlightTexture, worldPos.x, worldPos.y, 1, 1);
      }
    }
  }

  /**
   * Transforms a point from grid space to world space.

   * @param gridPos - The coordinates of the point in grid space
   * @return - The coordinates of the point in world space
   */
  private Vector3 gridPosToWorldPos(Point gridPos) {
    return new Vector3(
      (float) Math.floor(gridPos.x), (float) Math.floor(gridPos.y), 0f).mul(isoTransform);
  }

  /**
   * Calculates the matrices needed to transform a point into and outof isometric
   * world space.
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

  public TiledMapTileLayer getMapTiles() {
    return (TiledMapTileLayer) map.getLayers().get(0);
  }

  /**
   * Place a building onto the map, called when a tile is clicked and building mode is enabled.

   * @return - True if building could be done successfully, false otherwise.
   */
  public boolean placeBuilding() {
    if (!canBuild) {
      return false;
    }
    buildingManager.placeBuilding(
      new Building(
        selectedBuilding.texture, selectedBuilding.location.getNewPoint(),
        selectedBuilding.size.getNewPoint(), selectedBuilding.flipped, selectedBuilding.type
      )
    );
    selectedBuilding = null;
    return true;
  }

  /**
   * Returns the number of buildings of a certain type that have been placed
   * in the world.
   * 
   * @param type - The type of building
   * @return - An int holding the number of that building that have been placed
   *   in the world
   */
  public int getBuildingCount(BuildingType type) {
    return buildingManager.getBuildingCount(type);
  }
}
