package edu.wpi.TeamN.form;

import java.io.Serializable;
import java.util.ArrayList;
import javafx.scene.layout.StackPane;

public class Form implements Serializable {
  ArrayList<FormElement> elements;

  /**
   * checks if all elements that are required fields are filled out
   *
   * @return true if the form can be submitted and false if there are elements that need to be
   *     filled out
   */
  public boolean check() {
    for (FormElement element : elements) {
      if (element.is_required() && !element.check()) {
        return false;
      }
    }
    return true;
  }

  /**
   * builds up the page based on this instance's elements
   *
   * @param pane StackPane where the elements of the form will be laid in
   */
  public void build(StackPane pane) {
    for (FormElement element : elements) {
      pane.getChildren().add(element.build());
    }
  }
}
