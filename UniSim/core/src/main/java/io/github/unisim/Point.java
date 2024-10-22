package io.github.unisim;

/**
 * Represents a point in 2D space with integer co-ordinates (x, y).
 */
public class Point {
  @SuppressWarnings("memberName")
  public int x;
  @SuppressWarnings("memberName")
  public int y;

  /**
   * Create a new point with co-ordinates (x, y).

   * @param x - The x co-ordinate of the point
   * @param y - The y co-ordinate of the point
   */
  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Point() {
    this(0, 0);
  }

  @Override
  public String toString() {
    return "(" + x + ", " + y + ")";
  }
}
