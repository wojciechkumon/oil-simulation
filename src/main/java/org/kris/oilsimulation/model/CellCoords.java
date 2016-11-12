package org.kris.oilsimulation.model;

import java.util.Objects;

public class CellCoords {
  private final int row;
  private final int col;

  private CellCoords(int row, int col) {
    this.row = row;
    this.col = col;
  }

  public static CellCoords newCellCoords(int row, int col) {
    return new CellCoords(row, col);
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CellCoords that = (CellCoords) o;
    return row == that.row && col == that.col;
  }

  @Override
  public int hashCode() {
    return Objects.hash(row, col);
  }

}
