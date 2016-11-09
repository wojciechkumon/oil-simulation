package org.kris.oilsimulation.model.automatonview;

import org.kris.oilsimulation.model.CellState;
import org.kris.oilsimulation.model.Size;

class AutomatonViewImpl implements AutomatonView {
  private final CellState[][] grid;
  private final Size size;

  AutomatonViewImpl(CellState[][] grid, Size size) {
    this.grid = grid;
    this.size = size;
  }

  public CellState getState(int row, int col) {
    return grid[row][col];
  }

  public int getWidth() {
    return size.getWidth();
  }

  public int getHeight() {
    return size.getHeight();
  }

}
