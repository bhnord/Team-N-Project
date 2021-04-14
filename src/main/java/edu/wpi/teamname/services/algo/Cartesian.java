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

  public double get_x() {
    return _x;
  }

  public double get_y() {
    return _y;
  }

  public void set_x(double _x) {
    this._x = _x;
  }

  public void set_y(double _y) {
    this._y = _y;
  }
}
