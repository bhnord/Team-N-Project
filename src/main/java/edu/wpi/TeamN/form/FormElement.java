package edu.wpi.TeamN.form;

import com.jfoenix.controls.*;
import edu.wpi.TeamN.services.database.DatabaseService;
import java.io.Serializable;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

public abstract class FormElement implements Serializable {
  public abstract Node build(DatabaseService db);

  public abstract boolean check();

  public abstract boolean validate();

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  protected abstract void editViewInner(
      JFXComboBox<ElementType> type, HBox box, JFXListView<HBox> list);

  private boolean required;
  private String question;
  private String help;
  private Form formin;
  private String value = "";

  public HBox editView(JFXListView<HBox> list) {
    HBox box = new HBox();
    box.setSpacing(15);

    JFXComboBox<ElementType> comboBox = new JFXComboBox<>();
    comboBox.setItems(FXCollections.observableArrayList(ElementType.values()));
    comboBox.setOnAction(
        event -> {
          FormElement e = null;
          switch (comboBox.getValue()) {
            case ComboBox:
              e = new ComboBox(is_required(), getName(), getHelp(), formin, ComboBoxType.ROOM);
              break;
            case TextField:
              e = new TextField(is_required(), getName(), getHelp(), formin);
              break;
            case TimePicker:
              e = new TimePicker(is_required(), getName(), getHelp(), formin);
              break;
          }
          formin.elements.set(formin.elements.indexOf(this), e);
          list.getItems().set(list.getItems().indexOf(box), e.editView(list));
        });

    box.getChildren().add(comboBox);

    JFXTextField nameField = new JFXTextField();
    if (this.question.isEmpty()) {
      nameField.setPromptText("question");
    } else {
      nameField.setText(getName());
    }
    nameField.setOnKeyReleased(event -> this.question = nameField.getText());
    box.getChildren().add(nameField);

    JFXTextArea helpField = new JFXTextArea();
    if (this.help.isEmpty()) {
      helpField.setPromptText("help description");
    } else {
      helpField.setText(getHelp());
    }
    helpField.setOnKeyReleased(event -> this.help = helpField.getText());
    helpField.prefHeightProperty().bind(box.prefHeightProperty());
    box.getChildren().add(helpField);

    JFXCheckBox requiredField = new JFXCheckBox();
    requiredField.setText("Is Required");
    requiredField.selectedProperty().set(is_required());
    requiredField.setOnAction(event -> this.required = requiredField.selectedProperty().get());
    box.getChildren().add(requiredField);

    this.editViewInner(comboBox, box, list);

    JFXButton delete = new JFXButton();
    delete.setButtonType(JFXButton.ButtonType.RAISED);
    delete.setOnAction(
        event -> {
          list.getItems().remove(box);
          formin.elements.remove(this);
        });
    box.getChildren().add(delete);
    box.setAlignment(Pos.TOP_LEFT);

    return box;
  }

  public FormElement(boolean required, String question, String help, Form formin) {
    this.required = required;
    this.question = question;
    this.help = help;
    this.formin = formin;
  }

  public boolean is_required() {
    return required;
  }

  public String getName() {
    return question;
  }

  public String getHelp() {
    return help;
  }
}
