package edu.wpi.cs3733.d21.teamN.views;

import com.google.inject.Inject;
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
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

public class FormsEditorController extends MasterController implements Initializable {

  @FXML VBox elements;
  @FXML JFXListView<HBox> editor;
  @FXML Label title;
  @FXML AnchorPane anchorPane;
  @Inject private FXMLLoader loader;
  @Inject private DatabaseService db;
  @FXML @Inject StackPane rootStackPane;

  private ArrayList<Form> forms;
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
    super.sideBarSetup(anchorPane, appPrimaryScene, loader, "Service Request");
    dialogFactory = new DialogFactory(rootStackPane);
  }

  public void setUp() {
    editor.setMinWidth(1000);
    forms = new ArrayList<>();
    for (NamedForm nf : db.getAllForms()) {
      forms.add(nf.getForm());
    }
    for (Form f : forms) {
      editor.getItems().add(formsEditor(f));
    }
  }

  public HBox formsEditor(Form form) {
    HBox ret = new HBox();
    ret.setSpacing(15);

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
              });
        });
    ret.getChildren().add(delete);

    JFXTextField title = new JFXTextField(form.getTitle());
    ret.getChildren().add(title);
    return ret;
  }

  public Label getTitle() {
    return title;
  }

  public void submit(ActionEvent actionEvent) throws IOException {
  }

  public void add(ActionEvent actionEvent) {
      Form nf = new Form();
      forms.add(nf);
      editor.getItems().add(formsEditor(nf));
  }
}
