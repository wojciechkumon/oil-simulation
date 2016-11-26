package org.kris.oilsimulation.model;

public class AdvectionCalculator {

  public static void apply(AutomatonGrid oldAutomatonGrid, AutomatonGrid newAutomatonGrid,
                           ExternalConditions externalConditions) {
    Size size = oldAutomatonGrid.getSize();
    Vector resultantVector = calculateResultantVector(externalConditions);
    int roundedHorizontal = (int) Math.round(resultantVector.getX());
    int roundedVertical = (int) Math.round(resultantVector.getY());
    for (int i = 0; i < size.getHeight(); i++) {
      for (int j = 0; j < size.getWidth(); j++) {
        setNewCellState(oldAutomatonGrid, newAutomatonGrid, roundedHorizontal, roundedVertical, i, j);
      }
    }
  }

  private static void setNewCellState(AutomatonGrid oldAutomatonGrid, AutomatonGrid newAutomatonGrid,
                                      int roundedHorizontal, int roundedVertical,
                                      int i, int j) {
    int newI = i + roundedVertical;
    int newJ = j + roundedHorizontal;
    if (isInsideGrid(newI, newJ, newAutomatonGrid.getSize())) {
      newAutomatonGrid.set(newI, newJ, oldAutomatonGrid.get(i, j));
    }
  }

  private static Vector calculateResultantVector(ExternalConditions externalConditions) {
    Vector current = externalConditions.getCurrent().scalarMul(1.1);
    Vector wind = externalConditions.getWind().scalarMul(0.3);
    return current.add(wind);
  }

  private static boolean isInsideGrid(int newI, int newJ, Size size) {
    return newI >= 0 && newI < size.getHeight()
        && newJ >= 0 && newJ < size.getWidth();
  }

}
