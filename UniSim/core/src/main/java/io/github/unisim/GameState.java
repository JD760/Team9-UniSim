package io.github.unisim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameState {
    public static Skin defaultSkin = new Skin(Gdx.files.internal("ui/uiskin.json"));
    public static Settings settings = new Settings();
    public static Screen gameScreen = new GameScreen();
    public static Screen startScreen = new StartMenuScreen();
    public static Screen settingScreen = new SettingsScreen();
    public static Screen currentScreen;
}
