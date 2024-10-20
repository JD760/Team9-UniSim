package io.github.unisim.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Create a Title bar with basic info.
 */
public class InfoBar implements Disposable {
  private Stage stage;
  private TestActor testActor;

  public InfoBar() {
    stage = new Stage(new ScreenViewport());
    Gdx.input.setInputProcessor(stage);
    testActor = new TestActor();

    stage.addActor(testActor);
  }

  public void resize(int width, int height) {
    stage.getViewport().update(width, height, true);
    testActor.setBounds(0, height * 0.95f, width, height * 0.05f);
  }

  public void render() {
    float dt = Gdx.graphics.getDeltaTime();
    stage.act(dt);
    stage.draw();
  }

  public void dispose() {
    stage.dispose();
  }
}
