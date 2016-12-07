package org.kris.oilsimulation.model;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class OilSourcesCalculator {

  public void apply(AutomatonGrid grid, Map<CellCoords, OilSource> sources) {
    toParticlesMap(sources)
        .entrySet()
        .forEach(entry -> addToGrid(grid, entry.getKey(), entry.getValue()));
  }

  private Map<CellCoords, List<OilParticle>> toParticlesMap(Map<CellCoords, OilSource> sources) {
    if (sources.isEmpty()) {
      return Collections.emptyMap();
    }
    return sources.entrySet()
        .stream()
        .collect(toMap(Map.Entry::getKey, this::mapToParticlesList));
  }

  private List<OilParticle> mapToParticlesList(Map.Entry<CellCoords, OilSource> entry) {
    return entry.getValue().getNextParticles();
  }

  private void addToGrid(AutomatonGrid grid, CellCoords coords, List<OilParticle> particles) {
    OilCellState oilCellState = (OilCellState) grid.get(coords.getRow(), coords.getCol());
    if (oilCellState.getOilParticles().isEmpty()) {
      grid.set(coords.getRow(), coords.getCol(), new OilCellState(particles));
    } else {
      List<OilParticle> newParticles = new ArrayList<>(oilCellState.getOilParticles());
      newParticles.addAll(particles);
      grid.set(coords.getRow(), coords.getCol(), new OilCellState(newParticles));
    }
  }

  public Map<CellCoords, OilSource> getSourcesNextState(Map<CellCoords, OilSource> sources) {
    return sources.entrySet().
        stream()
        .map(this::mapToNextSourceState)
        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  private Map.Entry<CellCoords, OilSource> mapToNextSourceState(Map.Entry<CellCoords, OilSource> entry) {
    return new AbstractMap.SimpleImmutableEntry<>(entry.getKey(), entry.getValue().nextState());
  }
}