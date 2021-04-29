package edu.wpi.TeamN.utilities;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.TeamN.services.algo.WordDistanceComputer;
import java.util.Objects;
import java.util.PriorityQueue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

public class AutoCompleteComboBoxListener implements EventHandler<KeyEvent> {
  private static class WordIntPair implements Comparable<WordIntPair> {
    int _distance;
    Label _word;

    @Override
    public int compareTo(WordIntPair o) {
      return _distance - o._distance;
    }

    public WordIntPair(int distance, Label word) {
      this._distance = distance;
      this._word = word;
    }
  }

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
    WordDistanceComputer wordDistanceComputer = new WordDistanceComputer();
    PriorityQueue<WordIntPair> words = new PriorityQueue<>();
    String word = AutoCompleteComboBoxListener.this.comboBox.getEditor().getText().toLowerCase();
    for (Label l : data) {
      words.add(
          new WordIntPair(
              wordDistanceComputer.getDistance(
                  l.getText()
                      .toLowerCase()
                      .substring(0, Math.min(word.length(), l.getText().length())),
                  word),
              l));
    }
    for (int i = 0; i < Math.min(15, words.size()); i++) {
      list.add(Objects.requireNonNull(words.poll())._word);
    }

    //    if (data.get(i)
    //            .getText()
    //            .toLowerCase()
    //            .startsWith(
    //
    // AutoCompleteComboBoxListener.this.comboBox.getEditor().getText().toLowerCase())) {
    //      list.add(data.get(i));
    String t = comboBox.getEditor().getText();

    comboBox.setItems(list);
    this.comboBox.autosize();

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
