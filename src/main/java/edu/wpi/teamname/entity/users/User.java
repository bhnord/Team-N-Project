package edu.wpi.teamname.entity.users;

abstract class User {
  private String id, username;

  public User(String id, String username) {
    this.id = id;
    this.username = username;
  }

  public String getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public abstract String getType();
}
