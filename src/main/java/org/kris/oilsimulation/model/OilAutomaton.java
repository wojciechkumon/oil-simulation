package org.kris.oilsimulation.model;

import org.kris.oilsimulation.model.automatonview.AutomatonView;
import org.kris.oilsimulation.model.automatonview.AutomatonViewFactory;
import org.kris.oilsimulation.model.automatonview.History;
import org.kris.oilsimulation.model.automatonview.HistoryFactory;
import org.kris.oilsimulation.model.calculators.AdvectionCalculator;
import org.kris.oilsimulation.model.calculators.Calculators;
import org.kris.oilsimulation.model.calculators.EvaporationCalculator;
import org.kris.oilsimulation.model.calculators.OilSourcesCalculator;
import org.kris.oilsimulation.model.calculators.SpreadingCalculator;
import org.kris.oilsimulation.model.cell.CellCoords;
import org.kris.oilsimulation.model.cell.CellState;
import org.kris.oilsimulation.model.cell.LandCellState;
import org.kris.oilsimulation.model.cell.OilSource;
import org.kris.oilsimulation.model.cell.WaterCellState;

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
        new AdvectionCalculator(random), new OilSourcesCalculator(), new EvaporationCalculator());
    OilAutomaton automaton = new OilAutomaton(size, externalConditions,
        constants, calculators, initialStates.getInitialSources());

    initialStates.getInitialCellStates().entrySet().forEach(entry -> {
      CellCoords coords = entry.getKey();
      CellState state = entry.getValue();
      automaton.grid.set(coords, state);
    });

    return automaton;
  }

  @Override
  public Automaton nextState(NextStateSettings stateSettings) {
    OilAutomatonNextSettings settings = (OilAutomatonNextSettings) stateSettings;
    return produceNextState(settings.getExternalConditions());
  }

  @Override
  public Automaton clearState() {
    OilAutomaton newAutomaton = new OilAutomaton(size, ExternalConditions.getNoInfluenceConditions(),
        constants, calculators, getSourcesNextState(), HistoryFactory.getEmptyHistory());
    copyLandToNewAutomaton(newAutomaton);
    return newAutomaton;
  }

  @Override
  public OilSimulationConstants getOilSimulationConstants() {
    return constants;
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

    calculators.evaporation().apply(tmpGrid);

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

  @Override
  protected AutomatonView createView() {
    return AutomatonViewFactory.create(grid.getGridView(), history);
  }
}
