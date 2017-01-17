package org.kris.oilsimulation.controller.pollutionmap;

public class PollutionCell {
  private final int pollutedIterations;
  private final boolean water;

  public PollutionCell(int pollutedIterations, boolean water) {
    this.pollutedIterations = pollutedIterations;
    this.water = water;
  }

  public int getPollutedIterations() {
    return pollutedIterations;
  }

  public boolean isWater() {
    return water;
  }
}
