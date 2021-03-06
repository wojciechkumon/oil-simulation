package org.kris.oilsimulation.model;

public class Vector {
  private static final Vector ZERO_VECTOR = new Vector(0, 0);
  private final double x;
  private final double y;

  public Vector(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public Vector add(Vector other) {
    return new Vector(x + other.x, y + other.y);
  }

  public Vector scalarMul(double scalar) {
    return new Vector(scalar * x, scalar * y);
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public static Vector zeroVector() {
    return ZERO_VECTOR;
  }
}
