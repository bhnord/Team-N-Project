package edu.wpi.cs3733.d21.teamN.form;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CheckBox extends FormElement {
  private ArrayList<String> options;
  private ArrayList<String> selected;

  public CheckBox(
      boolean required, String question, String help, Form formin, ArrayList<String> options) {
    super(required, question, help, formin);
    this.options = options;
  }

  @Override
  public Node build(DatabaseService db) {
    selected = new ArrayList<>();
    HBox full = new HBox();
    full.setSpacing(10);
    VBox working = new VBox();
    working.setSpacing(10);
    for (int i = 0; i < options.size(); i++) {
      if (working.getChildren().size() >= options.size() / 3) {
        full.getChildren().add(working);
        working = new VBox();
        working.setSpacing(10);
      }
      JFXCheckBox checkBox = new JFXCheckBox();
      checkBox.setText(options.get(i));
      checkBox.setOnAction(
          e -> {
            if (checkBox.selectedProperty().get()) {
              selected.add(checkBox.getText());
            } else {
              selected.remove(checkBox.getText());
            }
          });
      working.getChildren().add(checkBox);
    }
    if (working.getChildren().size() != 0) {
      full.getChildren().add(working);
    }
    return full;
  }

  @Override
  public boolean check() {
    return !this.selected.isEmpty();
  }

  @Override
  public boolean validate() {
    return true;
  }

  public String getValue() {
    StringBuilder stringBuilder = new StringBuilder();
    for (String s : selected) {
      stringBuilder.append(s).append("\\:");
    }
    return stringBuilder.toString();
  }

  @Override
  protected void editViewInner(JFXComboBox<ElementType> type, HBox box, JFXListView<HBox> list) {
    type.getSelectionModel().select(ElementType.CheckBox);
    JFXTextArea area = new JFXTextArea();
    StringBuilder pt = new StringBuilder();
    options.forEach(pt::append);
    area.setPromptText("comma separated list of items");
    area.setOnKeyReleased(
        event -> this.options = new ArrayList<>(Arrays.asList(area.getText().split("\\s*,\\s*"))));
    area.setMaxHeight(40);
    box.getChildren().add(area);
  }
}
