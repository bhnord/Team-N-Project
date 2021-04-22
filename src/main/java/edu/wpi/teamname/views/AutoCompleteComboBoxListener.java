package edu.wpi.teamname.views;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

public class AutoCompleteComboBoxListener implements EventHandler<KeyEvent> {

  //  private ComboBox comboBox;
  private JFXComboBox comboBox;
  private StringBuilder sb;
  private ObservableList<Label> data;
  private boolean moveCaretToPos = false;
  private int caretPos;
  private String promptText;

  public AutoCompleteComboBoxListener(final JFXComboBox<Label> comboBox) {
    this.comboBox = comboBox;
    sb = new StringBuilder();
    data = comboBox.getItems();
    promptText = comboBox.getPromptText();

    comboBox.setConverter(
        new StringConverter<Label>() {

          @Override
          public String toString(Label label) {
            return label != null ? label.getText() : "";
          }

          @Override
          public Label fromString(String string) {
            for (Object label : comboBox.getItems()) {
              if (((Label) label).getText().equals(string)) return (Label) label;
            }
            return null;
          }
        });

    this.comboBox.setEditable(true);
    this.comboBox.autosize();
    this.comboBox.setOnMousePressed(t -> comboBox.show());
    this.comboBox
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (newVal) {
                this.comboBox.show();
              } else {
                this.comboBox.hide();
              }
            });
    this.comboBox.setOnKeyReleased(AutoCompleteComboBoxListener.this);
  }

  @Override
  public void handle(KeyEvent event) {

    switch (event.getCode()) {
      case UP:
        caretPos = -1;
        moveCaret(comboBox.getEditor().getText().length());
        return;
      case DOWN:
        if (!comboBox.isShowing()) {
          comboBox.show();
        }
        caretPos = -1;
        moveCaret(comboBox.getEditor().getText().length());
        return;
      case BACK_SPACE:
      case DELETE:
        moveCaretToPos = true;
        caretPos = comboBox.getEditor().getCaretPosition();
        break;
      case ENTER:
      case ESCAPE:
        comboBox.hide();
        return;
    }

    if (event.getCode() == KeyCode.RIGHT
        || event.getCode() == KeyCode.LEFT
        || event.isControlDown()
        || event.getCode() == KeyCode.HOME
        || event.getCode() == KeyCode.END
        || event.getCode() == KeyCode.TAB
        || event.getCode() == KeyCode.SHIFT) {
      return;
    }

    ObservableList<Label> list = FXCollections.observableArrayList();
    for (int i = 0; i < data.size(); i++) {
      if (data.get(i)
          .getText()
          .toLowerCase()
          .startsWith(
              AutoCompleteComboBoxListener.this.comboBox.getEditor().getText().toLowerCase())) {
        list.add(data.get(i));
      }
    }
    String t = comboBox.getEditor().getText();

    comboBox.setItems(list);
    if (!comboBox.isLabelFloat()) {
      if (t.length() == 1) comboBox.setPromptText("");
      else if (t.length() == 0) comboBox.setPromptText(promptText);
    }
    comboBox.getEditor().setText(t);
    if (!moveCaretToPos) {
      caretPos = -1;
    }
    moveCaret(t.length());
    if (!list.isEmpty()) {
      comboBox.show();
    }
  }

  private void moveCaret(int textLength) {
    if (caretPos == -1) {
      comboBox.getEditor().positionCaret(textLength);
    } else {
      comboBox.getEditor().positionCaret(caretPos);
    }
    moveCaretToPos = false;
  }
}
