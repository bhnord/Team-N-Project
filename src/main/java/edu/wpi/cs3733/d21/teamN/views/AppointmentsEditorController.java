package edu.wpi.cs3733.d21.teamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d21.teamN.services.database.AppointmentType;
import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import edu.wpi.cs3733.d21.teamN.services.database.NamedForm;
import edu.wpi.cs3733.d21.teamN.utilities.DialogFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
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

public class AppointmentsEditorController extends MasterController implements Initializable {

  @FXML VBox elements;
  @FXML JFXListView<HBox> editor;
  @FXML Label title;
  @FXML AnchorPane anchorPane;
  @FXML JFXTextField titleEditor;
  @Inject private FXMLLoader loader;
  @Inject private DatabaseService db;
  @FXML @Inject StackPane rootStackPane;

  private ArrayList<AppointmentType> appointments;
  private ArrayList<NamedForm> forms;
  private DialogFactory dialogFactory;
  private int defaultFormID;

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
    defaultFormID = db.getAllForms().iterator().next().getId();
    editor.setMinWidth(1000);
    forms = new ArrayList<>(db.getAllForms());
    appointments = new ArrayList<>(db.getAllAppointmentTypes());
    for (AppointmentType a : appointments) {
      editor.getItems().add(formsEditor(a));
    }
  }

  public HBox formsEditor(AppointmentType appointment) {
    HBox ret = new HBox();
    ret.setSpacing(15);
    ret.setId(appointment.getType());

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
                appointments.remove(appointment);
                db.deleteForm(appointment.getId());
              });
        });
    ret.getChildren().add(delete);

    JFXTextField title = new JFXTextField(appointment.getType());
    title.setEditable(false);
    ret.getChildren().add(title);

    JFXComboBox<NamedForm> selectForm = new JFXComboBox<>();
    selectForm.setItems(FXCollections.observableArrayList(forms));
    selectForm.setOnAction(
        event -> appointment.setForm(selectForm.getSelectionModel().getSelectedItem().getId()));
    NamedForm form = db.getForm(appointment.getFormId());
    if (form != null) {
      selectForm.getSelectionModel().select(form);
    }
    ret.getChildren().add(selectForm);

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
    for (AppointmentType testForm : appointments) {
      if (testForm.getType().equals(titleEditor.getText())) {
        dialogFactory.creatDialogOkay("Can't Create", "Please make name unique");
        return;
      }
    }
    db.addAppointmentType(titleEditor.getText(), defaultFormID);
    AppointmentType appointmentType = db.getAppointmentType(defaultFormID);
    appointments.add(appointmentType);
    editor.getItems().add(formsEditor(appointmentType));
  }
}
