package org.kris.oilsimulation.model;

public class OilCellState implements CellState {
  private static OilCellState EMPTY_CELL = new OilCellState(0);

  private final int oilParticles;

  public OilCellState(int oilParticles) {
    this.oilParticles = oilParticles;
  }

  public int getOilParticles() {
    return oilParticles;
  }

  public static OilCellState emptyCell() {
    return EMPTY_CELL;
  }

}
