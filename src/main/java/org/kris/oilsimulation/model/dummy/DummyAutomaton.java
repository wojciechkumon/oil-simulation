package org.kris.oilsimulation.model.dummy;

import org.kris.oilsimulation.model.AbstractAutomaton;
import org.kris.oilsimulation.model.Automaton;
import org.kris.oilsimulation.model.CellState;
import org.kris.oilsimulation.model.Size;

import static org.kris.oilsimulation.model.dummy.BlackWhiteState.BLACK;
import static org.kris.oilsimulation.model.dummy.BlackWhiteState.WHITE;

public class DummyAutomaton extends AbstractAutomaton {

  public DummyAutomaton(Size size) {
    super(size);

    for (int i = 0; i < size.getHeight(); i++) {
      for (int j = 0; j < size.getWidth(); j++) {
        CellState newCellState = (i + j) % 2 == 0 ? BLACK : WHITE;
        grid.set(i, j, newCellState);
      }
    }
  }

  @Override
  public Automaton nextState() {
    DummyAutomaton newAutomaton = new DummyAutomaton(size);
    for (int i = 0; i < size.getHeight(); i++) {
      for (int j = 0; j < size.getWidth(); j++) {
        CellState newCellState = grid.get(i, j) == WHITE ? BLACK : WHITE;
        newAutomaton.grid.set(i, j, newCellState);
      }
    }
    return newAutomaton;
  }

}
