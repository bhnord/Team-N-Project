package edu.wpi.cs3733.d21.teamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d21.teamN.form.Form;
import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import edu.wpi.cs3733.d21.teamN.services.database.NamedForm;
import edu.wpi.cs3733.d21.teamN.utilities.DialogFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.kordamp.ikonli.javafx.FontIcon;

public class FormsEditorController extends MasterController implements Initializable {

  @FXML VBox elements;
  @FXML JFXListView<HBox> editor;
  @FXML Label title;
  @FXML AnchorPane anchorPane;
  @FXML JFXTextField titleEditor;
  @Inject private FXMLLoader loader;
  @Inject private DatabaseService db;
  @FXML @Inject StackPane rootStackPane;
  @FXML Rectangle darkMode;
  @FXML Label l1;
  @FXML JFXButton add;

  private ArrayList<NamedForm> forms;
  private DialogFactory dialogFactory;

  /**
   * This method allows the tests to inject the scene at a later time, since it must be done on the
   * JavaFX thread
   *
   * @param appPrimaryScene Primary scene of the app whose root will be changed
   */
  @Inject
  public void setAppPrimaryScene(Scene appPrimaryScene) {
    this.appPrimaryScene = appPrimaryScene;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.sideBarSetup(anchorPane, appPrimaryScene, loader, "Editor");
    dialogFactory = new DialogFactory(rootStackPane);
    darkMode.setVisible(db.getCurrentUser().getDarkMode());
    updateStyle(db.getCurrentUser().getAppColor());
  }

  public void updateStyle(String color) {
    Color appC = Color.web(color);
    String s = appC.darker().darker().desaturate().toString();
    String style = "-fx-background-color: " + "#" + s.substring(2) + "; -fx-background-radius: 25;";
    JFXButton[] lA = {add};
    for (JFXButton a : lA) a.setStyle(style);
    style = "-fx-background-color: " + "#" + color.substring(2) + "; -fx-background-radius: 25;";
    if (db.getCurrentUser().getDarkMode()) {
      l1.setStyle(style);
    }
    if (db.getCurrentUser().getDarkMode()) {
      titleEditor.setFocusColor(Color.web("WHITE"));
      titleEditor.setUnFocusColor(Color.web("WHITE"));
      titleEditor.setStyle("-fx-text-inner-color: WHITE;");
    } else {
      titleEditor.setFocusColor(Color.web("BLACK"));
      titleEditor.setUnFocusColor(Color.web("BLACK"));
      titleEditor.setStyle("-fx-text-inner-color: BLACK;");
    }
  }

  public void setUp() {
    editor.setMinWidth(1000);
    forms = new ArrayList<>(db.getAllForms());
    for (NamedForm f : forms) {
      editor.getItems().add(formsEditor(f));
    }
  }

  public HBox formsEditor(NamedForm form) {
    HBox ret = new HBox();
    ret.setSpacing(15);
    ret.setId(form.getForm().getTitle());

    FontIcon delete = new FontIcon();
    delete.setIconLiteral("gmi-clear");
    delete.setIconSize(25);
    delete.setOnMouseClicked(
        event -> {
          dialogFactory.creatDialogConfirmCancel(
              "Deletion Conformation",
              "Are you sure you want to delete this form? (no undo)",
              e -> {
                editor.getItems().remove(ret);
                forms.remove(form);
                db.deleteForm(form.getId());
              });
        });
    ret.getChildren().add(delete);

    FontIcon edit = new FontIcon();
    edit.setIconLiteral("gmi-edit");
    edit.setIconSize(25);
    edit.setId(form.getName());
    edit.setOnMouseClicked(
        event -> {
          Parent root = null;
          try {
            root = loader.load(getClass().getResourceAsStream("Template.fxml"));
          } catch (IOException e) {
            e.printStackTrace();
          }
          appPrimaryScene.setRoot(root);
          FormEditorController formController = loader.getController();
          for (NamedForm n : forms) {
            System.out.println(n.getForm().getNames().size());
            if (n.getName().equals(edit.getId())) {
              formController.setUp(n);
              return;
            }
          }
        });
    ret.getChildren().add(edit);

    JFXTextField title = new JFXTextField(form.getName());
    ret.getChildren().add(title);
    title.setEditable(false);

    JFXCheckBox isRequest = new JFXCheckBox();
    isRequest.setText("Is Service Request");
    isRequest.selectedProperty().set(form.getForm().isRequest());
    //    isRequest.setId();
    isRequest.setOnAction(
        e -> {
          form.getForm().setRequest(isRequest.selectedProperty().get());
          db.updateForm(form);
        });
    ret.getChildren().add(isRequest);

    return ret;
  }

  public Label getTitle() {
    return title;
  }

  public void submit(ActionEvent actionEvent) throws IOException {}

  public void add(ActionEvent actionEvent) {
    if (titleEditor.getText().isEmpty()) {
      dialogFactory.creatDialogOkay(
          "Can't Create", "Please fill out title field before creating a new Form");
      return;
    }
    for (NamedForm testForm : forms) {
      if (testForm.getName().equals(titleEditor.getText())) {
        dialogFactory.creatDialogOkay("Can't Create", "Please make name unique");
        return;
      }
    }
    Form nf = new Form(titleEditor.getText());
    NamedForm namedForm = new NamedForm(nf.getTitle(), nf);
    System.out.println(namedForm.getId());
    db.addForm(namedForm);
    namedForm = db.getFormByName(titleEditor.getText());
    forms.add(namedForm);
    editor.getItems().add(formsEditor(namedForm));
  }
}
