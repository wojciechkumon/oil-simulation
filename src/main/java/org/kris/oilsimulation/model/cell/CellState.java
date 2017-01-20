package org.kris.oilsimulation.model.cell;

import java.util.List;

public interface CellState {

  boolean isWater();

  CellState newSameTypeState(List<OilParticle> oilParticles);

  List<OilParticle> getOilParticles();

  double getMass();

  double getVolume();

}
