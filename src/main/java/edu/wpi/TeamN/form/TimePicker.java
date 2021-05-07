package edu.wpi.TeamN.form;

import com.jfoenix.controls.JFXTimePicker;
import edu.wpi.TeamN.services.database.DatabaseService;
import javafx.scene.Node;

public class TimePicker extends FormElement {
  private transient JFXTimePicker timePicker;

  public TimePicker(boolean required, String question, String help) {
    super(required, question, help);
  }

  @Override
  public Node build(DatabaseService db) {
    timePicker = new JFXTimePicker();
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
  public String getValue() {
    return timePicker.getValue().toString();
  }
}
