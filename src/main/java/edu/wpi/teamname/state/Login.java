package edu.wpi.teamname.state;

import javafx.beans.property.SimpleDoubleProperty;

public class Login {
  private double username;
  private double password;

  private SimpleDoubleProperty payment;

  public Login() {
    payment = new SimpleDoubleProperty();
  }

  public double getUsername() {
    return username;
  }

  public void setUsername(double username) {
    this.username = username;
  }

  public double getPassword() {
    return password;
  }

  public void setPassword(double amount) {
    this.password = amount;
  }

  public SimpleDoubleProperty paymentProperty() {
    return payment;
  }

  public void calculatePayment() {

  }
}
