package edu.wpi.cs3733.d21.teamN.form;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;

public class DividerLine extends FormElement {
  public DividerLine(Form formin) {
    super(false, "", "", formin);
  }

  @Override
  public Node build(DatabaseService db) {
    return new Separator();
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
  public String getValue(){
    return null;
  }

  @Override
  protected void editViewInner(JFXComboBox<ElementType> type, HBox box, JFXListView<HBox> list) {
    type.getSelectionModel().select(ElementType.Div);
  }
}
