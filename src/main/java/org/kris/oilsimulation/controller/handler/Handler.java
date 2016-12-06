package org.kris.oilsimulation.controller.handler;

public interface Handler extends Runnable {

  default void clear() {}

}
