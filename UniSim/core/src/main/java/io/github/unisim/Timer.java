package io.github.unisim;

/**
 * A simple timer utility that can be updated on each render call.
 */
public class Timer {
  private float remainingTime;
  private float initialTime;
  private boolean hasFinished;

  /**
   * Create a new timer set to count down from an initial number of milliseconds.

   * @param initialTime - The number of milliseconds before the timer ends
   */
  public Timer(float initialTime) {
    this.initialTime = initialTime;
    remainingTime = initialTime;
    hasFinished = false;
  }

  /**
   * Removes a provided timestep from the counter and returns whether the timer has stopped.

   * @param deltaTime - the time in milliseconds to remove from the counter
   * @return - true if the timer is running and the time has been decremented, false otherwise.
   */
  public boolean tick(float deltaTime) {
    remainingTime -= deltaTime;
    if (remainingTime > 0) {
      return true;
    } else {
      hasFinished = true;
      return false;
    }
  }

  /**
   * Reset the timer to its' initial time value. 
   */
  public void reset() {
    remainingTime = initialTime;
    hasFinished = false;
  }

  /**
   * Return the remaining time in a String representation.

   * @return - remaining time in the form MM:SS
   */
  public String getRemainingTime() {
    // get the number of minutes and seconds from the remaining time in milliseconds.
    int remainingMinutes = (int)((remainingTime + 1000) / 60_000);
    int remainingSeconds = (int)Math.ceil(remainingTime / 1000 - 60 * remainingMinutes);

    return formatNum(remainingMinutes) + ":" + formatNum(remainingSeconds);
  }

  /**
   * Format a number of minutes or seconds to always have a length of two digits.
   * This is done by prepending a zero if the number has only one digit.

   * @param num - the number to convert to a formatted string
   * @return - a formatted string with length at least two.
   */
  private String formatNum(int num) {
    if (num < 10) {
      return "0" + num;
    }
    return String.valueOf(num);
  }

  /**
   * Return whether the timer is still running or has reached zero.

   * @return - true if the timer is running, false if the remaining time has reached zero
   */
  public boolean isRunning() {
    return !hasFinished;
  }
}
