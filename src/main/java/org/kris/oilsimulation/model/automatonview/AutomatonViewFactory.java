package org.kris.oilsimulation.model.automatonview;

import org.kris.oilsimulation.model.CellState;
import org.kris.oilsimulation.model.Size;

public class AutomatonViewFactory {

  private AutomatonViewFactory() {}

  public static AutomatonView create(CellState[][] grid, Size size) {
    return new AutomatonViewImpl(grid, size);
  }

  public static AutomatonView getEmptyView() {
    return Holder.EMPTY_AUTOMATON_VIEW;
  }

  private static class Holder {
    private static final EmptyAutomatonView EMPTY_AUTOMATON_VIEW = new EmptyAutomatonView();
  }

}
