package org.kris.oilsimulation.model.automatonview;

class EmptyAutomatonView implements AutomatonView {

  EmptyAutomatonView() {}

  @Override
  public GridView getGridView() {
    return GridViewFactory.getEmptyView();
  }

  @Override
  public History getHistory() {
    return HistoryFactory.getEmptyHistory();
  }
}
