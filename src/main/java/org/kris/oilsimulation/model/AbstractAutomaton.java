package org.kris.oilsimulation.model;

import org.kris.oilsimulation.model.automatonview.AutomatonView;

import java.util.Collection;

public abstract class AbstractAutomaton implements Automaton {
  protected final AutomatonGrid grid;
  protected final Size size;

  public AbstractAutomaton(Size size) {
    this.grid = new AutomatonGrid(size);
    this.size = size;
  }

  @Override
  public void insertStructure(Collection<Cell> structure) {
    structure.forEach(cell -> {
      CellCoords coords = cell.getCoords();
      grid.set(coords.getRow(), coords.getCol(), cell.getState());
    });
  }

  @Override
  public AutomatonView getAutomatonView() {
    return grid.getAutomatonView();
  }

}
