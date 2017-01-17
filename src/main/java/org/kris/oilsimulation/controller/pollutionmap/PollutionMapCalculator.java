package org.kris.oilsimulation.controller.pollutionmap;

import org.kris.oilsimulation.model.automatonview.AutomatonView;
import org.kris.oilsimulation.model.automatonview.GridView;
import org.kris.oilsimulation.model.automatonview.History;

public class PollutionMapCalculator {
  private final PollutionMeter pollutionMeter;

  public PollutionMapCalculator(PollutionMeter pollutionMeter) {
    this.pollutionMeter = pollutionMeter;
  }

  public PollutionMap getPollutionMap(AutomatonView automatonView) {
    int height = automatonView.getGridView().getHeight();
    int width = automatonView.getGridView().getWidth();
    int[][] pollutedIterations = new int[height][width];

    addHistoryPollution(automatonView.getHistory(), pollutedIterations);
    addGridViewPollution(automatonView.getGridView(), pollutedIterations);

    return mapToPollutionCell(pollutedIterations, automatonView.getGridView());
  }

  private void addHistoryPollution(History history, int[][] pollutedIterations) {
    for (int i = 0; i < history.size(); i++) {
      addGridViewPollution(history.get(i), pollutedIterations);
    }
  }

  private void addGridViewPollution(GridView gridView, int[][] pollutedIterations) {
    for (int i = 0; i < gridView.getHeight(); i++) {
      for (int j = 0; j < gridView.getWidth(); j++) {
        boolean polluted = pollutionMeter.isPolluted(gridView.getCellView(i, j));
        if (polluted) {
          pollutedIterations[i][j] += 1;
        }
      }
    }
  }

  private PollutionMap mapToPollutionCell(int[][] pollutedIterationsMap, GridView gridView) {
    PollutionCell[][] pollutionCells = new PollutionCell[gridView.getHeight()][gridView.getWidth()];
    for (int i = 0; i < gridView.getHeight(); i++) {
      for (int j = 0; j < gridView.getWidth(); j++) {
        int cellPollutionIterations = pollutedIterationsMap[i][j];
        boolean water = gridView.getCellView(i, j).isWater();
        pollutionCells[i][j] = new PollutionCell(cellPollutionIterations, water);
      }
    }
    return new PollutionMap(pollutionCells);
  }
}
