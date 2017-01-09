package org.kris.oilsimulation.model.automatonview;

public interface GridView {

  CellView getCellView(int row, int col);

  int getWidth();

  int getHeight();
}
