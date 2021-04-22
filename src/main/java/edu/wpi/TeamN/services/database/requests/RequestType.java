package edu.wpi.TeamN.services.database.requests;

public enum RequestType {
  AUDIO_VISUAL("A/V"),
  COMPUTER_SERVICE("Computer Service"),
  EXTERNAL_PATIENT_TRANSPORTATION(" Ext. Patient Transport"),
  FLORAL("Floral"),
  FOOD_DELIVERY("Food Delivery"),
  GIFT_DELIVERY("Gift Delivery"),
  INTERNAL_PATIENT_TRANSPORTATION("Int. Patient Transport"),
  LANGUAGE_INTERPRETER("Interpreter"),
  LAUNDRY("Laundry"),
  MAINTENANCE("Maintenance"),
  MEDICINE_DELIVERY("Medicine Delivery"),
  RELIGIOUS("Religious"),
  SANITATION("Sanitation"),
  SECURITY("Security");

  String name;

  RequestType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
