package org.kris.oilsimulation.model;

import org.kris.oilsimulation.model.automatonview.AutomatonView;

import java.util.Collection;

public interface Automaton {

  Automaton nextState();

  Automaton nextState(NextStateSettings stateSettings);

  void insertStructure(Collection<Cell> structure);

  AutomatonView getAutomatonView();

  Automaton clearState();
}
