package org.kris.oilsimulation.model;

import org.kris.oilsimulation.model.automatonview.AutomatonView;
import org.kris.oilsimulation.model.automatonview.AutomatonViewFactory;

public class AutomatonGrid {
  private final CellState[][] grid;
  private final Size size;

  public AutomatonGrid(Size size) {
    this.grid = new CellState[size.getHeight()][size.getWidth()];
    this.size = size;
  }

  public CellState get(int row, int col) {
    return grid[row][col];
  }

  public void set(int row, int col, CellState newState) {
    grid[row][col] = newState;
  }

  public AutomatonView getAutomatonView() {
    return AutomatonViewFactory.create(grid, size);
  }

  public Size getSize() {
    return size;
  }

}
