package org.kris.oilsimulation.model;

public class ExternalConditions {
  private static final ExternalConditions ZERO_CONDITIONS =
      new ExternalConditions(Vector.zeroVector(), Vector.zeroVector());

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

  public static ExternalConditions getNoInfluenceConditions() {
    return ZERO_CONDITIONS;
  }
}
