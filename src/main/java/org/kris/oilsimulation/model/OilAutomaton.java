package org.kris.oilsimulation.model;

import org.kris.oilsimulation.model.automatonview.AutomatonView;
import org.kris.oilsimulation.model.automatonview.AutomatonViewFactory;
import org.kris.oilsimulation.model.automatonview.History;
import org.kris.oilsimulation.model.automatonview.HistoryFactory;

import java.util.Map;
import java.util.Random;

public class OilAutomaton extends AbstractAutomaton {
  private final ExternalConditions externalConditions;
  private final OilSimulationConstants constants;
  private final Calculators calculators;
  private final Map<CellCoords, OilSource> sources;
  private final History history;

  private OilAutomaton(Size size, ExternalConditions externalConditions,
                       OilSimulationConstants constants, Calculators calculators,
                       Map<CellCoords, OilSource> sources) {
    this(size, externalConditions, constants, calculators, sources,
        HistoryFactory.getEmptyHistory());
  }

  private OilAutomaton(Size size, ExternalConditions externalConditions,
                       OilSimulationConstants constants, Calculators calculators,
                       Map<CellCoords, OilSource> sources, History history) {
    super(size);
    this.externalConditions = externalConditions;
    this.constants = constants;
    this.calculators = calculators;
    this.sources = sources;

    for (int i = 0; i < size.getHeight(); i++) {
      for (int j = 0; j < size.getWidth(); j++) {
        grid.set(i, j, WaterCellState.emptyCell());
      }
    }
    this.history = history;
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
          automaton.grid.set(coords, state);
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
    AutomatonView view = getAutomatonView();
    History newHistory = view.getHistory().add(view.getGridView());
    OilAutomaton newAutomaton = new OilAutomaton(size, newExternalConditions,
        constants, calculators, getSourcesNextState(), newHistory);
    setNewAutomatonGridState(newAutomaton);

    return newAutomaton;
  }

  private Map<CellCoords, OilSource> getSourcesNextState() {
    return calculators.oilSources().getSourcesNextState(sources);
  }

  private void setNewAutomatonGridState(OilAutomaton newAutomaton) {
    copyLandToNewAutomaton(newAutomaton);
    AutomatonGrid tmpGrid = new AutomatonGrid(size);
    grid.copyTo(tmpGrid);

    calculators.spreading().apply(tmpGrid, constants);

    calculators.advection().apply(tmpGrid, newAutomaton.grid,
        externalConditions, constants);

    calculators.oilSources().apply(newAutomaton.grid, sources);
  }

  private void copyLandToNewAutomaton(OilAutomaton newAutomaton) {
    for (int i = 0; i < size.getHeight(); i++) {
      for (int j = 0; j < size.getWidth(); j++) {
        if (!grid.get(i, j).isWater()) {
          newAutomaton.grid.set(i, j, LandCellState.emptyCell());
        }
      }
    }
  }

  // TODO remove after implementation (to test in debugger)
  private int countParticles() {
    int sum = 0;
    for (int i = 0; i < size.getHeight(); i++) {
      for (int j = 0; j < size.getWidth(); j++) {
        CellState cellState = grid.get(i, j);
        sum += cellState.getOilParticles().size();
      }
    }
    return sum;
  }

  @Override
  protected AutomatonView createView() {
    return AutomatonViewFactory.create(grid.getGridView(), history);
  }
}
