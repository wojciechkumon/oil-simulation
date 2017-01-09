package org.kris.oilsimulation.model.automatonview;

class EmptyGridView implements GridView {

  EmptyGridView() {}

  @Override
  public CellView getState(int row, int col) {
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
