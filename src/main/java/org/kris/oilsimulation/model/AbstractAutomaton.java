package org.kris.oilsimulation.model;

import org.kris.oilsimulation.model.automatonview.AutomatonView;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractAutomaton implements Automaton {
  protected final AutomatonGrid grid;
  protected final Size size;
  private final AtomicReference<AutomatonView> viewReference = new AtomicReference<>();

  public AbstractAutomaton(Size size) {
    this.grid = new AutomatonGrid(size);
    this.size = size;
  }

  @Override
  public AutomatonView getAutomatonView() {
    // thread safe lazy init without blocking
    AutomatonView result = viewReference.get();
    if (result == null) {
      result = createView();
      if (!viewReference.compareAndSet(null, result)) {
        return viewReference.get();
      }
    }
    return result;
  }

  protected abstract AutomatonView createView();

}
