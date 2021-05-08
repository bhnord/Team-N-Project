package edu.wpi.cs3733.d21.teamN.form;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import edu.wpi.cs3733.d21.teamN.services.database.users.User;
import edu.wpi.cs3733.d21.teamN.services.database.users.UserType;
import edu.wpi.cs3733.d21.teamN.utilities.AutoCompleteComboBoxListener;
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
    this.values = new ArrayList<>();
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
    comboBox.setOnKeyPressed(
        e -> setValue(comboBox.getSelectionModel().getSelectedItem().getText()));
    ArrayList<String> innerValues = new ArrayList<>();
    switch (type) {
      case ROOM:
        for (edu.wpi.cs3733.d21.teamN.services.algo.Node n : db.getAllNodes()) {
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
          list.getItems().set(list.getItems().indexOf(box), this.editView(list));
        });
    if (this.type == ComboBoxType.CUSTOM) {
      System.out.println("custom");
      JFXTextArea area = new JFXTextArea();
      StringBuilder pt = new StringBuilder();
      values.forEach(pt::append);
      area.setPromptText(pt.toString());
      area.setOnKeyPressed(
          event -> this.values = new ArrayList<>(Arrays.asList(area.getText().split("\n"))));
      area.setMaxHeight(40);
      box.getChildren().add(area);
    }
    box.getChildren().add(comboBox);
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
