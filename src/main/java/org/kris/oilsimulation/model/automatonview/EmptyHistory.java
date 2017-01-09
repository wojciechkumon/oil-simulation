package org.kris.oilsimulation.model.automatonview;

import java.util.Collections;

class EmptyHistory implements History {

  EmptyHistory() {}

  @Override
  public History add(GridView gridView) {
    return new HistoryImpl(Collections.singletonList(gridView));
  }

  @Override
  public int size() {
    return 0;
  }

  @Override
  public GridView get(int index) {
    throw new UnsupportedOperationException();
  }
}
