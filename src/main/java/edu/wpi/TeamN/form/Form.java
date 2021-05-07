package edu.wpi.TeamN.form;

import edu.wpi.TeamN.services.database.DatabaseService;
import edu.wpi.TeamN.views.FormController;
import java.io.Serializable;
import java.util.ArrayList;

public class Form implements Serializable {
  ArrayList<FormElement> elements;
  String title;
  DatabaseService db;

  public Form(DatabaseService db) {
    this.db = db;
    elements = new ArrayList<>();
    elements.add(new TextField(false, "question", "help"));
    elements.add(new TimePicker(false, "time", "help"));
    title = "bla";
  }

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
   * @param formController contoller where the elements of the form will be laid in
   */
  public void build(FormController formController) {
    formController.getTitle().setText(title);
    for (FormElement element : elements) {
      formController.getElements().getChildren().add(element.build(db));
    }
  }
}
