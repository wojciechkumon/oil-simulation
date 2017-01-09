package org.kris.oilsimulation.model.automatonview;

public class AutomatonViewFactory {

  private AutomatonViewFactory() {}

  public static AutomatonView create(GridView gridView, History currentHistory) {
    return new AutomatonViewImpl(gridView, currentHistory);
  }

  public static AutomatonView getEmptyView() {
    return Holder.EMPTY_AUTOMATON_VIEW;
  }

  private static class Holder {
    private static final EmptyAutomatonView EMPTY_AUTOMATON_VIEW = new EmptyAutomatonView();
  }
}
