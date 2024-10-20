package io.github.unisim.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ShapeActor extends Actor {
  private ShapeRenderer renderer = new ShapeRenderer();
  private Color barColor;

  public ShapeActor(Color barColor) {
    super();
    this.barColor = barColor;
    Gdx.app.log("#INFO", "Drawing test Actor");
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    batch.end();
    renderer.setProjectionMatrix(batch.getProjectionMatrix());
    renderer.setTransformMatrix(batch.getTransformMatrix());
    renderer.translate(getX(), getY(), 0);

    renderer.begin(ShapeType.Filled);
    renderer.setColor(barColor);
    renderer.rect(0, 0, getWidth(), getHeight());
    renderer.end();

    batch.begin();
  }

}

