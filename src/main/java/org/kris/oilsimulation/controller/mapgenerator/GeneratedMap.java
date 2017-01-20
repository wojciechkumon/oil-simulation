package org.kris.oilsimulation.controller.mapgenerator;

import org.kris.oilsimulation.model.InitialStates;
import org.kris.oilsimulation.model.Size;

public class GeneratedMap {
  private final InitialStates initialStates;
  private final Size size;

  public GeneratedMap(Size size, InitialStates initialStates) {
    this.initialStates = initialStates;
    this.size = size;
  }

  public InitialStates getInitialStates() {
    return initialStates;
  }

  public Size getSize() {
    return size;
  }
}
