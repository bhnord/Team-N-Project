package edu.wpi.teamname.services.algo;

import javafx.scene.shape.Shape;

public class Edge {
  private String _edgeID;
  private String _startNode;
  private String _endNode;

  public double get_distance() {
    return _distance;
  }

  public void set_distance(double _distance) {
    this._distance = _distance;
  }

  private double _distance;

  public Shape get_shape() {
    return _shape;
  }

  public void set_shape(Shape _shape) {
    this._shape = _shape;
  }

  private Shape _shape;

  public String get_edgeID() {
    return _edgeID;
  }

  public String get_startNode() {
    return _startNode;
  }

  public String get_endNode() {
    return _endNode;
  }

  public void set_edgeID(String _edgeID) {
    this._edgeID = _edgeID;
  }

  public void set_startNode(String _startNode) {
    this._startNode = _startNode;
  }

  public void set_endNode(String _endNode) {
    this._endNode = _endNode;
  }

  public Edge(String edgeID, String startNode, String endNode) {
    this._edgeID = edgeID;
    this._startNode = startNode;
    this._endNode = endNode;
  }

  @Override
  public String toString() {
    return "Edge{"
        + "_edgeID='"
        + _edgeID
        + '\''
        + ", _startNode='"
        + _startNode
        + '\''
        + ", _endNode='"
        + _endNode
        + '\''
        + '}';
  }
}
