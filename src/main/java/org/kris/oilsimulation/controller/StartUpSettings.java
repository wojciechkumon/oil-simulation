package org.kris.oilsimulation.controller;

import org.kris.oilsimulation.model.CellCoords;
import org.kris.oilsimulation.model.CellState;
import org.kris.oilsimulation.model.ExternalConditions;
import org.kris.oilsimulation.model.OilCellState;
import org.kris.oilsimulation.model.OilParticle;
import org.kris.oilsimulation.model.OilSimulationConstants;
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
    Vector current = new Vector(1.2, 1.2);
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

  public Map<CellCoords, CellState> getInitialCellStates() {
    int middleHeight = size.getHeight() / 2;
    int middleWidth = size.getWidth() / 2;
    Map<CellCoords, CellState> initialStates = new HashMap<>();
    initialStates.put(newCellCoords(middleHeight - 1, middleWidth - 1), getStartingParticles(140, constants));
    initialStates.put(newCellCoords(middleHeight - 1, middleWidth), getStartingParticles(260, constants));
    initialStates.put(newCellCoords(middleHeight, middleWidth - 1), getStartingParticles(400, constants));
    initialStates.put(newCellCoords(middleHeight, middleWidth), getStartingParticles(2600, constants));

    return initialStates;
  }

  private OilCellState getStartingParticles(int amount, OilSimulationConstants constants) {
    List<OilParticle> particles = new ArrayList<>(amount);
    for (int i = 0; i < amount; i++) {
      particles.add(constants.getStartingParticle());
    }
    return new OilCellState(particles);
  }
}
