package edu.wpi.TeamN.form;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.TeamN.services.database.DatabaseService;
import edu.wpi.TeamN.services.database.users.User;
import edu.wpi.TeamN.services.database.users.UserType;
import edu.wpi.TeamN.utilities.AutoCompleteComboBoxListener;
import javafx.scene.Node;
import javafx.scene.control.Label;

public class ComboBox extends FormElement {

  private transient JFXComboBox<Label> comboBox;
  private ComboBoxType type;
  private String[] values;

  public ComboBox(boolean required, String question, String help, ComboBoxType type) {
    super(required, question, help);
    this.type = type;
  }

  public ComboBox(boolean required, String question, String help, String[] values) {
    super(required, question, help);
    comboBox = new JFXComboBox<>();
    this.values = values;
    this.type = ComboBoxType.CUSTOM;
  }

  @Override
  public Node build(DatabaseService db) {
    comboBox = new JFXComboBox<>();
    switch (type) {
      case ROOM:
        setUpComboBox(
            (String[])
                db.getAllNodes().stream()
                    .filter(value -> !value.get_nodeType().equals("HALL"))
                    .map(edu.wpi.TeamN.services.algo.Node::get_longName)
                    .toArray());
        return comboBox;
      case EMPLOYEE:
        setUpComboBox(
            (String[])
                db.getUsersByType(UserType.EMPLOYEE).stream().map(User::getUsername).toArray());
        return comboBox;
      case CUSTOM:
        setUpComboBox(this.values);
        return comboBox;
    }
    return comboBox;
  }

  @Override
  public boolean check() {
    return comboBox.getValue() != null;
  }

  @Override
  public boolean validate() {
    return true;
  }

  @Override
  public String getValue() {
    return comboBox.getSelectionModel().getSelectedItem().getText();
  }

  private void setUpComboBox(String[] list) {
    for (String s : list) {
      javafx.scene.control.Label lbl = new javafx.scene.control.Label(s);
      lbl.setId(s);
      comboBox.getItems().add(lbl);
      new AutoCompleteComboBoxListener(comboBox);
    }
  }
}
