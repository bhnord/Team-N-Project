package edu.wpi.TeamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.TeamN.map.AdminMap;
import edu.wpi.TeamN.map.DirectionHandler;
import edu.wpi.TeamN.map.MapDrawer;
import edu.wpi.TeamN.services.algo.Node;
import edu.wpi.TeamN.services.algo.PathFinder;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class PathFinderController extends MapController implements Initializable {
  ArrayList<String> path = new ArrayList<>();
  ArrayList<Node.Link> nodePath = new ArrayList<>();
  DirectionHandler directionHandler;
  private Scene appPrimaryScene;
  private ArrayList<javafx.scene.Node> extras;

  public ArrayList<Node.Link> getNodePath() {
    return nodePath;
  }

  @FXML private JFXListView<HBox> texutualDescription;
  @FXML private JFXListView<HBox> stops;
  @FXML private JFXComboBox<Label> locationDropdown;

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
    super.init(appPrimaryScene);
    extras = new ArrayList<>();

    adminMap = new AdminMap(db);
    mapDrawer = new MapDrawer(this);
    directionHandler = new DirectionHandler(this, stops, texutualDescription, locationDropdown);
    mapImageView.setCursor(Cursor.CROSSHAIR);
    this.Load();
    mapDrawer.setUpZoom(mapImageView, mapAnchor);

    texutualDescription = new JFXListView<>();
    texutualDescription.setOnMouseClicked(
        event -> {
          HBox selected = texutualDescription.getSelectionModel().getSelectedItem();
          if (event.getButton() == MouseButton.PRIMARY && selected != null) {
            ObservableList<Integer> seletedI =
                texutualDescription.getSelectionModel().getSelectedIndices();
            mapDrawer.colorPath(super.getPathColor().getValue(), nodePath);
            Node.Link link = nodePath.get(nodePath.size() - seletedI.get(0) - 1);
            mapDrawer.setMap(link._other.get_floor());
            mapFloor();
            link._shape.setStroke(Color.RED);
          }
        });
    for (Node n : getNodeSet().values()) {
      n.get_shape().setVisible(false);
      for (Node.Link l : n.get_neighbors()) {
        l._shape.setVisible(false);
      }
    }
    mapImageView.setOnMouseClicked(this::handleClick);
  }

  public void handleClick(MouseEvent event) {
    Node n = adminMap.get(event.getX(), event.getY(), mapDrawer.getCurrentMap());
    System.out.println(n.get_nodeID() + ":");
    if (event.getButton() == MouseButton.PRIMARY && !path.contains(n.get_nodeID())) {
      for (Node.Link l : n.get_neighbors()) {
        System.out.println("\t" + l._other.get_nodeID());
      }
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
    this.mapDrawer.captureMouseDrag(mouseEvent);
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
      PathFinder p = new PathFinder();
      ArrayList<String> s = p.getDescription(pathLink);
      for (String l : s) {
        directionHandler.addDirection(l);
      }
    }
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
    clearExtras();
    for (Node.Link l : nodePath) {
      if (l._this.get_floor().equals(mapDrawer.getCurrentMap()))
        if (l._this.get_floor().equals(l._other.get_floor())) {
          Polygon p = createArrow(l);
          mapAnchor.getChildren().add(p);
          extras.add(p);
        } else {
          Label label = new Label(l._other.get_floor());
          label.setTranslateX(l._other.get_x() * getDownScale() + 10);
          label.setTranslateY(l._other.get_y() * getDownScale() - 10);
          mapAnchor.getChildren().add(label);
          extras.add(label);
        }
    }
  }

  public void nodeSelected(JFXColorPicker a) {
    for (Node n : getNodeSet().values()) {
      if (path.contains(n.get_nodeID())) {
        n.get_shape().setFill(a.getValue());
        directionHandler.addStop(n);
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
    directionHandler.clean();
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
}
