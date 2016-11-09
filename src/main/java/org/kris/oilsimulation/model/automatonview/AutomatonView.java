package org.kris.oilsimulation.model.automatonview;

import org.kris.oilsimulation.model.CellState;

public interface AutomatonView {

  CellState getState(int row, int col);

  int getWidth();

  int getHeight();

}
