package org.kris.oilsimulation.controller.maploader;

import org.kris.oilsimulation.model.InitialStates;
import org.kris.oilsimulation.model.Size;

import java.io.Serializable;

public class LoadedMap implements Serializable {
  private final InitialStates initialStates;
  private final Size size;

  public LoadedMap(Size size, InitialStates initialStates) {
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
