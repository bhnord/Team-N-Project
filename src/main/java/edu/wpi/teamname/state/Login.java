package edu.wpi.teamname.state;

public class Login {
  private String username;
  private String password;
  private static Login login = null;

  private Login() {
    // only the login class is allowed to create login objects
  }

  public static Login getLogin() {
    if (login == null) {
      login = new Login();
      return login;
    } else return login;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
