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
    Size size = new Size(150, 150);
    Vector current = new Vector(3, 3);
    Vector wind = new Vector(-3, -3);
    ExternalConditions externalConditions = new ExternalConditions(current, wind);
    OilSimulationConstants constants =
        new OilSimulationConstants(50, 600, 835, 30, 50, 50, 3);

    return new StartUpSettings(size, externalConditions, constants);
  }

  public ExternalConditions getExternalConditions() {
    return externalConditions;
  }

  public OilSimulationConstants getOilSimulationConstants() {
    return constants;
  }
}
