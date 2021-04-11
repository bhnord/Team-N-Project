package edu.wpi.teamname.services.algo;

public class Cartesian extends Node<Cartesian> {
  public double _x, _y;

  public Cartesian(double _x, double _y) {
    super();
    this.value = this;
    this._x = _x;
    this._y = _y;
  }

  @Override
  public double heuristic(Node<Cartesian> other) {
    return Math.sqrt(
        (other.value._x - this._x) * (other.value._x - this._x)
            + (other.value._y - this._y) * (other.value._y - this._y));
  }
}
