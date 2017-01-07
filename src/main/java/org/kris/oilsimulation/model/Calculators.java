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

  public SpreadingCalculator spreading() {
    return spreadingCalculator;
  }

  public AdvectionCalculator advection() {
    return advectionCalculator;
  }

  public OilSourcesCalculator oilSources() {
    return oilSourcesCalculator;
  }
}
