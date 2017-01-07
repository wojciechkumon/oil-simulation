package org.kris.oilsimulation.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static org.kris.oilsimulation.model.CellCoords.newCellCoords;

public class SpreadingCalculator {
  private static final double GRAVITY = 9.80665;
  private final Random random;

  public SpreadingCalculator(Random random) {
    this.random = random;
  }

  public void apply(AutomatonGrid automatonGrid, OilSimulationConstants constants) {
    firstHorizontalSpreading(automatonGrid, constants);
    secondHorizontalSpreading(automatonGrid, constants);
    firstVerticalSpreading(automatonGrid, constants);
    secondVerticalSpreading(automatonGrid, constants);
  }

  private void firstHorizontalSpreading(AutomatonGrid grid,
                                        OilSimulationConstants constants) {
    Size size = grid.getSize();
    for (int i = 0; i < size.getHeight(); i++) {
      for (int j = 2; j < size.getWidth(); j += 2) {
        applySpreading(grid.get(i, j - 1), grid.get(i, j),
            grid, newCellCoords(i, j - 1), newCellCoords(i, j), constants);
      }
    }
  }

  private void secondHorizontalSpreading(AutomatonGrid grid,
                                         OilSimulationConstants constants) {
    Size size = grid.getSize();
    for (int i = 0; i < size.getHeight(); i++) {
      for (int j = 1; j < size.getWidth(); j += 2) {
        applySpreading(grid.get(i, j - 1), grid.get(i, j),
            grid, newCellCoords(i, j - 1), newCellCoords(i, j), constants);
      }
    }
  }

  private void firstVerticalSpreading(AutomatonGrid grid,
                                      OilSimulationConstants constants) {
    Size size = grid.getSize();
    for (int j = 0; j < size.getWidth(); j++) {
      for (int i = 2; i < size.getWidth(); i += 2) {
        applySpreading(grid.get(i - 1, j), grid.get(i, j),
            grid, newCellCoords(i - 1, j), newCellCoords(i, j), constants);
      }
    }
  }

  private void secondVerticalSpreading(AutomatonGrid grid,
                                       OilSimulationConstants constants) {
    Size size = grid.getSize();
    for (int j = 0; j < size.getWidth(); j++) {
      for (int i = 1; i < size.getWidth(); i += 2) {
        applySpreading(grid.get(i - 1, j), grid.get(i, j),
            grid, newCellCoords(i - 1, j), newCellCoords(i, j), constants);
      }
    }
  }

  private void applySpreading(CellState first, CellState second,
                              AutomatonGrid grid,
                              CellCoords firstCoords, CellCoords secondCoords,
                              OilSimulationConstants constants) {
    double massChange = calculateMassChange(first, second, constants);
    if (massChange > 0) {
      moveMass(massChange, second, first, grid, secondCoords, firstCoords);
    } else if (massChange < 0) {
      moveMass(massChange, first, second, grid, firstCoords, secondCoords);
    }
  }

  private double calculateMassChange(CellState first, CellState second,
                                     OilSimulationConstants constants) {
    double firstMass = first.getMass();
    double secondMass = second.getMass();
    double timeStep = constants.getTimeStep();
    double cellLengthSquared = constants.getCellSize() * constants.getCellSize();
    double coefficient = calculateCoefficient(constants, first, second);
    return ((secondMass - firstMass) / 2) *
        (1 - Math.exp(-2 * timeStep * coefficient / cellLengthSquared));
  }

  private double calculateCoefficient(OilSimulationConstants constants, CellState first,
                                      CellState second) {
    double propagationFactorSquared = constants.getPropagationFactor()
        * constants.getPropagationFactor();
    double timeStep = constants.getTimeStep();
    double oilVolumeSquared = calculateOilVolumeSquared(first, second);
    double density = constants.getDensity(); // TODO check
    double viscosity = 15.0;
    return (0.48 / propagationFactorSquared) * Math.pow(timeStep, -0.5)
        * Math.pow(oilVolumeSquared * GRAVITY * density / viscosity, 1.0 / 3);
  }

  private static double calculateOilVolumeSquared(CellState first, CellState second) {
    double volume = first.getVolume() + second.getVolume();
    return volume * volume;
  }

  private void moveMass(double massChange, CellState source, CellState target,
                        AutomatonGrid grid, CellCoords sourceCoords, CellCoords targetCoords) {
    double r = Math.abs(massChange) / source.getMass();

    List<OilParticle> sourceParticles = new ArrayList<>(source.getOilParticles());
    List<OilParticle> targetParticles = new ArrayList<>(target.getOilParticles());

    moveParticles(source, grid, sourceCoords, targetCoords, r, sourceParticles, targetParticles);
  }

  private void moveParticles(CellState source, AutomatonGrid grid,
                             CellCoords sourceCoords, CellCoords targetCoords, double r,
                             List<OilParticle> sourceParticles, List<OilParticle> targetParticles) {
    Iterator<OilParticle> iterator = sourceParticles.iterator();

    while (iterator.hasNext()) {
      OilParticle oilParticle = iterator.next();
      double randomValue = random.nextDouble();
      if (randomValue < r) {
        iterator.remove();
        targetParticles.add(oilParticle);
      }
    }

    if (source.getOilParticles().size() != sourceParticles.size()) {
      grid.set(sourceCoords, getNewCellState(grid, sourceCoords, sourceParticles));
      grid.set(targetCoords, getNewCellState(grid, targetCoords, targetParticles));
    }
  }

  private CellState getNewCellState(AutomatonGrid grid, CellCoords coords,
                                    List<OilParticle> particles) {
    return grid.get(coords).newSameTypeState(particles);
  }

}
