package org.kris.oilsimulation.model.automatonview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class HistoryImpl implements History {
  private final List<GridView> history;

  HistoryImpl(List<GridView> history) {
    this.history = Collections.unmodifiableList(history);
  }

  @Override
  public History add(GridView gridView) {
    List<GridView> newHistoryList = new ArrayList<>(history.size() + 1);
    newHistoryList.addAll(history);
    newHistoryList.add(gridView);
    return new HistoryImpl(newHistoryList);
  }

  @Override
  public int size() {
    return history.size();
  }

  @Override
  public GridView get(int index) {
    return history.get(index);
  }
}
