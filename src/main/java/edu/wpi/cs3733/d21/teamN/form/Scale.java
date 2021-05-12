package edu.wpi.cs3733.d21.teamN.form;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXRadioButton;
import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Scale extends FormElement {
  int max;
  int min;
  private transient ToggleGroup group;

  public Scale(boolean required, String question, String help, Form formin, int min, int max) {
    super(required, question, help, formin);
    this.min = min;
    this.max = max;
  }

  @Override
  public Node build(DatabaseService db) {
    if (max < min) {
      int temp = max;
      max = min;
      min = temp;
    }
    VBox full = new VBox();
    full.setSpacing(15);
    Text title = new Text(getName());
    title.setFont(Font.loadFont("resources/Fonts/Roboto/Roboto-Medium.ttf", 14));
    // todo do this with css
    full.getChildren().add(title);
    HBox buttons = new HBox();
    buttons.setSpacing(15);
    group = new ToggleGroup();
    for (int i = min; i <= max; i++) {
      JFXRadioButton radioButton = new JFXRadioButton();
      radioButton.setText(Integer.toString(i));
      radioButton.setToggleGroup(group);
      radioButton.setUserData(i);
      buttons.getChildren().add(radioButton);
    }
    group
        .selectedToggleProperty()
        .addListener(
            (ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) -> {
              if (group.getSelectedToggle() != null) {
                setValue(group.getSelectedToggle().getUserData().toString());
              }
            });
    full.getChildren().add(buttons);
    return full;
  }

  @Override
  public boolean check() {
    return !getValue().isEmpty();
  }

  @Override
  public boolean validate() {
    return true;
  }

  @Override
  protected void editViewInner(JFXComboBox<ElementType> type, HBox box, JFXListView<HBox> list) {
    type.getSelectionModel().select(ElementType.Scale);
    Integer[] values = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    JFXComboBox<Integer> mini = new JFXComboBox<>();
    mini.setPromptText("min");
    JFXComboBox<Integer> maxi = new JFXComboBox<>();
    maxi.setPromptText("max");
    mini.setItems(FXCollections.observableArrayList(values));
    maxi.setItems(FXCollections.observableArrayList(values));
    mini.setOnAction(e -> this.min = mini.getSelectionModel().getSelectedItem());
    maxi.setOnAction(e -> this.max = maxi.getSelectionModel().getSelectedItem());
    box.getChildren().add(mini);
    box.getChildren().add(maxi);
  }
}
