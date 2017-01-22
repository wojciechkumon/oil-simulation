package org.kris.oilsimulation.model.cell;

import java.io.Serializable;
import java.util.List;

public interface OilSource extends Serializable {

  List<OilParticle> getNextParticles();

  OilSource nextState();

}
