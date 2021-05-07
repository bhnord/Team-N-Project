package edu.wpi.TeamN.form;

import com.jfoenix.controls.JFXTextField;
import javafx.scene.Node;

public class TextField extends FormElement {

  JFXTextField textField;

  public TextField(boolean required, String question, String help) {
    super(required, question, help);
  }

  @Override
  public Node build() {
    textField = new JFXTextField(this.getName());
    return textField;
  }

  @Override
  public boolean check() {
    return !textField.getText().isEmpty();
  }

  @Override
  public boolean validate() {
    return true;
  }

  @Override
  public String getValue() {
    return textField.getText();
  }
}
