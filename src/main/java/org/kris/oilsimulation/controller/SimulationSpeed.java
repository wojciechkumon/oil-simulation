package org.kris.oilsimulation.controller;

public enum SimulationSpeed {

  SINGLE_STEP(-1),
  PER_SECOND_1(1000),
  PER_SECOND_5(200),
  PER_SECOND_10(100),
  PER_SECOND_25(40),
  PER_SECOND_50(20),
  PER_SECOND_100(10);

  private final int iterationDelayMillis;

  SimulationSpeed(int iterationDelayMillis) {
    this.iterationDelayMillis = iterationDelayMillis;
  }

  public int getIterationDelayMillis() {
    return iterationDelayMillis;
  }

}
