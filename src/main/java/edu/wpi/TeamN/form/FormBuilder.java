package edu.wpi.TeamN.form;

import java.util.ArrayList;
import javafx.scene.layout.StackPane;

public class FormBuilder {
  ArrayList<FormElement> elements;

  public void build(StackPane pane) {
    for (FormElement element : elements) {
      pane.getChildren().add(element.build());
    }
  }
}
