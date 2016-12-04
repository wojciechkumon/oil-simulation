package org.kris.oilsimulation.model;

import static org.kris.oilsimulation.model.CellCoords.newCellCoords;

public class SpreadingCalculator {

  public static void apply(AutomatonGrid oldAutomatonGrid, AutomatonGrid newAutomatonGrid) {
    AutomatonGrid tmpGrid = new AutomatonGrid(oldAutomatonGrid.getSize());
    oldAutomatonGrid.copyTo(tmpGrid);

    firstHorizontalSpreading(oldAutomatonGrid, tmpGrid);
    secondHorizontalSpreading(tmpGrid, newAutomatonGrid);
    firstVerticalSpreading(newAutomatonGrid, tmpGrid);
    secondVerticalSpreading(tmpGrid, newAutomatonGrid);
  }

  private static void firstHorizontalSpreading(AutomatonGrid oldAutomatonGrid,
                                               AutomatonGrid newAutomatonGrid) {
    Size size = oldAutomatonGrid.getSize();
    for (int i = 0; i < size.getHeight(); i++) {
      for (int j = 2; j < size.getWidth(); j += 2) {
        applySpreading((OilCellState) oldAutomatonGrid.get(i, j - 1),
            (OilCellState) oldAutomatonGrid.get(i, j),
            newAutomatonGrid, newCellCoords(i, j - 1), newCellCoords(i, j));
      }
    }
  }

  private static void applySpreading(OilCellState first, OilCellState second,
                                     AutomatonGrid newAutomatonGrid,
                                     CellCoords firstCoords, CellCoords secondCoords) {

  }


  private static void secondHorizontalSpreading(AutomatonGrid oldAutomatonGrid,
                                                AutomatonGrid newAutomatonGrid) {

  }

  private static void firstVerticalSpreading(AutomatonGrid oldAutomatonGrid,
                                             AutomatonGrid newAutomatonGrid) {

  }

  private static void secondVerticalSpreading(AutomatonGrid oldAutomatonGrid,
                                              AutomatonGrid newAutomatonGrid) {

  }
}
