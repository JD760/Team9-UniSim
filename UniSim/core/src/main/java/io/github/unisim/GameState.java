package io.github.unisim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import io.github.unisim.ui.GameScreen;
import io.github.unisim.ui.SettingsScreen;
import io.github.unisim.ui.StartMenuScreen;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Contains a collection of settings and references that should be available globally.
 */
public class GameState {
  public static Skin defaultSkin = new Skin(Gdx.files.internal("ui/uiskin.json"));
  public static Settings settings = new Settings();
  public static InputProcessor fullscreenInputProcessor = new FullscreenInputProcessor();
  public static Screen gameScreen = new GameScreen();
  public static Screen startScreen = new StartMenuScreen();
  public static Screen settingScreen = new SettingsScreen();
  public static Screen currentScreen;
  // Create an unmodifiable set containing the IDs of all buildable tiles
  // we use a set to make searching more efficient
  public static Set<Integer> buildableTiles = Stream.of(
      8, 9, 10, 11, 12, 13, 14).collect(Collectors.toUnmodifiableSet()
  );
  public static boolean paused = true;
  public static Color UIPrimaryColour = new Color(0.635f, 0.345f, 0.125f, 1.0f);
  public static Color UISecondaryColour = new Color(0.882f, 0.612f, 0.408f, 1.0f);
}
