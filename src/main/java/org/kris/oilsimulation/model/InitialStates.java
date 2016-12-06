package org.kris.oilsimulation.model;

import java.util.Collections;
import java.util.Map;

public class InitialStates {
  private final Map<CellCoords, ? extends CellState> initialCellStates;
  private final Map<CellCoords, OilSource> initialSources;

  public InitialStates(Map<CellCoords, ? extends CellState> initialCellStates,
                       Map<CellCoords, OilSource> initialSources) {
    this.initialCellStates = initialCellStates;
    this.initialSources = initialSources;
  }

  public Map<CellCoords, ? extends CellState> getInitialCellStates() {
    return initialCellStates;
  }

  public Map<CellCoords, OilSource> getInitialSources() {
    return initialSources;
  }

  public static InitialStates emptyStates() {
    return new InitialStates(Collections.emptyMap(), Collections.emptyMap());
  }
}
