package edu.wpi.cs3733.d21.teamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.*;
import edu.wpi.cs3733.d21.teamN.map.AdminMap;
import edu.wpi.cs3733.d21.teamN.map.DirectionHandler;
import edu.wpi.cs3733.d21.teamN.map.MapDrawer;
import edu.wpi.cs3733.d21.teamN.services.algo.Node;
import edu.wpi.cs3733.d21.teamN.services.algo.PathFinder;
import edu.wpi.cs3733.d21.teamN.services.database.users.User;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class PathFinderController extends MapController implements Initializable {
  ArrayList<String> path = new ArrayList<>();
  ArrayList<Node.Link> nodePath = new ArrayList<>();
  DirectionHandler directionHandler;
  private Scene appPrimaryScene;
  private ArrayList<Circle> extras;
  private ArrayList<Double> percents;
  private double len;

  public ArrayList<Node.Link> getNodePath() {
    return nodePath;
  }

  @FXML private JFXListView<HBox> texutualDescription;
  @FXML private JFXListView<HBox> stops;
  @FXML private JFXComboBox<Label> locationDropdown;

  @FXML private Group ControlsGroup;
  @FXML private JFXButton buttonUp, buttonDown;

  @Inject
  public void setAppPrimaryScene(Scene appPrimaryScene) {
    this.appPrimaryScene = appPrimaryScene;
  }

  @Inject
  public void setLoader(FXMLLoader loader) {
    this.loader = loader;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    if (db.getCurrentUser().getUsername().equals("guest")) {
      super.sideBarSetup(anchorPane, appPrimaryScene, loader, "Login Map");
    } else {
      super.sideBarSetup(anchorPane, appPrimaryScene, loader, "Map");
    }
    super.init(appPrimaryScene);
    extras = new ArrayList<>();

    adminMap = new AdminMap(db);
    mapDrawer = new MapDrawer(this);
    directionHandler = new DirectionHandler(this, stops, texutualDescription, locationDropdown);
    mapImageView.setCursor(Cursor.CROSSHAIR);
    this.Load();
    mapDrawer.setUpZoom(mapImageView, mapAnchor);

    texutualDescription = new JFXListView<>();
    for (Node n : getNodeSet().values()) {
      n.get_shape().setVisible(false);
      for (Node.Link l : n.get_neighbors()) {
        l._shape.setVisible(false);
      }
    }
    mapImageView.setOnMouseClicked(this::handleClick);
    mapImageView.setOnMouseDragged(event -> mapDrawer.captureMouseDrag(event));
    if (!db.getCurrentUser().getUsername().equals("guest")) {
      updateStyle(db.getCurrentUser().getAppColor());
    }
    percents = new ArrayList<>();
    AnimationTimer a = new MyTimer();
    a.start();
  }

  @FXML JFXButton reverse, b1, b2, b3, L2, L1, g, F1, F2, F3;
  @FXML Rectangle rectangle1, rectangle2, rectangle3, rectangle4, rectangle5;
  @FXML Label l1, l2, l3, l4, l5, l6;
  @FXML TextArea tArea;

  public void updateStyle(String color) {
    if (!db.getCurrentUser().getDarkMode()) {
      Color appC = Color.web(color);
      String s = appC.darker().darker().desaturate().toString();
      String style =
          "-fx-background-color: " + "#" + s.substring(2) + "; -fx-background-radius: 15;";

      JFXButton[] lA = {reverse, L2, L1, g, b1, b2, b3, F1, F2, F3};
      for (JFXButton a : lA) a.setStyle(style);
    } else {
      Color appC = Color.web(color);
      String s = appC.darker().darker().desaturate().toString();
      String style =
          "-fx-background-color: " + "#" + s.substring(2) + "; -fx-background-radius: 15;";
      Rectangle[] rA = {rectangle1, rectangle2, rectangle3, rectangle4, rectangle5};
      for (Rectangle a : rA) a.setFill(appC.darker().darker().desaturate());
      style = "-fx-background-color: " + "#" + color.substring(2) + "; -fx-background-radius: 15;";

      JFXButton[] lA = {reverse, L2, L1, g, b1, b2, b3, F1, F2, F3};
      for (JFXButton a : lA) a.setStyle(style);

      Label[] labelA = {l1, l2, l3, l4, l5, l6};
      for (Label a : labelA) a.setTextFill(Color.web("WHITE"));
      buttonDown.setTextFill(Color.web("WHITE"));
      buttonUp.setTextFill(Color.web("WHITE"));

      tArea.setStyle(
          "-fx-text-fill: WHITE; -fx-control-inner-background:"
              + "#"
              + s.substring(2)
              + ";-fx-text-box-border: transparent;-fx-focus-color: transparent;");

      JFXTextField[] tA = {nodeSize, pathSize};
      for (JFXTextField a : tA) {
        a.setFocusColor(Color.web("WHITE"));
        a.setUnFocusColor(Color.web("WHITE"));
        a.setStyle("-fx-text-inner-color: WHITE;");
      }
      locationDropdown.setFocusColor(Color.web("WHITE"));
      locationDropdown.setUnFocusColor(Color.web("WHITE"));
      locationDropdown.setStyle("-fx-text-fill: WHITE;");
    }
  }

  public void handleClick(MouseEvent event) {
    Node n = adminMap.get(event.getX(), event.getY(), mapDrawer.getCurrentMap());
    if (event.getButton() == MouseButton.PRIMARY
        && !path.contains(n.get_nodeID())
        && !event.isShiftDown()) {
      if (n.get_longName().toLowerCase().contains("park")) {
        User.setParkingSpot(n.get_longName());
        //        System.out.println("set parking spot");
        // Label set here Michael
      }
      //      for (Node.Link l : n.get_neighbors()) {
      //        System.out.println("\t" + l._other.get_nodeID());
      //      }
      updatePath();
      nodeSelected(selectedNodeColor);
      directionHandler.addStop(n);
      n.get_shape().setVisible(true);
    } else if (event.getButton() == MouseButton.SECONDARY) path.remove(n.get_nodeID());
  }

  public void updatePath() {
    StringBuilder str = new StringBuilder();
    for (String node : path) {
      if (str.toString().equals("")) str.append(node);
      else str.append(", ").append(node);
    }
  }

  @FXML
  private void dragMouse(MouseEvent mouseEvent) {
    //    this.mapDrawer.captureMouseDrag(mouseEvent);
  }

  public void newColorPath(ActionEvent actionEvent) {
    updateUserColors(pathColor.getId(), pathColor.getValue().toString());
    directionHandler.clean();
    nodePath.clear();
    for (int i = 0; path.size() - 1 > i; i++) {
      ArrayList<Node.Link> pathLink =
          this.pathfind(getNodeSet().get(path.get(i)), getNodeSet().get(path.get(i + 1)));
      pathLink.addAll(nodePath);
      nodePath = pathLink;
      mapDrawer.colorPath(pathColor.getValue(), pathLink);
      PathFinder p = PathFinder.getPathFinder();
      ArrayList<String> s = p.getDescription(pathLink);
      directionHandler.addDirection(path.get(0));
      for (String l : s) {
        directionHandler.addDirection(l);
      }
    }
    //    arrowChase();
    createExtras();
  }

  public void mapFloor() {
    createExtras();
    for (String s : path) {
      Node n = getNodeSet().get(s);
      n.get_shape().setVisible(n.get_floor().equals(mapDrawer.getCurrentMap()));
    }
    for (Node.Link link : nodePath) {
      correctFloor(link);
    }
  }

  private void clearExtras() {
    for (javafx.scene.Node n : extras) {
      mapAnchor.getChildren().remove(n);
    }
    percents.clear();
    extras.clear();
  }

  private Polygon createArrow(Node.Link l) {
    double size = 20;
    double angle = .78;
    Polygon p = new Polygon();
    double dx = l._other.get_x() - l._this.get_x();
    double dy = l._other.get_y() - l._this.get_y();
    double mag = Math.sqrt(dx * dx + dy * dy);
    dx /= mag;
    dy /= mag;
    dx *= size;
    dy *= size;

    p.getPoints().add((l._this.get_x() + dx / 2) * getDownScale());
    p.getPoints().add((l._this.get_y() + dy / 2) * getDownScale());
    double x1 = Math.cos(angle) * dx - Math.sin(angle) * dy;
    double y1 = Math.sin(angle) * dx + Math.cos(angle) * dy;

    double x2 = Math.cos(-angle) * dx - Math.sin(-angle) * dy;
    double y2 = Math.sin(-angle) * dx + Math.cos(-angle) * dy;

    p.getPoints().add((l._this.get_x() - x1 / 2.1) * getDownScale());
    p.getPoints().add((l._this.get_y() - y1 / 2.1) * getDownScale());
    p.getPoints().add((l._this.get_x() - x2 / 2.1) * getDownScale());
    p.getPoints().add((l._this.get_y() - y2 / 2.1) * getDownScale());
    p.setFill(Color.RED);
    return p;
  }

  private void createExtras() {
    this.len = PathFinder.getPathFinder().getTotalLen(nodePath);
    clearExtras();
    for (double i = 0; i < 1; i += .02) {
      double[] d = PathFinder.getPathFinder().getParametric(i, nodePath, mapDrawer.getCurrentMap());
      System.out.println(d[0] + ", " + d[1]);
      Circle c = new Circle(d[0] * getDownScale(), d[1] * getDownScale(), 1);
      mapAnchor.getChildren().add(c);
      c.setVisible(d[2] == 1.0);
      extras.add(c);
      percents.add(i);
    }
    //    for (Node.Link l : nodePath) {
    //      if (l._this.get_floor().equals(mapDrawer.getCurrentMap()))
    //        if (l._this.get_floor().equals(l._other.get_floor())) {
    //          Polygon p = createArrow(l);
    //          mapAnchor.getChildren().add(p);
    //          extras.add(p);
    //        } else {
    //          Label label = new Label(l._other.get_floor());
    //          label.setTranslateX(l._other.get_x() * getDownScale() + 10);
    //          label.setTranslateY(l._other.get_y() * getDownScale() - 10);
    //          mapAnchor.getChildren().add(label);
    //          extras.add(label);
    //        }
    //    }
  }

  private void arrowChase() {
    for (Node.Link l : nodePath) {
      Image i =
          new Image(
              Objects.requireNonNull(
                  getClass().getClassLoader().getResourceAsStream("gifs/Arrow1.gif")));
      ImageView imageView = new ImageView(i);
      //      imageView.setFitWidth(500);
      //      imageView.setFitHeight(500);
      //      System.out.println(
      //          l._this.get_x() * getDownScale() + " : " + l._this.get_y() * getDownScale());
      imageView.setX(l._this.get_x() * getDownScale());
      imageView.setY(l._this.get_y() * getDownScale());
      //            imageView.setLayoutX(l._this.get_x() * getDownScale());
      //            imageView.setLayoutY(l._this.get_y() * getDownScale());

      imageView.setRotate(getDirection(l));

      mapAnchor.getChildren().add(imageView);
    }
  }

  private double getDirection(Node.Link l) {
    double dx = l._other.get_x() * getDownScale() - l._this.get_x() * getDownScale();
    double dy = l._other.get_y() * getDownScale() - l._this.get_y() * getDownScale();
    double mag = Math.sqrt(dx);

    return dx;
  }

  public void nodeSelected(JFXColorPicker a) {
    for (Node n : getNodeSet().values()) {
      if (path.contains(n.get_nodeID())) {
        n.get_shape().setFill(a.getValue());
        //        directionHandler.addStop(n);
      }
    }
  }

  public void clearSelection(ActionEvent actionEvent) {
    clearExtras();
    //    reset();
    for (String s : path) {
      Node n = getNodeSet().get(s);
      n.get_shape().setVisible(false);
    }
    for (Node.Link l : nodePath) {
      l._this.get_shape().setFill(Color.web(getUserPrefs().getBasicNodeColor()));
      l._other.get_shape().setFill(Color.web(getUserPrefs().getBasicNodeColor()));
      l._shape.setStroke(Color.BLACK);
      l._shape.setVisible(false);
    }
    path.clear();
    directionHandler.cleanAll();
    nodePath.clear();
  }

  public ArrayList<String> getPath() {
    return path;
  }

  public void newSize(ActionEvent actionEvent) {
    double nodeValue = Double.parseDouble(nodeSize.getText());
    double edgeValue = Double.parseDouble(pathSize.getText());
    if (((JFXTextField) actionEvent.getSource()).getId().equals("nodeSize")
        && nodeValue <= 5
        && nodeValue >= 3) {
      userPrefs.setNodeSize(nodeValue);
      for (int i = 1; mapAnchor.getChildren().size() - 1 > i; i++) {
        if (this.getNodeSet().containsKey(mapAnchor.getChildren().get(i).getId())) {
          ((Circle) ((Group) mapAnchor.getChildren().get(i)).getChildren().get(0))
              .setRadius(nodeValue);
        }
      }
    } else if (((JFXTextField) actionEvent.getSource()).getId().equals("pathSize")
        && nodeValue <= 5
        && nodeValue >= 3) {
      userPrefs.setEdgeWidth(edgeValue);
      for (int i = 1; mapAnchor.getChildren().size() - 1 > i; i++) {
        if (this.getEdgeSet().containsKey(mapAnchor.getChildren().get(i).getId())) {
          ((Line) ((Group) mapAnchor.getChildren().get(i)).getChildren().get(0))
              .setStrokeWidth(edgeValue);
        }
      }
    }
    db.updateUserPrefs(Integer.parseInt(String.valueOf(db.getCurrentUser().getId())), userPrefs);
  }

  public void mousePress(MouseEvent mouseEvent) {
    super.mouseClick(mouseEvent);
  }

  public void addStop(ActionEvent actionEvent) {
    directionHandler.addStopClick(locationDropdown.getEditor().getText());
  }

  @FXML
  public void groupUp() {
    // accountSettingsGroup.setTranslateX(100);
    TranslateTransition tt = new TranslateTransition();
    tt.setNode(ControlsGroup);
    tt.setDuration(new Duration(1000));
    // tt.setFromX(0);
    tt.setToY(-250);
    tt.setAutoReverse(true);
    buttonDown.setVisible(true);
    buttonUp.setVisible(false);
    tt.play();
  }

  @FXML
  public void groupDown() {
    TranslateTransition tt = new TranslateTransition();
    tt.setNode(ControlsGroup);
    tt.setDuration(new Duration(1000));
    // tt.setFromX(100);
    tt.setToY(0);
    tt.setAutoReverse(true);
    buttonDown.setVisible(false);
    buttonUp.setVisible(true);
    tt.play();
  }

  public void goBack(ActionEvent actionEvent) {
    directionHandler.reverse();
    newColorPath(actionEvent);
  }

  private class MyTimer extends AnimationTimer {

    long prev = 0;

    @Override
    public void handle(long now) {
      if (prev == 0) {
        prev = now;
        return;
      }
      doHandle((double) (now - prev) / 10000000);
      prev = now;
    }

    private void doHandle(double dt) {
      for (int i = 0; i < percents.size(); i++) {
        percents.set(i, (percents.get(i) + 1 / len * dt) % 1);
        double[] d =
            PathFinder.getPathFinder()
                .getParametric(percents.get(i), nodePath, mapDrawer.getCurrentMap());
        extras.get(i).setCenterX(d[0] * getDownScale());
        extras.get(i).setCenterY(d[1] * getDownScale());
        extras.get(i).setVisible(d[2] == 1.0);
      }
    }
  }
}
