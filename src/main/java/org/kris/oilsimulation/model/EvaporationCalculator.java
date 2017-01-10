package org.kris.oilsimulation.model;

import java.util.List;
import java.util.stream.Collectors;

public class EvaporationCalculator {

  public void apply(AutomatonGrid grid) {
    Size size = grid.getSize();
    for (int i = 0; i < size.getHeight(); i++) {
      for (int j = 0; j < size.getWidth(); j++) {
        evaporateCell(grid, i, j);
      }
    }
  }

  private void evaporateCell(AutomatonGrid grid, int i, int j) {
    CellState state = grid.get(i, j);

    List<OilParticle> newParticles = state.getOilParticles().stream()
        .map(this::evaporate)
        .collect(Collectors.toList());

    grid.set(i, j, state.newSameTypeState(newParticles));
  }

  private OilParticle evaporate(OilParticle oilParticle) {
    double evaporationRate = oilParticle.getEvaporationRate();

    double newMass = oilParticle.getMass() * (1 - evaporationRate);
    double newEvaporationRate = evaporationRate - Math.pow(evaporationRate, 2);

    return new OilParticle(newMass, oilParticle.getWaterContent(),
        oilParticle.getVolume(), newEvaporationRate);
  }
}
