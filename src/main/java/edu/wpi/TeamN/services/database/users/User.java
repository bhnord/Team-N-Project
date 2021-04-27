package edu.wpi.TeamN.services.database.users;

public abstract class User {
  private String id, username, firstname, lastname;
  private UserPrefs userPrefs;

  public User(String id, String username, UserPrefs userPrefs) {
    this.id = id;
    this.username = username;
    this.userPrefs = userPrefs;
  }

  public String getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public abstract UserType getType();

  public void setWholeName(String firstname, String lastname) {
    this.firstname = firstname;
    this.lastname = lastname;
  }

  public String getBasicNodeColor() {
    return userPrefs.getBasicNodeColor();
  }

  public void setBasicNodeColor(String basicNodeColor) {
    userPrefs.setBasicNodeColor(basicNodeColor);
  }

  public String getExitColor() {
    return userPrefs.getExitColor();
  }

  public void setExitColor(String exitColor) {
    userPrefs.setExitColor(exitColor);
  }

  public String getElevatorColor() {
    return userPrefs.getElevatorColor();
  }

  public void setElevatorColor(String elevatorColor) {
    userPrefs.setElevatorColor(elevatorColor);
  }

  public String getStairColor() {
    return userPrefs.getStairColor();
  }

  public void setStairColor(String stairColor) {
    userPrefs.setStairColor(stairColor);
  }

  public String getPathfindingColor() {
    return userPrefs.getPathfindingColor();
  }

  public void setPathfindingColor(String pathfindingColor) {
    userPrefs.setPathfindingColor(pathfindingColor);
  }

  public double getNodeSize() {
    return userPrefs.getNodeSize();
  }

  public void setNodeSize(double nodeSize) {
    userPrefs.setNodeSize(nodeSize);
  }

  public double getEdgeWidth() {
    return userPrefs.getEdgeWidth();
  }

  public void setEdgeWidth(double edgeWidth) {
    userPrefs.setEdgeWidth(edgeWidth);
  }

  public String getHighlightColor() {
    return userPrefs.getHighlightColor();
  }

  public void setHighlightColor(String highlightColor) {
    userPrefs.setHighlightColor(highlightColor);
  }

  public String getFirstname() {
    return firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public UserPrefs getUserPrefs() {
    return userPrefs;
  }

  public String getWholeName() {
    return firstname + lastname;
  }

  @Override
  public String toString() {
    return "User{"
        + "id='"
        + id
        + '\''
        + ", username='"
        + username
        + '\''
        + ", firstname='"
        + firstname
        + '\''
        + ", lastname='"
        + lastname
        + '\''
        + ", userPrefs="
        + userPrefs
        + '}';
  }
}
