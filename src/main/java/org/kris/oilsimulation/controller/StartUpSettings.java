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
import org.kris.oilsimulation.model.WaterCellState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.kris.oilsimulation.model.CellCoords.newCellCoords;

public class StartUpSettings {
  private static final int START_MAP_SIZE = 50;
  private static final int START_CELL_SIZE = 50;
  private static final int START_TIME_STEP = 600;
  private static final int START_DENSITY = 835;
  private static final int START_SURFACE_TENSION = 30;
  private static final int START_PARTICLE_MASS = 50;
  private static final double START_EVAPORATION_RATE = 1 - 0.99811;
  private static final int START_PROPAGATION_FACTOR = 3;
  private static final int START_MAX_LAND_PARTICLES = 1_000;

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
    Size size = new Size(START_MAP_SIZE, START_MAP_SIZE);
    ExternalConditions externalConditions = ExternalConditions.getNoInfluenceConditions();
    OilSimulationConstants constants =
        new OilSimulationConstants(START_CELL_SIZE, START_TIME_STEP, START_DENSITY,
            START_SURFACE_TENSION, START_PARTICLE_MASS, START_EVAPORATION_RATE,
            START_PROPAGATION_FACTOR, START_MAX_LAND_PARTICLES);

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
    addStartingLand(initialStates, size);

    return initialStates;
  }

  private WaterCellState getStartingWaterState(int amount, OilSimulationConstants constants) {
    return new WaterCellState(getParticles(amount, constants));
  }

  private void addStartingLand(Map<CellCoords, CellState> initialStates, Size size) {
    int length = size.getWidth() - 5;
    for (int i = 0; i <= length; i++) {
      for (int j = 0; j < i; j++) {
        initialStates.put(newCellCoords(length - i, j), LandCellState.emptyCell());
      }
    }
  }

  private List<OilParticle> getParticles(int amount, OilSimulationConstants constants) {
    return Stream
        .generate(constants::getStartingParticle)
        .limit(amount)
        .collect(Collectors.toList());
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
