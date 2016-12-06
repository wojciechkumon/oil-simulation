package org.kris.oilsimulation.model;

import java.util.List;

public interface OilSource {

  List<OilParticle> getNextParticles();

  OilSource nextState();

}
