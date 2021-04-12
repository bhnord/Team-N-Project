package edu.wpi.teamname.services.algo;

public class Edge {
  private String _edgeID;
  private String _startNode;
  private String _endNode;

  public String get_edgeID() {
    return _edgeID;
  }

  public String get_startNode() {
    return _startNode;
  }

  public String get_endNode() {
    return _endNode;
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
