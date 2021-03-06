package org.kris.oilsimulation.model;

import java.io.Serializable;

public class Size implements Serializable {
  private final int width;
  private final int height;

  public Size(int width, int height) {
    this.width = width;
    this.height = height;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  @Override
  public String toString() {
    return "width=" + width + ",height=" + height;
  }

}
