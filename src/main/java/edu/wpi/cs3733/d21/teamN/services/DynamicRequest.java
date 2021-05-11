package edu.wpi.cs3733.d21.teamN.services;

import edu.wpi.cs3733.d21.teamN.form.Form;

public class DynamicRequest {
  private int id, senderID, receiverID, dynamicRequestTypeId;
  private Form form;

  public DynamicRequest(int id, int dynamicRequestTypeId, int senderID, int receiverID, Form form) {
    this.id = id;
    this.dynamicRequestTypeId = dynamicRequestTypeId;
    this.senderID = senderID;
    this.receiverID = receiverID;

    this.form = form;
  }

  public DynamicRequest(int dynamicRequestTypeId, int senderID, int receiverID, Form form) {
    this.dynamicRequestTypeId = dynamicRequestTypeId;
    this.senderID = senderID;
    this.receiverID = receiverID;

    this.form = form;
  }

  public int getId() {
    return id;
  }

  public int getDynamicRequestTypeId() {
    return dynamicRequestTypeId;
  }

  public int getSenderID() {
    return senderID;
  }

  public int getReceiverID() {
    return receiverID;
  }

  public Form getForm() {
    return form;
  }
}
