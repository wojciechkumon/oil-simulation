package org.kris.oilsimulation.controller;

import org.kris.oilsimulation.model.CellCoords;
import org.kris.oilsimulation.model.CellState;
import org.kris.oilsimulation.model.ExternalConditions;
import org.kris.oilsimulation.model.InitialStates;
import org.kris.oilsimulation.model.OilCellState;
import org.kris.oilsimulation.model.OilParticle;
import org.kris.oilsimulation.model.OilSimulationConstants;
import org.kris.oilsimulation.model.OilSource;
import org.kris.oilsimulation.model.OilSourceImpl;
import org.kris.oilsimulation.model.Size;
import org.kris.oilsimulation.model.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.kris.oilsimulation.model.CellCoords.newCellCoords;

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
    Size size = new Size(50, 50);
    Vector current = new Vector(0, 0);
    Vector wind = new Vector(0, 0);
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

  public InitialStates getInitialStates() {
    return new InitialStates(getInitialCellStates(), getInitialSources());
  }

  private Map<CellCoords, CellState> getInitialCellStates() {
    int middleHeight = size.getHeight() / 2;
    int middleWidth = size.getWidth() / 2;
    Map<CellCoords, CellState> initialStates = new HashMap<>();
    initialStates.put(newCellCoords(middleHeight, middleWidth), getStartingState(100, constants));

    return initialStates;
  }

  private OilCellState getStartingState(int amount, OilSimulationConstants constants) {
    return new OilCellState(getParticles(amount, constants));
  }

  private List<OilParticle> getParticles(int amount, OilSimulationConstants constants) {
    List<OilParticle> particles = new ArrayList<>(amount);
    for (int i = 0; i < amount; i++) {
      particles.add(constants.getStartingParticle());
    }
    return particles;
  }

  private Map<CellCoords, OilSource> getInitialSources() {
    int middleHeight = size.getHeight() / 2;
    int middleWidth = size.getWidth() / 2;
    Map<CellCoords, OilSource> sources = new HashMap<>();
    sources.put(newCellCoords(middleHeight, middleWidth),
        new OilSourceImpl(getParticles(64_000, constants), 40));

    return sources;
  }
}
