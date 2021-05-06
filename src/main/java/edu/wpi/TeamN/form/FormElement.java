package edu.wpi.TeamN.form;

import javafx.scene.Node;

public abstract class FormElement {
  public abstract Node build();
  public abstract boolean check();
  private boolean required;

  public boolean is_required(){
    return required;
  }

  public FormElement(boolean is_required){
    this.required = is_required;
  }
}
