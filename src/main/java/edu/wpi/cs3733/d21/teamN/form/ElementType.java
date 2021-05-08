package edu.wpi.cs3733.d21.teamN.form;

public enum ElementType {
  TextField("Text Field"),
  TimePicker("Time Picker"),
  ComboBox("Drop Down"),
  YesNo("Yes/No"),
  Scale("Scale"),
  Address("Address"),
  PhoneNumber("Phone Number"),
  Email("Email Address"),
  DatePicker("Date Picker"),
  Div("Divider Line"),
  Text("Text Body");

  private String name;

  ElementType(String n) {
    this.name = n;
  }

  @Override
  public String toString() {
    return name;
  }
}
