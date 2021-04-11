package edu.wpi.teamname.services.algo;

public class DataNode extends Cartesian {

  private String _nodeID;
  private String _floor;
  private String _building;
  private String _nodeType;
  private String _longName;
  private String _shortName;

  public String get_nodeID() {
    return _nodeID;
  }

  public String get_floor() {
    return _floor;
  }

  public String get_building() {
    return _building;
  }

  public String get_nodeType() {
    return _nodeType;
  }

  public String get_longName() {
    return _longName;
  }

  public String get_shortName() {
    return _shortName;
  }

  public DataNode(
      double _x,
      double _y,
      String _nodeID,
      String _floor,
      String _building,
      String _nodeType,
      String _longName,
      String _shortName) {
    super(_x, _y);
    this._nodeID = _nodeID;
    this._floor = _floor;
    this._building = _building;
    this._nodeType = _nodeType;
    this._longName = _longName;
    this._shortName = _shortName;
  }

  @Override
  public String toString() {
    return "DataNode{"
        + "_x="
        + _x
        + ", _y="
        + _y
        + ", _nodeID='"
        + _nodeID
        + '\''
        + ", _floor="
        + _floor
        + ", _building='"
        + _building
        + '\''
        + ", _nodeType='"
        + _nodeType
        + '\''
        + ", _longName='"
        + _longName
        + '\''
        + ", _shortName='"
        + _shortName
        + '\''
        + '}';
  }
}
