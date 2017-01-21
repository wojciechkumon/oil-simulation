package org.kris.oilsimulation.model.cell;

import java.util.Collections;
import java.util.List;

public class LandCellState extends AbstractCellState {
  private static final LandCellState EMPTY_CELL = new LandCellState(Collections.emptyList());

  public LandCellState(List<OilParticle> oilParticles) {
    super(oilParticles);
  }

  @Override
  public boolean isWater() {
    return false;
  }

  @Override
  public LandCellState newSameTypeState(List<OilParticle> oilParticles) {
    return new LandCellState(oilParticles);
  }

  public static LandCellState emptyCell() {
    return EMPTY_CELL;
  }
}
