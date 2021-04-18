package edu.wpi.teamname.services.algo;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public class Edge {
  private String _edgeID;
  private Node _startNode;
  private Node _endNode;

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

  public Node get_startNode() {
    return _startNode;
  }

  public Node get_endNode() {
    return _endNode;
  }

  public void set_edgeID(String _edgeID) {
    this._edgeID = _edgeID;
  }

  public void set_startNode(Node _startNode) {
    this._startNode = _startNode;
  }

  public void set_endNode(Node _endNode) {
    this._endNode = _endNode;
  }

  public Edge(String _edgeID, Node _startNode, Node _endNode, double distance) {
    this._edgeID = _edgeID;
    this._startNode = _startNode;
    this._endNode = _endNode;
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
