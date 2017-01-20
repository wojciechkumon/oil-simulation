package org.kris.oilsimulation.model.cell;

import java.util.List;

public interface OilSource {

  List<OilParticle> getNextParticles();

  OilSource nextState();

}
