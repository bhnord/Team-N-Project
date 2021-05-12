package edu.wpi.cs3733.d21.teamN.form;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import java.io.Serializable;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

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

  private FormElement defaultInit(ElementType type) {
    switch (type) {
      case ComboBox:
        return new ComboBox(is_required(), getName(), getHelp(), formin, ComboBoxType.ROOM);
      case TextField:
        return new TextField(is_required(), getName(), getHelp(), formin);
      case TimePicker:
        return new TimePicker(is_required(), getName(), getHelp(), formin);
      case DatePicker:
        return new DatePicker(is_required(), getName(), getHelp(), formin);
      case Div:
        return new DividerLine(formin);
      case Text:
        return new Text(getName(), formin);
      case PhoneNumber:
        return new PhoneNumber(is_required(), getName(), getHelp(), formin);
      case Email:
        return new Email(is_required(), getName(), getHelp(), formin);
      case Scale:
        return new Scale(is_required(), getName(), getHelp(), formin, 1, 10);
      case YesNo:
        return new YesNo(is_required(), getName(), getHelp(), formin);
      case CheckBox:
        return new CheckBox(is_required(), getName(), getHelp(), formin, new ArrayList<>());
      case Address:
        return new Adress(is_required(), getName(), getHelp(), formin);
    }
    return null;
  }

  public HBox editView(JFXListView<HBox> list) {
    HBox box = new HBox();
    box.setSpacing(15);

    FontIcon delete = new FontIcon();
    delete.setIconLiteral("gmi-clear");
    delete.setIconSize(25);
    delete.setOnMouseClicked(
        event -> {
          list.getItems().remove(box);
          formin.elements.remove(this);
        });
    box.getChildren().add(delete);

    VBox rearange = new VBox();

    FontIcon up = new FontIcon();
    up.setIconLiteral("gmi-arrow-drop-up");
    up.setIconSize(25);
    up.setOnMouseClicked(
        event -> {
          int index = list.getItems().indexOf(box);
          if (index != 0) {
            list.getItems().set(index, list.getItems().get(index - 1));
            list.getItems().set(index - 1, box);
            formin.elements.set(index, formin.elements.get(index - 1));
            formin.elements.set(index - 1, this);
          }
        });
    rearange.getChildren().add(up);
    FontIcon down = new FontIcon();
    down.setIconLiteral("gmi-arrow-drop-down");
    down.setIconSize(25);
    down.setOnMouseClicked(
        event -> {
          int index = list.getItems().indexOf(box);
          if (index != list.getItems().size() - 1) {
            list.getItems().set(index, list.getItems().get(index + 1));
            list.getItems().set(index + 1, box);
            formin.elements.set(index, formin.elements.get(index + 1));
            formin.elements.set(index + 1, this);
          }
        });
    rearange.getChildren().add(down);

    box.getChildren().add(rearange);

    JFXComboBox<ElementType> comboBox = new JFXComboBox<>();
    comboBox.setItems(FXCollections.observableArrayList(ElementType.values()));
    comboBox.setOnAction(
        event -> {
          FormElement e = defaultInit(comboBox.getSelectionModel().getSelectedItem());
          formin.elements.set(formin.elements.indexOf(this), e);
          list.getItems().set(list.getItems().indexOf(box), e.editView(list));
        });

    box.getChildren().add(comboBox);

    if (this instanceof DividerLine) {
      this.editViewInner(comboBox, box, list);
      return box;
    }

    JFXTextField nameField = new JFXTextField();
    if (this.question.isEmpty()) {
      nameField.setPromptText("title");
    } else {
      nameField.setText(getName());
    }
    nameField.setOnKeyReleased(event -> this.question = nameField.getText());
    box.getChildren().add(nameField);

    if (this instanceof Text) {
      this.editViewInner(comboBox, box, list);
      return box;
    }

    JFXTextArea helpField = new JFXTextArea();
    if (this.help.isEmpty()) {
      helpField.setPromptText("help description");
    } else {
      helpField.setText(getHelp());
    }
    helpField.setOnKeyReleased(event -> this.help = helpField.getText());
    helpField.setMaxHeight(40);

    box.getChildren().add(helpField);

    JFXCheckBox requiredField = new JFXCheckBox();
    requiredField.setText("Is Required");
    requiredField.selectedProperty().set(is_required());
    requiredField.setOnAction(event -> this.required = requiredField.selectedProperty().get());
    box.getChildren().add(requiredField);

    this.editViewInner(comboBox, box, list);

    box.setAlignment(Pos.CENTER_LEFT);

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
