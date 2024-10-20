package io.github.unisim.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.unisim.Timer;

public class InfoBar implements Disposable {
  public Stage stage;
  private Timer timer;
  private Viewport viewport;
  private Label timerLabel;
  private SpriteBatch batch;

  public InfoBar(SpriteBatch batch, Timer timer) {
    this.batch = batch;
    this.timer = timer;
    viewport = new FitViewport(300, 200);
    stage = new Stage(viewport, batch);

    timerLabel = new Label(String.valueOf(timer.getRemainingTime()), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

    Table table = new Table();
    table.top();
    table.setFillParent(true);
    table.add(timerLabel).expandX();

    stage.addActor(table);
  }

  public void update() {
    if (timer.isRunning()) {
      timerLabel.setText(timer.getRemainingTime());
    } else {
      timerLabel.setText("Game Over!");
    }
    
  }

  public void dispose() {
    stage.dispose();
  }
}
