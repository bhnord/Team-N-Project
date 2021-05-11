package edu.wpi.cs3733.d21.teamN.form;

import com.jfoenix.controls.JFXListView;
import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import edu.wpi.cs3733.d21.teamN.views.FormController;
import java.io.Serializable;
import java.util.ArrayList;
import javafx.scene.layout.HBox;

public class Form implements Serializable {
  ArrayList<FormElement> elements;
  private String title;

  public boolean isRequest() {
    return request;
  }

  public void setRequest(boolean request) {
    this.request = request;
  }

  private boolean request;

  public void tester() {
    System.out.println("SUCCESSFULLY DESERIALIZED");
  }

  public Form(String title) {
    this.title = title;
    this.elements = new ArrayList<>();
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
  public void build(FormController formController, DatabaseService db) {
    formController.getTitle().setText(title);
    for (FormElement element : elements) {
      formController.getElements().getChildren().add(element.build(db));
    }
  }

  public void editorBuild(JFXListView<HBox> list) {
    for (FormElement element : elements) {
      list.getItems().add(element.editView(list));
    }
  }

  public void add(JFXListView<HBox> list) {
    elements.add(new TextField(false, "", "", this));
    list.getItems().add(elements.get(elements.size() - 1).editView(list));
  }

  public void remove(JFXListView<HBox> list, int index) {
    elements.remove(index);
    list.getItems().remove(index);
  }

  public ArrayList<String> getResults() {
    ArrayList<String> out = new ArrayList<>();
    for (FormElement element : elements) {
      if (element.getValue() != null) out.add(element.getValue());
    }
    return out;
  }

  public ArrayList<String> getNames() {
    ArrayList<String> out = new ArrayList<>();
    for (FormElement element : elements) {
      if (element.getValue() != null) out.add(element.getName());
    }
    return out;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
