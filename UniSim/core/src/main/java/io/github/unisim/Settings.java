package io.github.unisim;

/**
 * Contains global settings for the game such as volume.
 */
public class Settings {
  private float volume = 1.0f;

  public float getVolume() {
    return volume;
  }

  public void setVolume(float volume) {
    this.volume = volume;
  }
}
