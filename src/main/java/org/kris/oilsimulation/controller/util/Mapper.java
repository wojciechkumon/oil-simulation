package org.kris.oilsimulation.controller.util;

import org.kris.oilsimulation.controller.maploader.LoadedMap;
import org.kris.oilsimulation.model.Size;
import org.kris.oilsimulation.model.cell.CellCoords;
import org.kris.oilsimulation.model.cell.CellState;

import java.util.HashMap;
import java.util.Map;

import static org.kris.oilsimulation.model.cell.CellCoords.newCellCoords;

public class Mapper {
  private Mapper() {}

  public static Map<CellCoords, ? extends CellState> toMap(CellState[][] cellStatesMatrix) {
    int capacity = cellStatesMatrix.length * cellStatesMatrix[0].length;
    Map<CellCoords, CellState> map = new HashMap<>(capacity);
    for (int i = 0; i < cellStatesMatrix.length; i++) {
      for (int j = 0; j < cellStatesMatrix[0].length; j++) {
        map.put(newCellCoords(j, i), cellStatesMatrix[i][j]);
      }
    }
    return map;
  }

  public static CellState[][] mapToCellStates(LoadedMap loadedMap) {
    Size size = loadedMap.getSize();
    CellState[][] newCellStates = new CellState[size.getHeight()][size.getWidth()];
    loadedMap.getInitialStates().getInitialCellStates().entrySet()
        .forEach(entry -> {
          CellCoords coords = entry.getKey();
          newCellStates[coords.getCol()][coords.getRow()] = entry.getValue();
        });
    return newCellStates;
  }
}
