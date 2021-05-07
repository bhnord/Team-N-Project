package edu.wpi.TeamN.form;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.TeamN.services.database.DatabaseService;
import edu.wpi.TeamN.services.database.users.User;
import edu.wpi.TeamN.services.database.users.UserType;
import edu.wpi.TeamN.utilities.AutoCompleteComboBoxListener;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ComboBox extends FormElement {

  private transient JFXComboBox<Label> comboBox;
  private ComboBoxType type;
  private ArrayList<String> values;

  public ComboBox(boolean required, String question, String help, Form formin, ComboBoxType type) {
    super(required, question, help, formin);
    this.type = type;
  }

  public ComboBox(
      boolean required, String question, String help, Form formin, ArrayList<String> values) {
    super(required, question, help, formin);
    comboBox = new JFXComboBox<>();
    this.values = values;
    this.type = ComboBoxType.CUSTOM;
  }

  @Override
  public Node build(DatabaseService db) {
    comboBox = new JFXComboBox<>();
    comboBox.setPromptText(getName());
    comboBox.setOnKeyPressed(e -> setValue(comboBox.getSelectionModel().getSelectedItem().getText()));
    ArrayList<String> innerValues = new ArrayList<>();
    switch (type) {
      case ROOM:
        for (edu.wpi.TeamN.services.algo.Node n : db.getAllNodes()) {
          if (!n.get_nodeType().equals("HALL")) {
            innerValues.add(n.get_longName());
          }
        }
        break;
      case EMPLOYEE:
        for (User u : db.getUsersByType(UserType.EMPLOYEE)) {
          innerValues.add(u.getUsername());
        }
        break;
      case CUSTOM:
        innerValues = new ArrayList<>(values);
        break;
    }
    setUpComboBox(innerValues);
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
  protected void editViewInner(JFXComboBox<ElementType> type, HBox box, JFXListView<HBox> list) {
    type.getSelectionModel().select(ElementType.ComboBox);
    JFXComboBox<ComboBoxType> comboBox = new JFXComboBox<>();
    comboBox.setItems(FXCollections.observableArrayList(ComboBoxType.values()));
    comboBox.getSelectionModel().select(this.type);
    comboBox.setOnAction(
        event -> {
          this.type = comboBox.getValue();
          list.getItems().add(list.getItems().indexOf(box), this.editView(list));
        });
    if (this.type == ComboBoxType.CUSTOM) {
      JFXTextArea area = new JFXTextArea();
      StringBuilder pt = new StringBuilder();
      values.forEach(pt::append);
      area.setPromptText(pt.toString());
      area.setOnKeyPressed(
          event -> this.values = new ArrayList<>(Arrays.asList(area.getText().split("\n"))));
    }
  }

  private void setUpComboBox(ArrayList<String> list) {
    for (String s : list) {
      javafx.scene.control.Label lbl = new javafx.scene.control.Label(s);
      System.out.println(s);
      lbl.setId(s);
      comboBox.getItems().add(lbl);
    }
    new AutoCompleteComboBoxListener(comboBox);
  }
}
