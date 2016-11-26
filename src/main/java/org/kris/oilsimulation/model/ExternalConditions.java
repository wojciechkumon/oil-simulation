package org.kris.oilsimulation.model;

public class ExternalConditions {
  private final Vector current;
  private final Vector wind;

  public ExternalConditions(Vector current, Vector wind) {
    this.current = current;
    this.wind = wind;
  }

  public Vector getCurrent() {
    return current;
  }

  public Vector getWind() {
    return wind;
  }

}
