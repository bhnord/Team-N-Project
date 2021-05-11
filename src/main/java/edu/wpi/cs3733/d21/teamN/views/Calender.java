package edu.wpi.cs3733.d21.teamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import edu.wpi.cs3733.d21.teamN.services.algo.Node;
import edu.wpi.cs3733.d21.teamN.services.database.Appointment;
import edu.wpi.cs3733.d21.teamN.services.database.AppointmentType;
import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import edu.wpi.cs3733.d21.teamN.services.database.users.User;
import edu.wpi.cs3733.d21.teamN.services.database.users.UserType;
import edu.wpi.cs3733.d21.teamN.utilities.AutoCompleteComboBoxListener;
import edu.wpi.cs3733.d21.teamN.utilities.DialogFactory;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.kordamp.ikonli.javafx.FontIcon;

public class Calender extends MasterController implements Initializable {

  @FXML AnchorPane anchorPane;
  @FXML Pane pain;
  @Inject private FXMLLoader loader;
  @Inject private DatabaseService db;
  @FXML @Inject StackPane rootStackPane;
  @FXML JFXListView<HBox> editor;
  @FXML JFXButton add;
  @FXML Label l1;
  @FXML Rectangle darkMode;

  private DialogFactory dialogFactory;
  private JFXButton selectedButton;
  HashSet<Appointment> appointments;
  HashSet<Appointment> appointmentsThisWeek;
  ArrayList<Appointment> appointmentSlot;
  private int defaultApp;
  private int defaultP;
  private int defaultD;
  private String defaultR;
  private ArrayList<User> staff;
  private ArrayList<Node> rooms;
  private ArrayList<AppointmentType> appointmentTypes;
  private ArrayList<User> patients;
  private ObservableList<Label> roomsL = FXCollections.observableArrayList();
  private Date currentDate;
  Timestamp currentSlot;
  Background full =
      new Background(
          new BackgroundFill(Color.color(.7, .7, .8, 1), new CornerRadii(5), Insets.EMPTY));
  Background selected =
      new Background(
          new BackgroundFill(Color.color(.9, .7, .7, 1), new CornerRadii(5), Insets.EMPTY));
  Background background =
      new Background(
          new BackgroundFill(Color.color(.9, .9, .9, 1), new CornerRadii(5), Insets.EMPTY));
  int opening = 9;
  int closing = 17;

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
    currentDate = new Date();
    super.sideBarSetup(anchorPane, appPrimaryScene, loader, "Map");
    appointments = db.getAllAppointments();
    appointmentsThisWeek = new HashSet<>();
    appointmentSlot = new ArrayList<>();
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
  }

  public void setUp() {
    patients = new ArrayList<>(db.getUsersByType(UserType.PATIENT));
    staff = new ArrayList<>(db.getUsersByType(UserType.EMPLOYEE));
    HashSet<Node> temp = db.getAllNodes();
    rooms = new ArrayList<>();
    for (Node n : temp) {
      // todo filter rooms
      if (!n.get_nodeType().equals("HALL")) {
        rooms.add(n);
        if (!n.get_longName().isEmpty()) {
          Label lbl = new Label(n.get_longName());
          lbl.setId(n.get_nodeID());
          roomsL.add(lbl);
        }
      }
    }
    roomsL.sort((label1, label2) -> label1.getText().compareToIgnoreCase(label2.getText()));
    appointmentTypes = new ArrayList<>(db.getAllAppointmentTypes());
    defaultApp = appointmentTypes.get(0).getId();
    currentDate = Calendar.getInstance().getTime();
    defaultD = staff.get(0).getId();
    defaultP = patients.get(0).getId();
    defaultR = rooms.get(0).get_nodeID();
    setUpCalander();
  }

  private void setUpCalander() {
    Date date = currentDate;
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int day = calendar.get(Calendar.DAY_OF_WEEK);
    HBox days = new HBox();
    days.setSpacing(5);
    FontIcon left = new FontIcon();
    left.setIconLiteral("gmi-keyboard-arrow-left");
    left.setIconSize(45);
    left.setOnMouseClicked(
        event -> {
          selectedButton = null;
          LocalDate localDate =
              Instant.ofEpochMilli(currentDate.getTime())
                  .atZone(ZoneId.systemDefault())
                  .toLocalDate();
          localDate = localDate.plusDays(-7);
          currentDate =
              Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
          setUpCalander();
        });
    days.getChildren().add(left);
    LocalDate localDate =
        Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    localDate = localDate.plusDays(-1 * day + 2);
    Date monday = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    Date friday =
        Date.from(localDate.atStartOfDay().plusDays(5).atZone(ZoneId.systemDefault()).toInstant());
    appointmentsThisWeek.clear();
    for (Appointment a : appointments) {
      if (a.getTimeOfAppointment().after(new Timestamp(monday.getTime()))
          && a.getTimeOfAppointment().before(new Timestamp(friday.getTime()))) {
        appointmentsThisWeek.add(a);
      }
    }

    for (int i = 0; i < 5; i++) {
      Date cur = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
      localDate = localDate.plusDays(1);
      days.getChildren().add(setUpDay(cur));
    }
    pain.getChildren().add(days);
    FontIcon right = new FontIcon();
    right.setIconLiteral("gmi-keyboard-arrow-right");
    right.setIconSize(45);
    right.setOnMouseClicked(
        event -> {
          selectedButton = null;
          LocalDate l =
              Instant.ofEpochMilli(currentDate.getTime())
                  .atZone(ZoneId.systemDefault())
                  .toLocalDate();
          l = l.plusDays(7);
          currentDate = Date.from(l.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
          setUpCalander();
        });
    days.getChildren().add(right);
  }

  private VBox setUpDay(Date date) {
    Locale locale = new Locale("en");
    DateFormat f = new SimpleDateFormat("EEEE", locale);
    String day = f.format(date) + " (" + date.toString().substring(4, 10) + ")";
    Timestamp timestamp = new Timestamp(date.getTime());
    VBox ret = new VBox();
    ret.setMinWidth(150);
    ret.setSpacing(15);
    Label l = new Label();
    l.setText(day);
    l.setMinHeight(20);
    l.setAlignment(Pos.CENTER);
    l.setBackground(background);
    l.prefWidthProperty().bind(ret.widthProperty());
    ret.getChildren().add(l);

    double curTime = opening;
    String z = "00";
    String t = "30";
    LocalDateTime ldate =
        Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();

    ldate = ldate.plusHours(9);

    while (curTime < closing) {
      String curTimeB = Integer.toString(((int) curTime));
      JFXButton c = new JFXButton();
      String ct = curTimeB + ":";
      ct += Math.floor(curTime) == curTime ? z : t;
      c.setText(ct);
      c.setAlignment(Pos.CENTER);
      c.prefWidthProperty().bind(ret.widthProperty());
      c.setMinHeight(40);
      c.setId(Timestamp.from(ldate.atZone(ZoneId.systemDefault()).toInstant()).toString());
      updateColor(c);
      c.setOnMousePressed(
          e -> {
            if (selectedButton == c) {
              return;
            }
            JFXButton prev = selectedButton;
            if (e.isSecondaryButtonDown()) {
              System.out.println("SECONDARY");
              Appointment res = appointmentSlot.get(editor.getSelectionModel().getSelectedIndex());
              appointmentsThisWeek.add(res);
              res.setTimeOfAppointment(Timestamp.valueOf(c.getId()));
              db.updateAppointment(res);
            }
            setAppointmentSlot(c.getId());
            selectedButton = c;
            selectedButton.setBackground(selected);
            updateAppointmentSlot();
            if (prev != null) updateColor(prev);
          });

      ret.getChildren().add(c);
      curTime = curTime + .5;
      ldate = ldate.plusMinutes(30);
    }

    return ret;
  }

  private void updateColor(JFXButton b) {
    Timestamp time = Timestamp.valueOf(b.getId());
    for (Appointment a : appointmentsThisWeek) {
      if (a.getTimeOfAppointment().equals(time)) {
        b.setBackground(full);
        return;
      }
    }
    b.setBackground(background);
  }

  private void updateAppointmentSlot() {
    editor.getItems().clear();
    for (Appointment a : appointmentSlot) {
      editor.getItems().add(getEditView(a));
    }
  }

  private HBox getEditView(Appointment a) {
    HBox editview = new HBox();
    editview.setSpacing(10);
    FontIcon delete = new FontIcon();
    delete.setIconLiteral("gmi-clear");
    delete.setIconSize(25);
    delete.setOnMouseClicked(
        event -> {
          editor.getItems().remove(editview);
          appointments.remove(a);
          appointmentsThisWeek.remove(a);
          appointmentSlot.remove(a);
          db.deleteAppointmnet(a.getId());
        });
    editview.getChildren().add(delete);
    JFXComboBox<Label> patientDropDown = new JFXComboBox<>();
    patientDropDown.setLabelFloat(true);
    patientDropDown.setPromptText("Patient");
    loadPatientDropdown(patientDropDown);
    editview.getChildren().add(patientDropDown);
    for (Label l : patientDropDown.getItems()) {
      if (l.getId().equals(Integer.toString(a.getPatientId()))) {
        patientDropDown.getSelectionModel().select(l);
      }
    }
    patientDropDown.setOnAction(
        e -> {
          int id = Integer.parseInt(patientDropDown.getSelectionModel().getSelectedItem().getId());
          a.setPatientId(id);
          db.updateAppointment(a);
        });

    JFXComboBox<Label> staffDropDown = new JFXComboBox<>();
    staffDropDown.setLabelFloat(true);
    staffDropDown.setPromptText("Seeing Doctor");
    loadEmployeeDropdown(staffDropDown);
    editview.getChildren().add(staffDropDown);
    for (Label l : staffDropDown.getItems()) {
      if (l.getId().equals(Integer.toString(a.getAssignedStaffId()))) {
        staffDropDown.getSelectionModel().select(l);
      }
    }
    staffDropDown.setOnAction(
        e -> {
          int id = Integer.parseInt(staffDropDown.getSelectionModel().getSelectedItem().getId());
          a.setAssignedStaffId(id);
          db.updateAppointment(a);
        });

    JFXComboBox<Label> typeDropDown = new JFXComboBox<>();
    typeDropDown.setLabelFloat(true);
    typeDropDown.setPromptText("Appointment Type");
    loadAppointmentTypeDropdown(typeDropDown);
    editview.getChildren().add(typeDropDown);
    for (Label l : typeDropDown.getItems()) {
      if (l.getId().equals(Integer.toString(a.getAppointmentTypeId()))) {
        typeDropDown.getSelectionModel().select(l);
      }
    }
    typeDropDown.setOnAction(
        e -> {
          int id = Integer.parseInt(typeDropDown.getSelectionModel().getSelectedItem().getId());
          a.setAssignedStaffId(id);
          db.updateAppointment(a);
        });

    JFXComboBox<Label> roomDropDown = new JFXComboBox<>();
    roomDropDown.setLabelFloat(true);
    roomDropDown.setPromptText("Appointment Type");
    roomDropDown.setItems(roomsL);
    new AutoCompleteComboBoxListener(roomDropDown, false);
    editview.getChildren().add(roomDropDown);
    for (Label l : roomDropDown.getItems()) {
      if (l.getId().equals(a.getAssociatedRoomId())) {
        roomDropDown.getSelectionModel().select(l);
      }
    }
    roomDropDown.setOnAction(
        e -> {
          String id = editor.getSelectionModel().getSelectedItem().getId();
          a.setAssociatedRoomId(id);
          db.updateAppointment(a);
        });
    return editview;
  }

  private void setAppointmentSlot(String encoding) {
    Timestamp time = Timestamp.valueOf(encoding);
    appointmentSlot.clear();
    this.currentSlot = time;
    for (Appointment a : appointmentsThisWeek) {
      if (a.getTimeOfAppointment().equals(time)) {
        appointmentSlot.add(a);
      }
    }
  }

  public void add(ActionEvent actionEvent) {
    if (!(selectedButton == null)) {
      Appointment appointment =
          new Appointment(defaultApp, defaultP, defaultD, null, currentSlot, "CSERV001L1");
      appointment = db.addAppointment(appointment);
      appointments.add(appointment);
      appointmentsThisWeek.add(appointment);
      appointmentSlot.add(appointment);
      editor.getItems().add(getEditView(appointment));
    }
  }
}
