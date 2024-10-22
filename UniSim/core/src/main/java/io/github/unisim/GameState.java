package io.github.unisim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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
  public static final Matrix4 isoTransform = new Matrix4().idt()
      .scale((float) (Math.sqrt(2.0) / 2.0), (float) (Math.sqrt(2.0) / 4.0), 1.0f)
      .rotate(0.0f, 0.0f, 1.0f, -45);
  public static final Matrix4 invIsoTransform = isoTransform.inv();
}
