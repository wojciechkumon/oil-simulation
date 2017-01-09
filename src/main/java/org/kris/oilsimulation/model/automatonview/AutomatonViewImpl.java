package org.kris.oilsimulation.model.automatonview;

class AutomatonViewImpl implements AutomatonView {
  private final GridView gridView;
  private final History history;

  AutomatonViewImpl(GridView gridView, History currentHistory) {
    this.gridView = gridView;
    this.history = currentHistory;
  }

  @Override
  public GridView getGridView() {
    return gridView;
  }

  @Override
  public History getHistory() {
    return history;
  }
}
