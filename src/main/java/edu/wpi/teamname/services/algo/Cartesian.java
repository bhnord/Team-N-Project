package edu.wpi.teamname.services.algo;

public class Cartesian extends Node<Cartesian> {
  public int _x, _y;

  public Cartesian(int _x, int _y) {
    super();
    this.value = this;
    this._x = _x;
    this._y = _y;
  }

  @Override
  public int heuristic(Node<Cartesian> other) {
    return (other.value._x - this._x) * (other.value._x - this._x)
        + (other.value._y - this._y) * (other.value._y - this._y);
  }
}
