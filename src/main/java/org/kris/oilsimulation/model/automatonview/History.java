package org.kris.oilsimulation.model.automatonview;

public interface History {

  History add(GridView gridView);

  int size();

  GridView get(int index);
}
