package org.kris.oilsimulation.model;

public class Calculators {
  private final SpreadingCalculator spreadingCalculator;
  private final AdvectionCalculator advectionCalculator;
  private final OilSourcesCalculator oilSourcesCalculator;
  private final EvaporationCalculator evaporationCalculator;

  public Calculators(SpreadingCalculator spreadingCalculator,
                     AdvectionCalculator advectionCalculator,
                     OilSourcesCalculator oilSourcesCalculator,
                     EvaporationCalculator evaporationCalculator) {
    this.spreadingCalculator = spreadingCalculator;
    this.advectionCalculator = advectionCalculator;
    this.oilSourcesCalculator = oilSourcesCalculator;
    this.evaporationCalculator = evaporationCalculator;
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

  public EvaporationCalculator evaporation() {
    return evaporationCalculator;
  }
}
