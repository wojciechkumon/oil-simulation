package org.kris.oilsimulation.model;

import java.util.Collections;
import java.util.Map;
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
    return newAutomaton(size, externalConditions, constants, Collections.emptyMap());
  }

  public static OilAutomaton newAutomaton(Size size, ExternalConditions externalConditions,
                                          OilSimulationConstants constants,
                                          Map<CellCoords, CellState> initialStates) {
    Random random = new Random();
    Calculators calculators = new Calculators(new SpreadingCalculator(random), new AdvectionCalculator(random));
    OilAutomaton automaton = new OilAutomaton(size, externalConditions, constants, calculators);

    initialStates.entrySet().forEach(
        entry -> automaton.grid.set(entry.getKey().getRow(), entry.getKey().getCol(), entry.getValue())
    );

    return automaton;
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
