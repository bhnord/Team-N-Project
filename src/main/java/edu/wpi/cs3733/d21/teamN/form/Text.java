package edu.wpi.cs3733.d21.teamN.form;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import java.awt.*;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class Text extends FormElement {
  public Text(String text, Form formin) {
    super(false, text, "", formin);
  }

  @Override
  public Node build(DatabaseService db) {
    Label l = new Label(getName());
    l.getStylesheets().add("resources/Fonts/Roboto-Black.css");
    // todo do this with css
    return l;
  }

  @Override
  public boolean check() {
    return true;
  }

  @Override
  public boolean validate() {
    return true;
  }

  @Override
  public String getValue() {
    return null;
  }

  @Override
  protected void editViewInner(JFXComboBox<ElementType> type, HBox box, JFXListView<HBox> list) {
    type.getSelectionModel().select(ElementType.Text);
  }
}
