package io.github.unisim.ui;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.unisim.GameState;

public class GameOverMenu {
  private Stage stage;
  private Skin skin;
  private ShapeActor bar = new ShapeActor(new Color(0.882f, 0.612f, 0.408f, 1.0f));
  private Table table;
  private TextButton mainMenuButton;
  private Cell<TextButton> buttonCell;
  private InputMultiplexer inputMultiplexer = new InputMultiplexer();

  /**
   * Create a Building Menu and attach its actors and components to the provided stage.
   * Also handles drawing buildings and their flipped variants

   * @param stage - The stage on which to draw the menu.
   */
  public GameOverMenu() {
    stage = new Stage(new ScreenViewport());
    table = new Table();
    skin = GameState.defaultSkin;

    // Play button
    mainMenuButton = new TextButton("Return to Main Menu", skin);
    mainMenuButton.addListener(new ClickListener() {
      @Override
      public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
        // Switch to the game screen
        GameState.currentScreen = GameState.startScreen;
      }
    });

    // Add UI elements to the stage
    buttonCell = table.add(mainMenuButton).center();
    stage.addActor(bar);
    stage.addActor(table);

    inputMultiplexer.addProcessor(GameState.fullscreenInputProcessor);
    inputMultiplexer.addProcessor(stage);
  }


  public void render(float delta) {
    stage.act(delta);
    stage.draw();
  }

  public void resize(int width, int height) {
    stage.getViewport().update(width, height, true);
    table.setBounds(0, 0, width, height * 0.1f);
    bar.setBounds(0, 0, width, height * 0.1f);
    buttonCell.width(width * 0.3f).height(height * 0.1f);
  }

  public InputProcessor getInputProcessor() {
    return inputMultiplexer;
  }
}
