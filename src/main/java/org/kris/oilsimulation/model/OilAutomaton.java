package org.kris.oilsimulation.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OilAutomaton extends AbstractAutomaton {
  private final ExternalConditions externalConditions;
  private final OilSimulationConstants constants;
  private final Calculators calculators;

  private OilAutomaton(Size size, ExternalConditions externalConditions,
                       OilSimulationConstants constants, Calculators calculators) {
    super(size);
    this.externalConditions = externalConditions;
    this.constants = constants;
    this.calculators = calculators;

    for (int i = 0; i < size.getHeight(); i++) {
      for (int j = 0; j < size.getWidth(); j++) {
        grid.set(i, j, OilCellState.emptyCell());
      }
    }
  }

  public static OilAutomaton newAutomaton(Size size, ExternalConditions externalConditions,
                                          OilSimulationConstants constants) {
    Random random = new Random();
    Calculators calculators = new Calculators(new SpreadingCalculator(random), new AdvectionCalculator(random));
    OilAutomaton automaton = new OilAutomaton(size, externalConditions, constants, calculators);
    int middleHeight = size.getHeight() / 2;
    int middleWidth = size.getWidth() / 2;

    automaton.grid.set(middleHeight - 1, middleWidth - 1, getStartingParticles(40, constants));
    automaton.grid.set(middleHeight - 1, middleWidth, getStartingParticles(60, constants));
    automaton.grid.set(middleHeight, middleWidth - 1, getStartingParticles(100, constants));
    automaton.grid.set(middleHeight, middleWidth, getStartingParticles(200, constants));
    return automaton;
  }

  private static OilCellState getStartingParticles(int amount, OilSimulationConstants constants) {
    List<OilParticle> particles = new ArrayList<>(amount);
    for (int i = 0; i < amount; i++) {
      particles.add(constants.getStartingParticle());
    }
    return new OilCellState(particles);
  }

  @Override
  public Automaton nextState() {
    OilAutomaton newAutomaton = new OilAutomaton(size, externalConditions, constants, calculators);
    runCalculators(newAutomaton);
    return newAutomaton;
  }

  private void runCalculators(OilAutomaton newAutomaton) {
    AutomatonGrid tmpGrid = new AutomatonGrid(size);
    grid.copyTo(tmpGrid);

    calculators.getSpreadingCalculator().apply(tmpGrid, constants);

    calculators.getAdvectionCalculator().apply(tmpGrid, newAutomaton.grid,
        externalConditions, constants.getCellSize());
  }

}
