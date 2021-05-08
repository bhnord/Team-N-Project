package edu.wpi.cs3733.d21.teamN.services.database;

import edu.wpi.cs3733.d21.teamN.form.Form;

public class AppointmentType {
  private int id;
  private String type;
  private Form form;

  AppointmentType(int id, String type, Form form) {
    this.id = id;
    this.type = type;
    this.form = form;
  }

  public int getId() {
    return id;
  }

  public String getType() {
    return type;
  }

  public Form getForm() {
    return form;
  }
}
