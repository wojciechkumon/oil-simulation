package org.kris.oilsimulation.model;


import javafx.beans.value.ChangeListener;

public interface Model {

  Automaton getAutomaton();

  void setAutomaton(Automaton automaton);

  void addChangeListener(ChangeListener<Automaton> listener);

  void init();

}
