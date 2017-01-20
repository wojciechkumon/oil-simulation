package org.kris.oilsimulation.model;

import org.kris.oilsimulation.model.automatonview.AutomatonView;

import java.util.Collection;

public interface Automaton {

  Automaton nextState(NextStateSettings stateSettings);

  AutomatonView getAutomatonView();

  Automaton clearState();

  OilSimulationConstants getOilSimulationConstants();
}
