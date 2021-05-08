package edu.wpi.cs3733.d21.teamN.form;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import edu.wpi.cs3733.d21.teamN.utilities.AddressAutoComplete;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

public class Adress extends FormElement {
  private transient JFXComboBox<String> addressbox;

  public Adress(boolean required, String question, String help, Form formin) {
    super(required, question, help, formin);
  }

  @Override
  public Node build(DatabaseService db) {
    addressbox = new JFXComboBox<>();
    new AddressAutoComplete(addressbox);
    return addressbox;
  }

  @Override
  public boolean check() {
    return addressbox.getSelectionModel().getSelectedItem() != null;
  }

  @Override
  public boolean validate() {
    return addressbox.validate();
  }

  @Override
  protected void editViewInner(JFXComboBox<ElementType> type, HBox box, JFXListView<HBox> list) {
    type.getSelectionModel().select(ElementType.Address);
  }
}
