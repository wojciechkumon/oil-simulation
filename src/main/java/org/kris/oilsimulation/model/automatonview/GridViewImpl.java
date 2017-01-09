package org.kris.oilsimulation.model.automatonview;

import org.kris.oilsimulation.model.CellState;
import org.kris.oilsimulation.model.Size;

class GridViewImpl implements GridView {
  private final Size size;
  private final CellView[][] cellViewGrid;

  GridViewImpl(CellState[][] grid, Size size) {
    this.size = size;
    this.cellViewGrid = new CellView[grid.length][grid[0].length];
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[0].length; j++) {
        this.cellViewGrid[i][j] = toCellView(grid[i][j]);
      }
    }
  }

  private CellView toCellView(CellState cellState) {
    return new CellViewImpl(cellState.getMass(), cellState.getVolume(),
        cellState.getOilParticles().size(), cellState.isWater());
  }

  @Override
  public CellView getState(int row, int col) {
    return cellViewGrid[row][col];
  }

  @Override
  public int getWidth() {
    return size.getWidth();
  }

  @Override
  public int getHeight() {
    return size.getHeight();
  }
}
