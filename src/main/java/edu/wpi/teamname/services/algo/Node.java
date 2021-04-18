package edu.wpi.teamname.services.algo;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import lombok.NonNull;

public class Node implements Comparable<Node> {

  public double get_localGoal() {
    return _localGoal;
  }

  public void set_localGoal(double _localGoal) {
    this._localGoal = _localGoal;
  }

  public double get_globalGoal() {
    return _globalGoal;
  }

  public void set_globalGoal(double _globalGoal) {
    this._globalGoal = _globalGoal;
  }

  public boolean is_seen() {
    return _seen;
  }

  public void set_seen(boolean _seen) {
    this._seen = _seen;
  }

  public Node get_parent() {
    return _parent;
  }

  public void set_parent(Node _parent) {
    this._parent = _parent;
  }

  public List<Edge> get_neighbors() {
    return _neighbors;
  }

  public void set_neighbors(List<Edge> _neighbors) {
    this._neighbors = _neighbors;
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

  public Shape get_shape() {
    return _shape;
  }

  public void set_shape(Shape _shape) {
    this._shape = _shape;
  }

  private Shape _shape;
  private double _x, _y;
  private double _localGoal;
  private double _globalGoal;
  private boolean _seen;
  private Node _parent;
  private List<Edge> _neighbors;

  public double heuristic(Node other) {
    return Math.sqrt(
            (other._x - this._x) * (other._x - this._x)
                    + (other._y - this._y) * (other._y - this._y));
  }

  public Node() {
    _neighbors = new ArrayList<>();
    reset();
  }

  /** resets all node parameters to default */
  public void reset() {
    this._localGoal = 0;
    this._globalGoal = 0;
    this._seen = false;
    this._parent = null;
  }

  public void addNeighbor(String id, Node other, double distance) {
    this._neighbors.add(new Edge(id,this, other, distance));
  }


  @Override
  public int compareTo(@NonNull Node Other) {
    return (this._localGoal + this._globalGoal) > (Other._localGoal + Other._globalGoal) ? 1 : -1;
  }
}
