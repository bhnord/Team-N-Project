package edu.wpi.cs3733.d21.teamN.services.database;

import edu.wpi.cs3733.d21.teamN.form.Form;

public class NamedForm {
  private int id;
  private String name;
  private Form form;

  public NamedForm(int id, String name, Form form) {
    this.form = form;
    this.name = name;
    this.id = id;
  }

  public NamedForm(String name, Form form) {
    this.name = name;
    this.form = form;
  }

  public Form getForm() {
    return form;
  }

  public String getName() {
    return name;
  }

  public int getId() {
    return id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isRequest() {
    return this.form.isRequest();
  }

  public String toString() {
    return this.name;
  }
}
