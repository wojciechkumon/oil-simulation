package org.kris.oilsimulation.model;

import org.kris.oilsimulation.model.automatonview.AutomatonView;

import java.util.Collection;

public interface Automaton {

  Automaton nextState();

  void insertStructure(Collection<Cell> structure);

  AutomatonView getAutomatonView();

}
