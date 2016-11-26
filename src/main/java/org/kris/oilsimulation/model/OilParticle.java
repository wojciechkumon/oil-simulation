package org.kris.oilsimulation.model;

public class OilParticle {
  private final double mass;
  private final double waterContent;
  private final double volume;
  private final double evaporationRate;

  public OilParticle(double mass, double waterContent, double volume, double evaporationRate) {
    this.mass = mass;
    this.waterContent = waterContent;
    this.volume = volume;
    this.evaporationRate = evaporationRate;
  }

  public double getMass() {
    return mass;
  }

  public double getWaterContent() {
    return waterContent;
  }

  public double getVolume() {
    return volume;
  }

  public double getEvaporationRate() {
    return evaporationRate;
  }
}
