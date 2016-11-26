package org.kris.oilsimulation.model;

import org.kris.oilsimulation.controller.StartUpSettings;
import org.kris.oilsimulation.model.dummy.DummyAutomaton;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;

public class ModelImpl implements Model {
  private final ObjectProperty<Automaton> automatonProperty;
  private final StartUpSettings startUpSettings;

  public ModelImpl(StartUpSettings startUpSettings) {
    this.automatonProperty = new SimpleObjectProperty<>();
    this.startUpSettings = startUpSettings;
  }

  @Override
  public Automaton getAutomaton() {
    return automatonProperty.get();
  }

  @Override
  public void setAutomaton(Automaton automaton) {
    automatonProperty.set(automaton);
  }

  @Override
  public void addChangeListener(ChangeListener<Automaton> listener) {
    automatonProperty.addListener(listener);
  }

  @Override
  public void init() {
    automatonProperty.set(
        OilAutomaton.newAutomaton(startUpSettings.getSize(),
            startUpSettings.getExternalConditions(),
            startUpSettings.getOilSimulationConstants()));
  }

}
