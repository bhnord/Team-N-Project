package edu.wpi.TeamN.form;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import edu.wpi.TeamN.services.database.DatabaseService;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

public class DatePicker extends FormElement {
  private transient JFXDatePicker datePicker;

  public DatePicker(boolean required, String question, String help, Form formin) {
    super(required, question, help, formin);
  }

  @Override
  public Node build(DatabaseService db) {
    datePicker = new JFXDatePicker();
    datePicker.setPromptText(getName());
    datePicker.setOnAction(e -> setValue(datePicker.getValue().toString()));
    return datePicker;
  }

  @Override
  public boolean check() {
    return !datePicker.getAccessibleText().isEmpty();
  }

  @Override
  public boolean validate() {
    return datePicker.validate();
  }

  @Override
  protected void editViewInner(JFXComboBox<ElementType> type, HBox box, JFXListView<HBox> list) {
    type.getSelectionModel().select(ElementType.DatePicker);
  }
}
