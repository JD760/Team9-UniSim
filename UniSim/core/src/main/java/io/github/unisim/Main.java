package io.github.unisim;

import com.badlogic.gdx.Game;
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
 * Main game class responsible for managing different screens (Start Menu, Settings, Game Screen)
 * and controlling global settings like volume and rendering the game.
 */
public class Main extends Game {
  private Timer timer;
  private SpriteBatch gameBatch;
  private SpriteBatch uiBatch;
  private TiledMap map;
  private Matrix4 projection;
  private float unitScale = 1 / 32f;
  private IsometricTiledMapRenderer renderer;
  private OrthographicCamera camera;
  private BitmapFont font;
  private float volume = 1.0f; // Default volume

  /**
   * Initializes game components, loads the map, and sets the initial screen to the start menu.
   */
  @Override
  public void create() {
    timer = new Timer(10_000);
    gameBatch = new SpriteBatch();
    camera = new OrthographicCamera(300, 200);
    uiBatch = new SpriteBatch();
    map = new TmxMapLoader().load("map_2.tmx");
    renderer = new IsometricTiledMapRenderer(map, unitScale);
    font = new BitmapFont();

    // Set the initial screen to the StartMenuScreen
    this.setScreen(new StartMenuScreen(this));
  }

  /**
   * Gets the current volume setting.
   * 
   * @return the volume level (0.0 to 1.0)
   */
  public float getVolume() {
    return volume;
  }

  /**
   * Sets the game volume.
   * 
   * @param volume The volume level to set (0.0 to 1.0).
   */
  public void setVolume(float volume) {
    this.volume = volume;
  }

  /**
   * Renders the game or the active screen.
   */
  @Override
  public void render() {
    super.render(); // Ensures the active screen is rendered
  }

  /**
   * Disposes of resources when the game exits.
   */
  @Override
  public void dispose() {
    gameBatch.dispose();
    uiBatch.dispose();
    map.dispose();
    super.dispose();
  }
}
