package edu.wpi.TeamN.services.database.users;

public abstract class User {
  private String id, username, firstname, lastname;

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

  public abstract UserType getType();

  public void setWholeName(String firstname, String lastname) {
    this.firstname = firstname;
    this.lastname = lastname;
  }

  public String getFirstname() {
    return firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public String getWholeName() {
    return firstname + lastname;
  }
}
