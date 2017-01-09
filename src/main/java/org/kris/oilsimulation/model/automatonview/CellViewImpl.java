package org.kris.oilsimulation.model.automatonview;

class CellViewImpl implements CellView {
  private final double mass;
  private final double volume;
  private final int numberOfParticles;
  private final boolean water;

  CellViewImpl(double mass, double volume, int numberOfParticles, boolean water) {
    this.mass = mass;
    this.volume = volume;
    this.numberOfParticles = numberOfParticles;
    this.water = water;
  }

  @Override
  public double getMass() {
    return mass;
  }

  @Override
  public double getVolume() {
    return volume;
  }

  @Override
  public int getNumberOfParticles() {
    return numberOfParticles;
  }

  @Override
  public boolean isWater() {
    return water;
  }
}
