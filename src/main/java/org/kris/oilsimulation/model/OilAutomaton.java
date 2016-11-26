package org.kris.oilsimulation.model;

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
    automaton.grid.set(middleHeight - 1, middleWidth - 1, new OilCellState(5));
    automaton.grid.set(middleHeight - 1, middleWidth, new OilCellState(25));
    automaton.grid.set(middleHeight, middleWidth - 1, new OilCellState(70));
    automaton.grid.set(middleHeight, middleWidth, new OilCellState(100));
    return automaton;
  }

  @Override
  public Automaton nextState() {
    OilAutomaton newAutomaton = new OilAutomaton(size, externalConditions, constants);

    Vector resultantVector = calculateResultantVector();
    int roundedHorizontal = (int) Math.round(resultantVector.getX());
    int roundedVertical = (int) Math.round(resultantVector.getY());
    for (int i = 0; i < size.getHeight(); i++) {
      for (int j = 0; j < size.getWidth(); j++) {
        setNewCellState(newAutomaton, roundedHorizontal, roundedVertical, i, j);
      }
    }
    return newAutomaton;
  }

  private void setNewCellState(OilAutomaton newAutomaton, int roundedHorizontal, int roundedVertical,
                               int i, int j) {
    int newI = i + roundedVertical;
    int newJ = j + roundedHorizontal;
    if (isInsideGrid(newI, newJ)) {
      newAutomaton.grid.set(newI, newJ, grid.get(i, j));
    }
  }

  private Vector calculateResultantVector() {
    Vector current = externalConditions.getCurrent().scalarMul(1.1);
    Vector wind = externalConditions.getWind().scalarMul(0.3);
    return current.add(wind);
  }

  private boolean isInsideGrid(int newI, int newJ) {
    return newI >= 0 && newI < size.getHeight()
        && newJ >= 0 && newJ < size.getWidth();
  }

}
