package org.kris.oilsimulation.model;

import java.util.Map;
import java.util.Random;

public class OilAutomaton extends AbstractAutomaton {
  private final ExternalConditions externalConditions;
  private final OilSimulationConstants constants;
  private final Calculators calculators;
  private final Map<CellCoords, OilSource> sources;

  private OilAutomaton(Size size, ExternalConditions externalConditions,
                       OilSimulationConstants constants, Calculators calculators,
                       Map<CellCoords, OilSource> sources) {
    super(size);
    this.externalConditions = externalConditions;
    this.constants = constants;
    this.calculators = calculators;
    this.sources = sources;

    for (int i = 0; i < size.getHeight(); i++) {
      for (int j = 0; j < size.getWidth(); j++) {
        grid.set(i, j, OilCellState.emptyCell());
      }
    }
  }

  public static OilAutomaton newAutomaton(Size size, ExternalConditions externalConditions,
                                          OilSimulationConstants constants) {
    return newAutomaton(size, externalConditions, constants, InitialStates.emptyStates());
  }

  public static OilAutomaton newAutomaton(Size size, ExternalConditions externalConditions,
                                          OilSimulationConstants constants,
                                          InitialStates initialStates) {
    Random random = new Random();
    Calculators calculators = new Calculators(new SpreadingCalculator(random),
        new AdvectionCalculator(random), new OilSourcesCalculator());
    OilAutomaton automaton = new OilAutomaton(size, externalConditions,
        constants, calculators, initialStates.getInitialSources());

    initialStates.getInitialCellStates().entrySet().forEach(
        entry -> {
          CellCoords coords = entry.getKey();
          CellState state = entry.getValue();
          automaton.grid.set(coords.getRow(), coords.getCol(), state);
        }
    );

    return automaton;
  }

  @Override
  public Automaton nextState() {
    return produceNextState(externalConditions);
  }

  @Override
  public Automaton nextState(NextStateSettings stateSettings) {
    OilAutomatonNextSettings settings = (OilAutomatonNextSettings) stateSettings;
    return produceNextState(settings.getExternalConditions());
  }

  private Automaton produceNextState(ExternalConditions newExternalConditions) {
    OilAutomaton newAutomaton = new OilAutomaton(size, newExternalConditions,
        constants, calculators, getSourcesNextState());
    setNewAutomatonGridState(newAutomaton);
    return newAutomaton;
  }

  private Map<CellCoords, OilSource> getSourcesNextState() {
    return calculators.getOilSourcesCalculator().getSourcesNextState(sources);
  }

  private void setNewAutomatonGridState(OilAutomaton newAutomaton) {
    AutomatonGrid tmpGrid = new AutomatonGrid(size);
    grid.copyTo(tmpGrid);

    calculators.getSpreadingCalculator().apply(tmpGrid, constants);

    calculators.getAdvectionCalculator().apply(tmpGrid, newAutomaton.grid,
        externalConditions, constants.getCellSize());

    calculators.getOilSourcesCalculator().apply(newAutomaton.grid, sources);
  }

  // TODO remove after implementation (to test in debugger)
  private int countParticles() {
    int sum = 0;
    for (int i = 0; i < size.getHeight(); i++) {
      for (int j = 0; j < size.getWidth(); j++) {
        OilCellState cellState = (OilCellState) grid.get(i, j);
        sum += cellState.getOilParticles().size();
      }
    }
    return sum;
  }

}
