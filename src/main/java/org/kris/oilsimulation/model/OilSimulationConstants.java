package org.kris.oilsimulation.model;

public class OilSimulationConstants {
  private final double cellSize; // m
  private final double timeStep; // s
  private final double density; // kg/m^3
  private final double surfaceTension; // dyne/s
  private final double propagationFactor; // dyne/s
  private final OilParticle startingParticle;

  public OilSimulationConstants(double cellSize, double timeStep,
                                double density, double surfaceTension,
                                double particleMass /* kg */,
                                double evaporationRate/* kg/(s*m^2) */,
                                double propagationFactor) {
    this.density = density;
    this.surfaceTension = surfaceTension;
    this.cellSize = cellSize;
    this.timeStep = timeStep;
    this.propagationFactor = propagationFactor;
    this.startingParticle = new OilParticle(particleMass, 0,
        particleMass / density, evaporationRate);
  }

  public double getCellSize() {
    return cellSize;
  }

  public double getTimeStep() {
    return timeStep;
  }

  public double getDensity() {
    return density;
  }

  public double getSurfaceTension() {
    return surfaceTension;
  }

  public OilParticle getStartingParticle() {
    return startingParticle;
  }

  public double getPropagationFactor() {
    return propagationFactor;
  }

}
