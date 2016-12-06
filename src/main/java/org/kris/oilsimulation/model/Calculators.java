package org.kris.oilsimulation.model;

public class Calculators {
  private final SpreadingCalculator spreadingCalculator;
  private final AdvectionCalculator advectionCalculator;
  private final OilSourcesCalculator oilSourcesCalculator;

  public Calculators(SpreadingCalculator spreadingCalculator,
                     AdvectionCalculator advectionCalculator, OilSourcesCalculator oilSourcesCalculator) {
    this.spreadingCalculator = spreadingCalculator;
    this.advectionCalculator = advectionCalculator;
    this.oilSourcesCalculator = oilSourcesCalculator;
  }

  public SpreadingCalculator getSpreadingCalculator() {
    return spreadingCalculator;
  }

  public AdvectionCalculator getAdvectionCalculator() {
    return advectionCalculator;
  }

  public OilSourcesCalculator getOilSourcesCalculator() {
    return oilSourcesCalculator;
  }
}
