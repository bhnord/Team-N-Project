package edu.wpi.TeamN.form;

import javafx.scene.Node;

public abstract class FormElement {
  public abstract Node build();
  public abstract boolean check();
  public abstract String serialize();
  private boolean required;
  private String name;

  public FormElement(boolean required, String name){
    this.required = required;
    this.name = name;
  }

  public boolean is_required(){
    return required;
  }

  public String getName(){
    return name;
  }
}
