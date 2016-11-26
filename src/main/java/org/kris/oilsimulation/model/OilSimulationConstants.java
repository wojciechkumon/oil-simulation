package org.kris.oilsimulation.model;

public class OilSimulationConstants {
  private final double density; // kg/m^3
  private final double surfaceTension; // dyne/s

  public OilSimulationConstants(double density, double surfaceTension) {
    this.density = density;
    this.surfaceTension = surfaceTension;
  }

  public double getDensity() {
    return density;
  }

  public double getSurfaceTension() {
    return surfaceTension;
  }
}
