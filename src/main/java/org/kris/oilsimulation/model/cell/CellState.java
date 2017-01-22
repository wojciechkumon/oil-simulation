package org.kris.oilsimulation.model.cell;

import java.io.Serializable;
import java.util.List;

public interface CellState extends Serializable {

  boolean isWater();

  CellState newSameTypeState(List<OilParticle> oilParticles);

  List<OilParticle> getOilParticles();

  double getMass();

  double getVolume();

}
