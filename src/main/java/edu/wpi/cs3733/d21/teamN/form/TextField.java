package edu.wpi.cs3733.d21.teamN.form;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

public class TextField extends FormElement {

  private transient JFXTextField textField;

  public TextField(boolean required, String question, String help, Form formin) {
    super(required, question, help, formin);
  }

  @Override
  public Node build(DatabaseService db) {
    textField = new JFXTextField();
    textField.setPromptText(getName());
    textField.setLabelFloat(true);
    textField.setOnKeyReleased(e -> setValue(textField.getText()));
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
  protected void editViewInner(
      JFXComboBox<ElementType> comboBox, HBox box, JFXListView<HBox> list) {
    comboBox.getSelectionModel().select(ElementType.TextField);
  }
}
