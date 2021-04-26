package edu.wpi.TeamN.services.database.users;

public class UserPrefs {
  private String basicNodeColor,
      exitColor,
      elevatorColor,
      stairColor,
      pathfindingColor,
      highlightColor;
  private double nodeSize = 1, edgeWidth = 1;

  public UserPrefs() {}

  public UserPrefs(
      String basicNodeColor,
      String exitColor,
      String elevatorColor,
      String stairColor,
      String pathfindingColor,
      String highlightColor,
      double nodeSize,
      double edgeWidth) {
    this.basicNodeColor = basicNodeColor;
    this.exitColor = exitColor;
    this.elevatorColor = elevatorColor;
    this.stairColor = stairColor;
    this.pathfindingColor = pathfindingColor;
    this.highlightColor = highlightColor;
    this.nodeSize = nodeSize;
    this.edgeWidth = edgeWidth;
  }

  public String getBasicNodeColor() {
    return basicNodeColor;
  }

  public void setBasicNodeColor(String basicNodeColor) {
    this.basicNodeColor = basicNodeColor;
  }

  public String getExitColor() {
    return exitColor;
  }

  public void setExitColor(String exitColor) {
    this.exitColor = exitColor;
  }

  public String getElevatorColor() {
    return elevatorColor;
  }

  public void setElevatorColor(String elevatorColor) {
    this.elevatorColor = elevatorColor;
  }

  public String getStairColor() {
    return stairColor;
  }

  public void setStairColor(String stairColor) {
    this.stairColor = stairColor;
  }

  public String getPathfindingColor() {
    return pathfindingColor;
  }

  public void setPathfindingColor(String pathfindingColor) {
    this.pathfindingColor = pathfindingColor;
  }

  public String getHighlightColor() {
    return highlightColor;
  }

  public void setHighlightColor(String highlightColor) {
    this.highlightColor = highlightColor;
  }

  public double getNodeSize() {
    return nodeSize;
  }

  public void setNodeSize(double nodeSize) {
    this.nodeSize = nodeSize;
  }

  public double getEdgeWidth() {
    return edgeWidth;
  }

  public void setEdgeWidth(double edgeWidth) {
    this.edgeWidth = edgeWidth;
  }

  @Override
  public String toString() {
    return "UserPrefs{"
        + "basicNodeColor='"
        + basicNodeColor
        + '\''
        + ", exitColor='"
        + exitColor
        + '\''
        + ", elevatorColor='"
        + elevatorColor
        + '\''
        + ", stairColor='"
        + stairColor
        + '\''
        + ", pathfindingColor='"
        + pathfindingColor
        + '\''
        + ", highlightColor='"
        + highlightColor
        + '\''
        + ", nodeSize="
        + nodeSize
        + ", edgeWidth="
        + edgeWidth
        + '}';
  }
}
