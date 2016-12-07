package org.kris.oilsimulation.model;

public class OilAutomatonNextSettings implements NextStateSettings {
  private final ExternalConditions externalConditions;

  public OilAutomatonNextSettings(Vector currentVector, Vector windVector) {
    this.externalConditions = new ExternalConditions(currentVector, windVector);
  }

  public ExternalConditions getExternalConditions() {
    return externalConditions;
  }
}
