package org.kris.oilsimulation.controller;

import org.kris.oilsimulation.model.Size;

public class StartUpSettings {
  private final Size size;

  public StartUpSettings(Size size) {
    this.size = size;
  }

  public Size getSize() {
    return size;
  }

  public static StartUpSettings getDefault() {
    return new StartUpSettings(new Size(20, 10));
  }

}
