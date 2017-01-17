package org.kris.oilsimulation.controller.pollutionmap;

public class PollutionMap {
  private final PollutionCell[][] cells;
  private final int maxIterations;

  public PollutionMap(PollutionCell[][] cells) {
    this.cells = cells;
    this.maxIterations = findMaxIterations(cells);
  }

  private int findMaxIterations(PollutionCell[][] cells) {
    int max = 0;
    for (int i = 0; i < cells.length; i++) {
      for (int j = 0; j < cells[0].length; j++) {
        if (cells[i][j].getPollutedIterations() > max) {
          max = cells[i][j].getPollutedIterations();
        }
      }
    }
    return max;
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
