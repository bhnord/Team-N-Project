package edu.wpi.TeamN.form;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTimePicker;
import edu.wpi.TeamN.services.database.DatabaseService;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

public class TimePicker extends FormElement {
  private transient JFXTimePicker timePicker;

  public TimePicker(boolean required, String question, String help, Form formin) {
    super(required, question, help, formin);
  }

  @Override
  public Node build(DatabaseService db) {
    timePicker = new JFXTimePicker();
    timePicker.setPromptText(getName());
    timePicker.setOnAction(e -> setValue(timePicker.getValue().toString()));
    return timePicker;
  }

  @Override
  public boolean check() {
    return timePicker.getValue() != null;
  }

  @Override
  public boolean validate() {
    return timePicker.validate();
  }

  @Override
  protected void editViewInner(JFXComboBox<ElementType> type, HBox box, JFXListView<HBox> list) {
    type.getSelectionModel().select(ElementType.TimePicker);
  }
}
