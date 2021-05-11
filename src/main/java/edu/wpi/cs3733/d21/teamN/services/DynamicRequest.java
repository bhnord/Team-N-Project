package edu.wpi.cs3733.d21.teamN.services;

import edu.wpi.cs3733.d21.teamN.form.Form;

public class DynamicRequest {
  private int id, senderID, receiverID;
  private Form form;

  public DynamicRequest(int id, int senderID, int receiverID, Form form) {
    this.id = id;
    this.senderID = senderID;
    this.receiverID = receiverID;

    this.form = form;
  }

  public DynamicRequest(int senderID, Form form) {
    this.senderID = senderID;
    this.receiverID = -1;

    this.form = form;
  }

  public int getId() {
    return id;
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
