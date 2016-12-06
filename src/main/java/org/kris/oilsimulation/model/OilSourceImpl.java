package org.kris.oilsimulation.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class OilSourceImpl implements OilSource {
  private final List<OilParticle> particlesLeft;
  private final int particlesPerStep;

  public OilSourceImpl(List<OilParticle> particlesLeft, int particlesPerStep) {
    this.particlesLeft = Collections.unmodifiableList(particlesLeft);
    this.particlesPerStep = particlesPerStep;
  }

  @Override
  public List<OilParticle> getNextParticles() {
    if (particlesLeft.isEmpty()) {
      return Collections.emptyList();
    }
    if (particlesLeft.size() <= particlesPerStep) {
      return Collections.unmodifiableList(new ArrayList<>(particlesLeft));
    }

    return getParticlesFromSource();
  }

  private List<OilParticle> getParticlesFromSource() {
    List<OilParticle> listToReturn = new ArrayList<>(particlesPerStep);
    Iterator<OilParticle> iterator = particlesLeft.iterator();
    int counter = 0;
    while (iterator.hasNext() && counter < particlesPerStep) {
      OilParticle particle = iterator.next();
      listToReturn.add(particle);
      counter++;
    }
    return Collections.unmodifiableList(listToReturn);
  }

  @Override
  public OilSource nextState() {
    if (particlesLeft.size() <= particlesPerStep) {
      return EmptyOilSource.getInstance();
    }
    return new OilSourceImpl(getParticlesLeft(), particlesPerStep);
  }

  private List<OilParticle> getParticlesLeft() {
    List<OilParticle> listToReturn = new ArrayList<>(particlesLeft);
    Iterator<OilParticle> iterator = particlesLeft.iterator();
    int counter = 0;
    while (iterator.hasNext() && counter < particlesPerStep) {
      OilParticle particle = iterator.next();
      listToReturn.remove(particle);
      counter++;
    }
    return Collections.unmodifiableList(listToReturn);
  }
}
