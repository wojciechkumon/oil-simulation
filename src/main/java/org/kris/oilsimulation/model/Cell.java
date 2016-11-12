package org.kris.oilsimulation.model;

public class Cell {
  private final CellCoords coords;
  private final CellState state;

  private Cell(CellCoords coords, CellState state) {
    this.coords = coords;
    this.state = state;
  }

  public static Cell newCell(CellCoords coords, CellState state) {
    return new Cell(coords, state);
  }

  public CellCoords getCoords() {
    return coords;
  }

  public CellState getState() {
    return state;
  }

}
