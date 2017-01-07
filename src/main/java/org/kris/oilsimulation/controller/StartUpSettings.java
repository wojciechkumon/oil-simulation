package org.kris.oilsimulation.controller;

import org.kris.oilsimulation.model.CellCoords;
import org.kris.oilsimulation.model.CellState;
import org.kris.oilsimulation.model.ExternalConditions;
import org.kris.oilsimulation.model.InitialStates;
import org.kris.oilsimulation.model.LandCellState;
import org.kris.oilsimulation.model.OilParticle;
import org.kris.oilsimulation.model.OilSimulationConstants;
import org.kris.oilsimulation.model.OilSource;
import org.kris.oilsimulation.model.OilSourceImpl;
import org.kris.oilsimulation.model.Size;
import org.kris.oilsimulation.model.Vector;
import org.kris.oilsimulation.model.WaterCellState;

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
    initialStates.put(newCellCoords(middleHeight, middleWidth), getStartingWaterState(100, constants));
    addStartingLand(initialStates);

    return initialStates;
  }

  private WaterCellState getStartingWaterState(int amount, OilSimulationConstants constants) {
    return new WaterCellState(getParticles(amount, constants));
  }

  private void addStartingLand(Map<CellCoords, CellState> initialStates) {
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < i; j++) {
        initialStates.put(newCellCoords(4 - i, j), LandCellState.emptyCell());
      }
    }
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
    int sourceIterations = 1000;
    int amount = 200_000;
    sources.put(newCellCoords(middleHeight, middleWidth),
        new OilSourceImpl(getParticles(amount, constants), amount / sourceIterations));

    return sources;
  }
}
