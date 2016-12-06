package org.kris.oilsimulation.model;

import java.util.Collections;
import java.util.List;

public class OilCellState implements CellState {
  private static final OilCellState EMPTY_CELL = new OilCellState(Collections.emptyList());
  private final List<OilParticle> oilParticles;

  public OilCellState(List<OilParticle> oilParticles) {
    this.oilParticles = Collections.unmodifiableList(oilParticles);
  }

  public List<OilParticle> getOilParticles() {
    return oilParticles;
  }

  public double getMass() {
    return oilParticles.stream()
        .map(OilParticle::getMass)
        .reduce(0.0, Double::sum);
  }

  public double getVolume() {
    return oilParticles.stream()
        .map(OilParticle::getVolume)
        .reduce(0.0, Double::sum);
  }

  public static OilCellState emptyCell() {
    return EMPTY_CELL;
  }

}
