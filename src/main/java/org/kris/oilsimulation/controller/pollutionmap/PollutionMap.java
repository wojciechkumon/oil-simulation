package org.kris.oilsimulation.controller.pollutionmap;

import java.util.Arrays;

public class PollutionMap {
  private final PollutionCell[][] cells;
  private final int maxIterations;

  public PollutionMap(PollutionCell[][] cells) {
    this.cells = cells;
    this.maxIterations = findMaxIterations(cells);
  }

  private int findMaxIterations(PollutionCell[][] cells) {
    return Arrays.stream(cells)
        .flatMap(Arrays::stream)
        .map(PollutionCell::getPollutedIterations)
        .reduce(0, Math::max);
  }

  public PollutionCell get(int i, int j) {
    return cells[i][j];
  }

  public int getMaxIterations() {
    return maxIterations;
  }

  public int getRows() {
    return cells.length;
  }

  public int getCols() {
    return cells[0].length;
  }
}
