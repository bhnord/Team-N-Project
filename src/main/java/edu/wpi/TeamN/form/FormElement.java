package edu.wpi.TeamN.form;

import javafx.scene.Node;

import java.io.Serializable;

public abstract class FormElement implements Serializable {
  public abstract Node build();

  public abstract boolean check();

  public abstract boolean validate();

  public abstract String getValue();

  private boolean required;
  private String question;

  public FormElement(boolean required, String question) {
    this.required = required;
    this.question = question;
  }

  public boolean is_required() {
    return required;
  }

  public String getName() {
    return question;
  }
}
