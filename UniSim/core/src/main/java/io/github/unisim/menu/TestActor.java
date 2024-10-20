package io.github.unisim.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TestActor extends Actor {
  private ShapeRenderer renderer = new ShapeRenderer();

  public TestActor() {
    super();
    Gdx.app.log("#INFO", "Drawing test Actor");
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    batch.end();
    renderer.setProjectionMatrix(batch.getProjectionMatrix());
    renderer.setTransformMatrix(batch.getTransformMatrix());
    renderer.translate(getX(), getY(), 0);

    renderer.begin(ShapeType.Filled);
    renderer.setColor(Color.BLUE);
    renderer.rect(0, 0, getWidth(), getHeight());
    renderer.end();

    batch.begin();
  }

}

