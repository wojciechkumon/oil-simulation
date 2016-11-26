package org.kris.oilsimulation.controller;

import org.kris.oilsimulation.model.ExternalConditions;
import org.kris.oilsimulation.model.OilSimulationConstants;
import org.kris.oilsimulation.model.Size;
import org.kris.oilsimulation.model.Vector;

public class StartUpSettings {
  private final Size size;
  private final ExternalConditions externalConditions;
  private final OilSimulationConstants constants;

  public StartUpSettings(Size size, ExternalConditions externalConditions,
                         OilSimulationConstants constants) {
    this.size = size;
    this.externalConditions = externalConditions;
    this.constants = constants;
  }

  public Size getSize() {
    return size;
  }

  public static StartUpSettings getDefault() {
    return new StartUpSettings(new Size(50, 50),
        new ExternalConditions(new Vector(3, 3), new Vector(-3, -3)),
        new OilSimulationConstants(835 ,30));
  }

  public ExternalConditions getExternalConditions() {
    return externalConditions;
  }

  public OilSimulationConstants getOilSimulationConstants() {
    return null;
  }
}
