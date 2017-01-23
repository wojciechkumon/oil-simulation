package org.kris.oilsimulation.controller.pollutionmap;

import org.kris.oilsimulation.model.automatonview.CellView;

public class PollutionMeter {
  private static final double DEFAULT_THRESHOLD = 20;
  private final double threshold;

  public PollutionMeter() {
    this(DEFAULT_THRESHOLD);
  }

  public PollutionMeter(double threshold) {
    this.threshold = threshold;
  }

  public boolean isPolluted(CellView cellView) {
    return cellView.getMass() > threshold;
  }

  public double getOilKgThreshold() {
    return threshold;
  }
}
