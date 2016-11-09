package org.kris.oilsimulation.model;

public class Cell {
  private final CellCoords coords;
  private final CellState state;

  public Cell(CellCoords coords, CellState state) {
    this.coords = coords;
    this.state = state;
  }

  public CellCoords getCoords() {
    return coords;
  }

  public CellState getState() {
    return state;
  }

}
