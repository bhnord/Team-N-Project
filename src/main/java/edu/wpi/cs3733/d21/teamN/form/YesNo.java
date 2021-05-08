package edu.wpi.cs3733.d21.teamN.form;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class YesNo extends FormElement {
  private transient JFXComboBox<String> cb;

  public YesNo(boolean required, String question, String help, Form formin) {
    super(required, question, help, formin);
  }

  @Override
  public Node build(DatabaseService db) {
    HBox full = new HBox();
    javafx.scene.text.Text area = new javafx.scene.text.Text();
    area.setFont(Font.loadFont("resources/Fonts/Roboto/Roboto-Medium.ttf", 14));
    // todo do this with css
    area.setText(getName());
    full.getChildren().add(area);
    String[] options = {"yes", "no"};
    cb = new JFXComboBox<>();
    cb.setItems(FXCollections.observableArrayList(options));
    full.getChildren().add(cb);
    return full;
  }

  @Override
  public boolean check() {
    return cb.getSelectionModel().getSelectedItem() != null;
  }

  @Override
  public boolean validate() {
    return true;
  }

  @Override
  protected void editViewInner(JFXComboBox<ElementType> type, HBox box, JFXListView<HBox> list) {
    type.getSelectionModel().select(ElementType.YesNo);
  }
}
