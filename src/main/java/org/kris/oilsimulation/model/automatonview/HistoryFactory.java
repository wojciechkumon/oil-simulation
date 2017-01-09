package org.kris.oilsimulation.model.automatonview;

import java.util.List;

public class HistoryFactory {

  private HistoryFactory() {}

  public static History create(List<GridView> history) {
    return new HistoryImpl(history);
  }

  public static History getEmptyHistory() {
    return Holder.EMPTY_HISTORY;
  }

  private static class Holder {
    private static final History EMPTY_HISTORY = new EmptyHistory();
  }
}
