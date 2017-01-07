package org.kris.oilsimulation.model;

import java.util.Collections;
import java.util.List;

public class WaterCellState extends AbstractCellState {
  private static final WaterCellState EMPTY_CELL = new WaterCellState(Collections.emptyList());

  public WaterCellState(List<OilParticle> oilParticles) {
    super(oilParticles);
  }

  @Override
  public boolean isWater() {
    return true;
  }

  @Override
  public WaterCellState newSameTypeState(List<OilParticle> oilParticles) {
    return new WaterCellState(oilParticles);
  }

  public static WaterCellState emptyCell() {
    return EMPTY_CELL;
  }
}
