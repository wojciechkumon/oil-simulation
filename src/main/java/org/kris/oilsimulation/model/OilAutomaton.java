package org.kris.oilsimulation.model;

import java.util.ArrayList;
import java.util.List;

public class OilAutomaton extends AbstractAutomaton {
  private final ExternalConditions externalConditions;
  private final OilSimulationConstants constants;

  private OilAutomaton(Size size, ExternalConditions externalConditions,
                       OilSimulationConstants constants) {
    super(size);
    this.externalConditions = externalConditions;
    this.constants = constants;

    for (int i = 0; i < size.getHeight(); i++) {
      for (int j = 0; j < size.getWidth(); j++) {
        grid.set(i, j, OilCellState.emptyCell());
      }
    }
  }

  public static OilAutomaton newAutomaton(Size size, ExternalConditions externalConditions,
                                          OilSimulationConstants constants) {
    OilAutomaton automaton = new OilAutomaton(size, externalConditions, constants);
    int middleHeight = size.getHeight() / 2;
    int middleWidth = size.getWidth() / 2;

    automaton.grid.set(middleHeight - 1, middleWidth - 1, getStartingPartiles(40, constants));
    automaton.grid.set(middleHeight - 1, middleWidth, getStartingPartiles(60, constants));
    automaton.grid.set(middleHeight, middleWidth - 1, getStartingPartiles(100, constants));
    automaton.grid.set(middleHeight, middleWidth, getStartingPartiles(200, constants));
    return automaton;
  }

  private static OilCellState getStartingPartiles(int amount, OilSimulationConstants constants) {
    List<OilParticle> particles = new ArrayList<>(amount);
    for (int i = 0; i < amount; i++) {
      particles.add(constants.getStartingParticle());
    }
    return new OilCellState(particles);
  }

  @Override
  public Automaton nextState() {
    OilAutomaton newAutomaton = new OilAutomaton(size, externalConditions, constants);
    runCalculators(newAutomaton);
    return newAutomaton;
  }

  private void runCalculators(OilAutomaton newAutomaton) {
    SpredingCalculator.apply(grid, newAutomaton.grid);

    AdvectionCalculator.apply(grid, newAutomaton.grid, externalConditions);
  }

}
