package org.kris.oilsimulation.model.automatonview;

public interface GridView {

  CellView getState(int row, int col);

  int getWidth();

  int getHeight();
}
