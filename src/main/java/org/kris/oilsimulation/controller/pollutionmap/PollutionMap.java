package org.kris.oilsimulation.controller.pollutionmap;

import java.util.Arrays;
import java.util.function.Predicate;

public class PollutionMap {
  private final PollutionCell[][] cells;
  private final int maxWaterIterations;
  private final int maxLandIterations;

  public PollutionMap(PollutionCell[][] cells) {
    this.cells = cells;
    Predicate<PollutionCell> isWaterPredicate = PollutionCell::isWater;
    this.maxWaterIterations = findMaxIterations(cells, isWaterPredicate);
    this.maxLandIterations = findMaxIterations(cells, isWaterPredicate.negate());
  }

  private int findMaxIterations(PollutionCell[][] cells, Predicate<? super PollutionCell> predicate) {
    return Arrays.stream(cells)
        .flatMap(Arrays::stream)
        .filter(predicate)
        .map(PollutionCell::getPollutedIterations)
        .reduce(0, Math::max);
  }

  public PollutionCell get(int i, int j) {
    return cells[i][j];
  }

  public int getWaterMaxIterations() {
    return maxWaterIterations;
  }

  public int getLandMaxIterations() {
    return maxLandIterations;
  }

  public int getRows() {
    return cells.length;
  }

  public int getCols() {
    return cells[0].length;
  }
}
