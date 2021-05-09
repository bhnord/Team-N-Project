package edu.wpi.cs3733.d21.teamN.services.database;

public class AppointmentType {
  private int id;
  private String type;
  private int formId;

  AppointmentType(int id, String type, int formId) {
    this.id = id;
    this.type = type;
    this.formId = formId;
  }

  public int getId() {
    return id;
  }

  public String getType() {
    return type;
  }

  public int getFormId() {
    return formId;
  }
}
