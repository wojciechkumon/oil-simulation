package org.kris.oilsimulation.model.cell;

import java.util.Collections;
import java.util.List;

public class EmptyOilSource implements OilSource {
  private static final EmptyOilSource INSTANCE = new EmptyOilSource();

  private EmptyOilSource() {}

  public static EmptyOilSource getInstance() {
    return INSTANCE;
  }

  @Override
  public List<OilParticle> getNextParticles() {
    return Collections.emptyList();
  }

  @Override
  public OilSource nextState() {
    return INSTANCE;
  }
}
