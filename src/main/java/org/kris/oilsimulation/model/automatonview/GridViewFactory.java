package org.kris.oilsimulation.model.automatonview;

import org.kris.oilsimulation.model.cell.CellState;
import org.kris.oilsimulation.model.Size;

public class GridViewFactory {

  private GridViewFactory() {}

  public static GridView create(CellState[][] grid, Size size) {
    return new GridViewImpl(grid, size);
  }

  public static GridView getEmptyView() {
    return Holder.EMPTY_GRID_VIEW;
  }

  private static class Holder {
    private static final EmptyGridView EMPTY_GRID_VIEW = new EmptyGridView();
  }
}
