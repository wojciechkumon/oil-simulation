package org.kris.oilsimulation.model;

public class Calculators {
  private final SpreadingCalculator spreadingCalculator;
  private final AdvectionCalculator advectionCalculator;

  public Calculators(SpreadingCalculator spreadingCalculator,
                     AdvectionCalculator advectionCalculator) {
    this.spreadingCalculator = spreadingCalculator;
    this.advectionCalculator = advectionCalculator;
  }

  public SpreadingCalculator getSpreadingCalculator() {
    return spreadingCalculator;
  }

  public AdvectionCalculator getAdvectionCalculator() {
    return advectionCalculator;
  }

}
