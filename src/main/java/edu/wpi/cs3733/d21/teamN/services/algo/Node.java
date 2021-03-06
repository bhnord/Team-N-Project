package edu.wpi.cs3733.d21.teamN.services.algo;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import lombok.NonNull;

public class Node implements Comparable<Node> {

  public static class Link {
    public String _id;
    public Node _other;
    public Node _this;
    double _distance;
    public Line _shape;
    public boolean _isStair;

    public Link(String id, Node _this, Node other, double distance, Line shape, boolean isStair) {
      this._id = id;
      this._distance = distance;
      this._other = other;
      this._shape = shape;
      this._this = _this;
      this._isStair = isStair;
    }
  }

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

  public Link get_parent() {
    return _parent;
  }

  public void set_parent(Link _parent) {
    this._parent = _parent;
  }

  public List<Link> get_neighbors() {
    return _neighbors;
  }

  public void set_neighbors(List<Link> _neighbors) {
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

  public Circle get_shape() {
    return _shape;
  }

  public void set_shape(Circle _shape) {
    this._shape = _shape;
  }

  private Circle _shape;
  private double _x, _y;

  public String get_nodeID() {
    return _nodeID;
  }

  public void set_nodeID(String _nodeID) {
    this._nodeID = _nodeID;
  }

  private String _nodeID;
  private double _localGoal;
  private double _globalGoal;
  private boolean _seen;
  private Link _parent;
  private List<Link> _neighbors;

  public String get_floor() {
    return _floor;
  }

  public void set_floor(String _floor) {
    this._floor = _floor;
  }

  public String get_building() {
    return _building;
  }

  public void set_building(String _building) {
    this._building = _building;
  }

  public String get_nodeType() {
    return _nodeType;
  }

  public void set_nodeType(String _nodeType) {
    this._nodeType = _nodeType;
  }

  public String get_longName() {
    return _longName;
  }

  public void set_longName(String _longName) {
    this._longName = _longName;
  }

  public String get_shortName() {
    return _shortName;
  }

  public void set_shortName(String _shortName) {
    this._shortName = _shortName;
  }

  private String _floor;
  private String _building;
  private String _nodeType;
  private String _longName;
  private String _shortName;

  public double heuristic(Node other) {
    return Math.sqrt(
            (other._x - this._x) * (other._x - this._x)
                + (other._y - this._y) * (other._y - this._y))
        + (!this._floor.equals(other.get_floor()) ? 3500 : 0);
  }

  public Node(double x, double y, String nodeID) {
    _neighbors = new ArrayList<>();
    reset();
    this._x = x;
    this._y = y;
    this._nodeID = nodeID;
  }

  public Node(
      double x,
      double y,
      String nodeID,
      String floor,
      String building,
      String nodeType,
      String longName,
      String shortName) {
    _neighbors = new ArrayList<>();
    reset();
    this._x = x;
    this._y = y;
    this._nodeID = nodeID;
    this._floor = floor;
    this._building = building;
    this._nodeType = nodeType;
    this._longName = longName;
    this._shortName = shortName;
  }

  /** resets all node parameters to default */
  public void reset() {
    this._localGoal = 0;
    this._globalGoal = 0;
    this._seen = false;
    this._parent = null;
  }

  public void addNeighbor(String id, Node other, double distance, Line shape, boolean isStair) {
    this._neighbors.add(new Link(id, this, other, distance, shape, this._nodeType.equals("STAI")));
  }

  public void removeNeightbor(Node other) {
    for (Link l : _neighbors) {
      if (l._other == other) {
        _neighbors.remove(l);
        break;
      }
    }
  }

  @Override
  public int compareTo(@NonNull Node Other) {
    return (this._localGoal + this._globalGoal) > (Other._localGoal + Other._globalGoal) ? 1 : -1;
  }
}
