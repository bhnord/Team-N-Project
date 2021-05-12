package edu.wpi.cs3733.d21.teamN.services.database;

public class DynamicRequestType {
  int id, formId;
  String type;

  public DynamicRequestType(int id, String type, int formId) {
    this.id = id;
    this.type = type;
    this.formId = formId;
  }

  public DynamicRequestType(String type, int formId) {
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
