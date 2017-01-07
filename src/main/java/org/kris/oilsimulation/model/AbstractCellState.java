package org.kris.oilsimulation.model;

import java.util.Collections;
import java.util.List;

public abstract class AbstractCellState implements CellState {
  private final List<OilParticle> oilParticles;

  public AbstractCellState(List<OilParticle> oilParticles) {
    this.oilParticles = Collections.unmodifiableList(oilParticles);
  }

  @Override
  public List<OilParticle> getOilParticles() {
    return oilParticles;
  }

  @Override
  public double getMass() {
    return oilParticles.stream()
        .map(OilParticle::getMass)
        .reduce(0.0, Double::sum);
  }

  @Override
  public double getVolume() {
    return oilParticles.stream()
        .map(OilParticle::getVolume)
        .reduce(0.0, Double::sum);
  }
}
