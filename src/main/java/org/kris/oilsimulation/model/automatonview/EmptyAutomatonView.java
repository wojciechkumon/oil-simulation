package org.kris.oilsimulation.model.automatonview;

import org.kris.oilsimulation.model.CellState;

class EmptyAutomatonView implements AutomatonView {

  @Override
  public CellState getState(int row, int col) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getWidth() {
    return 0;
  }

  @Override
  public int getHeight() {
    return 0;
  }

}
