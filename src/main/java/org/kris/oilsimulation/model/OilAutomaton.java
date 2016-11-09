package org.kris.oilsimulation.model;

import org.kris.oilsimulation.model.automatonview.AutomatonView;

import java.util.Collection;

import static org.kris.oilsimulation.model.BlackWhiteState.BLACK;
import static org.kris.oilsimulation.model.BlackWhiteState.WHITE;

public class OilAutomaton implements Automaton {
  private final AutomatonGrid grid;
  private final Size size;

  public OilAutomaton(Size size) {
    this.grid = new AutomatonGrid(size);
    this.size = size;

    for (int i = 0; i < size.getHeight(); i++) {
      for (int j = 0; j < size.getWidth(); j++) {
        CellState newCellState = (i + j) % 2 == 0 ? BLACK : WHITE;
        grid.set(i, j, newCellState);
      }
    }
  }

  @Override
  public Automaton nextState() {
    OilAutomaton newAutomaton = new OilAutomaton(size);
    for (int i = 0; i < size.getHeight(); i++) {
      for (int j = 0; j < size.getWidth(); j++) {
        CellState newCellState = grid.get(i, j) == WHITE ? BLACK : WHITE;
        newAutomaton.grid.set(i, j, newCellState);
      }
    }
    return newAutomaton;
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
