package org.kris.oilsimulation.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AdvectionCalculator {
  private final Random random;

  public AdvectionCalculator(Random random) {
    this.random = random;
  }

  public void apply(AutomatonGrid oldAutomatonGrid, AutomatonGrid newAutomatonGrid,
                    ExternalConditions externalConditions, double cellSize) {
    Size size = oldAutomatonGrid.getSize();
    Vector resultantVector = calculateResultantVector(externalConditions);
    double horizontal = resultantVector.getX() / cellSize;
    double vertical = resultantVector.getY() / cellSize;
    for (int i = 0; i < size.getHeight(); i++) {
      for (int j = 0; j < size.getWidth(); j++) {
        setNewCellState(oldAutomatonGrid, newAutomatonGrid, horizontal, vertical, i, j);
      }
    }
  }

  private Vector calculateResultantVector(ExternalConditions externalConditions) {
    Vector current = externalConditions.getCurrent().scalarMul(1.1);
    Vector wind = externalConditions.getWind().scalarMul(0.3);
    return current.add(wind);
  }

  private void setNewCellState(AutomatonGrid oldAutomatonGrid, AutomatonGrid newAutomatonGrid,
                               double horizontal, double vertical, int i, int j) {
    OilCellState source = ((OilCellState) oldAutomatonGrid.get(i, j));
    if (source.getOilParticles().isEmpty()) {
      return;
    }

    int newI = calculateNewPos(i, vertical);
    int newJ = calculateNewPos(j, horizontal);
    if (isInsideGrid(newI, newJ, newAutomatonGrid.getSize())) {
      addToNewGrid(oldAutomatonGrid, newAutomatonGrid, i, j, newI, newJ);
    }
  }

  private int calculateNewPos(int i, double distance) {
    int roundedDistance = (int) distance;
    double diff = distance - roundedDistance;

    double absDiff = Math.abs(diff);
    double randomValue = random.nextDouble();
    if (randomValue < absDiff) {
      return i + roundedDistance + (diff >= 0 ? 1 : -1);
    }

    return i + roundedDistance;
  }

  private boolean isInsideGrid(int newI, int newJ, Size size) {
    return newI >= 0 && newI < size.getHeight()
        && newJ >= 0 && newJ < size.getWidth();
  }

  private void addToNewGrid(AutomatonGrid oldAutomatonGrid, AutomatonGrid newAutomatonGrid,
                            int i, int j, int newI, int newJ) {
    OilCellState oilCellState = (OilCellState) newAutomatonGrid.get(newI, newJ);
    if (oilCellState.getOilParticles().isEmpty()) {
      newAutomatonGrid.set(newI, newJ, oldAutomatonGrid.get(i, j));
    } else {
      List<OilParticle> oldGridParticles = ((OilCellState) newAutomatonGrid.get(i, j)).getOilParticles();
      List<OilParticle> totalParticles = new ArrayList<>(oilCellState.getOilParticles());
      totalParticles.addAll(oldGridParticles);
      newAutomatonGrid.set(newI, newJ, new OilCellState(totalParticles));
    }
  }

}
