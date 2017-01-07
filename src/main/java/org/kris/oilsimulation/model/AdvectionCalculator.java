package org.kris.oilsimulation.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.kris.oilsimulation.model.CellCoords.newCellCoords;

public class AdvectionCalculator {
  private static final double CURRENT_COEFFICIENT = 1.1;
  private static final double WIND_COEFFICIENT = 0.3;
  private final Random random;

  public AdvectionCalculator(Random random) {
    this.random = random;
  }

  public void apply(AutomatonGrid oldAutomatonGrid, AutomatonGrid newAutomatonGrid,
                    ExternalConditions externalConditions, double cellSize) {
    Map<CellCoords, List<OilParticle>> particlesMap =
        createParticlesMap(oldAutomatonGrid, externalConditions, cellSize);
    copyFromMapToGrid(oldAutomatonGrid, particlesMap, newAutomatonGrid);
  }

  private Map<CellCoords, List<OilParticle>> createParticlesMap(AutomatonGrid oldAutomatonGrid,
                                                                ExternalConditions externalConditions,
                                                                double cellSize) {
    Size size = oldAutomatonGrid.getSize();
    Vector resultantVector = calculateResultantVector(externalConditions);
    double horizontal = resultantVector.getX() / cellSize;
    double vertical = resultantVector.getY() / cellSize;
    Map<CellCoords, List<OilParticle>> particlesMap = new HashMap<>();
    for (int i = 0; i < size.getHeight(); i++) {
      for (int j = 0; j < size.getWidth(); j++) {
        putParticlesToMap(oldAutomatonGrid, particlesMap, horizontal, vertical, i, j);
      }
    }
    return particlesMap;
  }

  private Vector calculateResultantVector(ExternalConditions externalConditions) {
    Vector current = externalConditions.getCurrent().scalarMul(CURRENT_COEFFICIENT);
    Vector wind = externalConditions.getWind().scalarMul(WIND_COEFFICIENT);
    return current.add(wind);
  }

  private void putParticlesToMap(AutomatonGrid oldAutomatonGrid,
                                 Map<CellCoords, List<OilParticle>> particlesMap,
                                 double horizontal, double vertical, int i, int j) {
    CellState source = oldAutomatonGrid.get(i, j);
    if (source.getOilParticles().isEmpty()) {
      return;
    }

    CellCoords closerPos = newCellCoords(closerPos(i, vertical), closerPos(j, horizontal));
    CellCoords furtherPos = newCellCoords(furtherPos(i, vertical), furtherPos(j, horizontal));
    double verticalThreshold = getThreshold(vertical);
    double horizontalThreshold = getThreshold(horizontal);

    source.getOilParticles().forEach(particle -> {
      int newRow = getNewIndex(verticalThreshold,
          closerPos.getRow(), furtherPos.getRow());
      int newCol = getNewIndex(horizontalThreshold,
          closerPos.getCol(), furtherPos.getCol());
      addToMap(particlesMap, newCellCoords(newRow, newCol), particle);
    });
  }

  private void addToMap(Map<CellCoords, List<OilParticle>> particlesMap,
                        CellCoords coords, OilParticle newParticle) {
    List<OilParticle> particles = particlesMap.get(coords);
    if (particles != null) {
      particles.add(newParticle);
      return;
    }
    particles = new ArrayList<>();
    particles.add(newParticle);
    particlesMap.put(coords, particles);
  }

  private int getNewIndex(double threshold, int closerIndex, int furtherIndex) {
    if (random.nextDouble() < threshold) {
      return furtherIndex;
    }
    return closerIndex;
  }

  private void copyFromMapToGrid(AutomatonGrid oldAutomatonGrid,
                                 Map<CellCoords, List<OilParticle>> particlesMap,
                                 AutomatonGrid newAutomatonGrid) {
    particlesMap.entrySet()
        .stream()
        .filter(entry -> isInsideGrid(entry.getKey(), newAutomatonGrid.getSize()))
        .forEach(entry -> {
          CellCoords coords = entry.getKey();
          CellState newCellState = getNewCellState(oldAutomatonGrid, entry, coords);
          newAutomatonGrid.set(coords.getRow(), coords.getCol(), newCellState);
        });
  }

  private CellState getNewCellState(AutomatonGrid oldAutomatonGrid,
                                    Map.Entry<CellCoords, List<OilParticle>> entry, CellCoords coords) {
    CellState cellState = oldAutomatonGrid.get(coords.getRow(), coords.getCol());
    return cellState.newSameTypeState(entry.getValue());
  }

  private boolean isInsideGrid(CellCoords coords, Size size) {
    return coords.getRow() >= 0 && coords.getRow() < size.getHeight()
        && coords.getCol() >= 0 && coords.getCol() < size.getWidth();
  }

  private int closerPos(int i, double distance) {
    int roundedDistance = (int) distance;
    return i + roundedDistance;
  }

  private int furtherPos(int i, double distance) {
    int roundedDistance = (int) distance;
    double diff = distance - roundedDistance;
    return i + roundedDistance + (diff >= 0 ? 1 : -1);
  }

  private double getThreshold(double distance) {
    int roundedDistance = (int) distance;
    return Math.abs(distance - roundedDistance);
  }
}
