package org.kris.oilsimulation.model.automatonview;

public interface CellView {

  double getMass();

  double getVolume();

  int getNumberOfParticles();

  boolean isWater();
}
