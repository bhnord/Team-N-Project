package edu.wpi.cs3733.d21.teamN.form;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.scene.layout.HBox;

public class PhoneNumber extends TextField {
  public PhoneNumber(boolean required, String question, String help, Form formin) {
    super(required, question, help, formin);
  }

  @Override
  public boolean validate() {
    return getValue().matches("\\d{10}");
  }

  @Override
  protected void editViewInner(
      JFXComboBox<ElementType> comboBox, HBox box, JFXListView<HBox> list) {
    comboBox.getSelectionModel().select(ElementType.PhoneNumber);
  }
}
