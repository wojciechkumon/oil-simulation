package org.kris.oilsimulation.model;

import org.kris.oilsimulation.model.automatonview.GridView;
import org.kris.oilsimulation.model.automatonview.GridViewFactory;
import org.kris.oilsimulation.model.cell.CellCoords;
import org.kris.oilsimulation.model.cell.CellState;

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

  public CellState get(CellCoords coords) {
    return grid[coords.getRow()][coords.getCol()];
  }

  public void set(int row, int col, CellState newState) {
    grid[row][col] = newState;
  }

  public void set(CellCoords coords, CellState newState) {
    grid[coords.getRow()][coords.getCol()] = newState;
  }

  public GridView getGridView() {
    return GridViewFactory.create(grid, size);
  }

  public Size getSize() {
    return size;
  }

  public void copyTo(AutomatonGrid newGrid) {
    if (size.getWidth() != newGrid.size.getWidth() ||
        size.getHeight() != newGrid.size.getHeight()) {
      throw new IllegalArgumentException("Size doesn't match: " + size + " " + newGrid.size);
    }
    for (int i = 0; i < size.getHeight(); i++) {
      System.arraycopy(grid[i], 0, newGrid.grid[i], 0, size.getWidth());
    }
  }
}
