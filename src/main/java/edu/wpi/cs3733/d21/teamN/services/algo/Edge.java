package edu.wpi.cs3733.d21.teamN.services.algo;

public class Edge {
  private String _edgeID;
  private String _startNode;
  private String _endNode;

  public String getEdgeID() {
    return _edgeID;
  }

  public String getStartNode() {
    return _startNode;
  }

  public String getEndNode() {
    return _endNode;
  }

  public void setEdgeID(String _edgeID) {
    this._edgeID = _edgeID;
  }

  public void setStartNode(String _startNode) {
    this._startNode = _startNode;
  }

  public void setEndNode(String _endNode) {
    this._endNode = _endNode;
  }

  public Edge(String _edgeID, String _startNode, String _endNode) {
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
