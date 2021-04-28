package edu.wpi.TeamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.TeamN.MapEntity.ActionHandlingI;
import edu.wpi.TeamN.MapEntity.MapDrawing;
import edu.wpi.TeamN.MapEntity.PathFinderMap;
import edu.wpi.TeamN.services.algo.Edge;
import edu.wpi.TeamN.services.algo.Node;
import edu.wpi.TeamN.services.algo.PathFinder;
import edu.wpi.TeamN.services.database.DatabaseService;
import edu.wpi.TeamN.state.HomeState;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PathFinderController extends masterController
    implements Initializable, mapControllerI {
  @Inject FXMLLoader loader;
  @Inject HomeState state;
  @Inject DatabaseService db;

  private MapController mapController;
  private PathFinderMap pathFinderMap;
  private MapDrawing mapDrawing;
  private ActionHandlingI actionHandling;
  ArrayList<String> path = new ArrayList<String>();
  ArrayList<Node.Link> nodePath = new ArrayList<Node.Link>();

  @FXML private AnchorPane mapAnchor;
  @FXML private ImageView mapImageView;
  @FXML private JFXColorPicker nodeColor;
  @FXML private JFXColorPicker EXIT;
  @FXML private JFXColorPicker ELEV;
  @FXML private JFXColorPicker STAI;
  @FXML private JFXColorPicker pathColor;
  @FXML private JFXColorPicker selectedNodeColor;
  @FXML private JFXTextField nodeSize;
  @FXML private JFXTextField pathSize;
  @FXML private JFXListView<Label> texutualDescription;

  public final double downScale = 0.25;
  public final double upScale = 4;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    nodeColor.setValue(Color.BLUE);
    EXIT.setValue(Color.RED);
    ELEV.setValue(Color.PINK);
    STAI.setValue(Color.ORANGE);
    pathColor.setValue(Color.BLACK);
    selectedNodeColor.setValue(Color.GREEN);
    nodeSize.setText("4");
    pathSize.setText("3");
    mapController = new MapController();
    pathFinderMap = new PathFinderMap(this.db);
    mapDrawing = new MapDrawing(this);

    //    texutualDescription = new JFXListView<>();
    texutualDescription.setOnMouseClicked(
        event -> {
          Label selected = texutualDescription.getSelectionModel().getSelectedItem();
          if (event.getButton() == MouseButton.PRIMARY && selected != null) {
            ObservableList<Integer> seletedI =
                texutualDescription.getSelectionModel().getSelectedIndices();
            mapDrawing.colorPath(pathColor.getValue(), nodePath);
            Node.Link link = nodePath.get(nodePath.size() - seletedI.get(0) - 1);
            mapDrawing.setMap(link._other.get_floor());
            mapFloor();
            link._shape.setStroke(Color.RED);
          }
        });
    load();
    mapFloor();
  }

  private void updatePath() {
    StringBuilder str = new StringBuilder();
    for (String node : path) {
      if (str.toString().equals("")) str.append(node);
      else str.append(", ").append(node);
    }
  }

  @FXML
  private void exit(ActionEvent actionEvent) throws IOException {
    super.cancel(actionEvent);
  }

  public void advanceViews(ActionEvent actionEvent) throws IOException {
    String file = ((Button) actionEvent.getSource()).getId() + ".fxml";
    Parent root = loader.load(getClass().getResourceAsStream(file));
    appPrimaryScene.setRoot(root);
  }

  private void load() {
    pathFinderMap
        .getEdgeSet()
        .forEach(
            (key, value) -> {
              placeLink(
                  key,
                  pathFinderMap.getNodeSet().get(value.getStartNode()),
                  pathFinderMap.getNodeSet().get(value.getEndNode()));
            });
    pathFinderMap
        .getNodeSet()
        .forEach(
            (key, value) -> {
              placeNode(key, value.get_x() * downScale, value.get_y() * downScale);
            });
  }

  private void placeNode(String id, double x, double y) {
    JFXColorPicker type = getType(id);
    Group root = mapDrawing.drawNode(id, x, y, type.getValue());
    root.setId(id);
    root.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            if (event.getButton() == MouseButton.PRIMARY && !path.contains(id)) path.add(id);
            else if (event.getButton() == MouseButton.SECONDARY) path.remove(id);
            updatePath();
            selectedNodeColor(new ActionEvent());
          }
        });
    mapAnchor.getChildren().add(root);
    root.setCursor(Cursor.CROSSHAIR);
    root.setVisible(false);
  }

  private JFXColorPicker getType(String id) {
    Node node = pathFinderMap.getNodeSet().get(id);
    if (node.get_nodeType().contains("ELEV")) {
      return ELEV;
    } else if (node.get_nodeType().contains("STAI")) {
      return STAI;
    } else if (node.get_nodeType().contains("EXIT")) {
      return EXIT;
    } else {
      return nodeColor;
    }
  }

  private void placeLink(String id, Node node1, Node node2) {
    Group root = mapDrawing.drawLine(id, node1, node2);
    root.setId(id);
    mapAnchor.getChildren().add(root);
    pathFinderMap.makeEdge(id, node1, node2, (Line) root.getChildren().get(0));
    root.setCursor(Cursor.CROSSHAIR);
    root.setVisible(false);
  }

  private double getUpScale() {
    return upScale;
  }

  public void PathFind(ActionEvent actionEvent) {
    mapDrawing.resetColors(pathFinderMap.getNodeSet());
    for (int i = 0; path.size() - 1 > i; i++) {
      ArrayList<Node.Link> pathLink = pathFinderMap.pathfind(path.get(i), path.get(i + 1));
      // newColorPath(pathColor.getValue(), pathLink);
    }
  }

  private void reset() {
    for (int i = 1; mapAnchor.getChildren().size() - 1 > i; i++) {
      if (path.contains(mapAnchor.getChildren().get(i).getId())) {
        Circle c = (Circle) ((Group) mapAnchor.getChildren().get(i)).getChildren().get(0);
        c.setRadius(Double.parseDouble(nodeSize.getText()));
      }
    }
    for (int i = 1; mapAnchor.getChildren().size() - 1 > i; i++) {
      if (pathFinderMap.getEdgeSet().containsKey(mapAnchor.getChildren().get(i).getId())) {
        mapAnchor.getChildren().get(i).setVisible(false);
      }
      newColorNode(EXIT);
      newColorNode(ELEV);
      newColorNode(STAI);
      newColorNodeaf(new ActionEvent());
      texutualDescription = new JFXListView<Label>();
      texutualDescription.setOnMouseClicked(
          event -> {
            Label selected = texutualDescription.getSelectionModel().getSelectedItem();
            if (event.getButton() == MouseButton.PRIMARY && selected != null) {
              ObservableList<Integer> seletedI =
                  texutualDescription.getSelectionModel().getSelectedIndices();
              mapDrawing.colorPath(pathColor.getValue(), nodePath);
              Node.Link link = nodePath.get(nodePath.size() - seletedI.get(0) - 1);
              mapDrawing.setMap(link._other.get_floor());
              mapFloor();
              link._shape.setStroke(Color.RED);
            }
          });
      nodePath.clear();
    }
  }

  public void newColorPath(ActionEvent actionEvent) {
    for (int i = 0; path.size() - 1 > i; i++) {
      ArrayList<Node.Link> pathLink = pathFinderMap.pathfind(path.get(i), path.get(i + 1));
      nodePath.addAll(pathLink);
      mapDrawing.colorPath(pathColor.getValue(), pathLink);
      PathFinder p = new PathFinder();
      ArrayList<String> s = p.getDescription(pathLink);
      for (String l : s) {
        texutualDescription.getItems().add(new Label(l));
      }
    }
  }

  public void newColorNode(ActionEvent actionEvent) {
    JFXColorPicker a = ((JFXColorPicker) actionEvent.getSource());
    for (int i = 1; mapAnchor.getChildren().size() - 1 > i; i++) {
      if (pathFinderMap.getNodeSet().containsKey(mapAnchor.getChildren().get(i).getId())) {
        if (pathFinderMap
            .getNodeSet()
            .get(mapAnchor.getChildren().get(i).getId())
            .get_nodeType()
            .equals(a.getId()))
          ((Shape) ((Group) mapAnchor.getChildren().get(i)).getChildren().get(0))
              .setFill(a.getValue());
      }
    }
  }

  public void newColorNode(JFXColorPicker a) {
    for (int i = 1; mapAnchor.getChildren().size() - 1 > i; i++) {
      if (pathFinderMap.getNodeSet().containsKey(mapAnchor.getChildren().get(i).getId())) {
        if (pathFinderMap
            .getNodeSet()
            .get(mapAnchor.getChildren().get(i).getId())
            .get_nodeType()
            .equals(a.getId()))
          ((Shape) ((Group) mapAnchor.getChildren().get(i)).getChildren().get(0))
              .setFill(a.getValue());
      }
    }
  }

  public void setMap(ActionEvent actionEvent) {
    mapDrawing.setMap(((Button) actionEvent.getSource()).getId());
    this.mapFloor();
  }

  private void mapFloor() {
    for (int i = 1; mapAnchor.getChildren().size() > i; i++) {
      if (pathFinderMap.getNodeSet().containsKey(mapAnchor.getChildren().get(i).getId())) {
        mapAnchor
            .getChildren()
            .get(i)
            .setVisible(
                pathFinderMap
                    .getNodeSet()
                    .get(mapAnchor.getChildren().get(i).getId())
                    .get_floor()
                    .equals(mapDrawing.getCurrentMap()));
      }
    }
    for (Node.Link link : nodePath) {
      correctFloor(link);
    }
  }

  public void correctFloor(Node.Link link) {
    if (pathFinderMap.getEdgeSet().containsKey(link._shape.getParent().getId())) {
      Edge a = pathFinderMap.getEdgeSet().get(link._shape.getParent().getId());
      if (pathFinderMap.getNodeSet().containsKey(a.getStartNode())
          || pathFinderMap.getNodeSet().containsKey(a.getEndNode())) {
        link._shape
            .getParent()
            .setVisible(
                pathFinderMap
                    .getNodeSet()
                    .get(a.getStartNode())
                    .get_floor()
                    .equals(mapDrawing.getCurrentMap()));
      }
    }
  }

  public void logOut(ActionEvent actionEvent) throws IOException {
    super.logOut(loader, appPrimaryScene);
  }

  @Override
  public double getDownScale() {
    return downScale;
  }

  @Override
  public ImageView getMapImageView() {
    return mapImageView;
  }

  @Override
  public double getNodeSize() {
    return Double.parseDouble(nodeSize.getText());
  }

  @Override
  public double getPathSize() {
    return Double.parseDouble(pathSize.getText());
  }

  public void newColorNodeaf(ActionEvent actionEvent) {
    JFXColorPicker a = nodeColor;
    for (int i = 1; mapAnchor.getChildren().size() - 1 > i; i++) {
      if (pathFinderMap.getNodeSet().containsKey(mapAnchor.getChildren().get(i).getId())) {
        if (!pathFinderMap
                .getNodeSet()
                .get(mapAnchor.getChildren().get(i).getId())
                .get_nodeType()
                .contains("ELEV")
            && !pathFinderMap
                .getNodeSet()
                .get(mapAnchor.getChildren().get(i).getId())
                .get_nodeType()
                .contains("EXIT")
            && !pathFinderMap
                .getNodeSet()
                .get(mapAnchor.getChildren().get(i).getId())
                .get_nodeType()
                .contains("STAI"))
          ((Shape) ((Group) mapAnchor.getChildren().get(i)).getChildren().get(0))
              .setFill(a.getValue());
      }
    }
  }

  public void selectedNodeColor(ActionEvent actionEvent) {
    for (int i = 1; mapAnchor.getChildren().size() - 1 > i; i++) {
      if (pathFinderMap.getNodeSet().containsKey(mapAnchor.getChildren().get(i).getId())
          && path.contains(mapAnchor.getChildren().get(i).getId())) {
        Circle l = ((Circle) ((Group) mapAnchor.getChildren().get(i)).getChildren().get(0));
        l.setFill(selectedNodeColor.getValue());
        l.setRadius(Double.parseDouble(nodeSize.getText()) + 1);
      }
    }
  }

  @FXML
  public void advanceHome() throws IOException {
    super.advanceHome(loader, appPrimaryScene);
  }

  public void clearSelection(ActionEvent actionEvent) {
    reset();
    path = new ArrayList<String>();
  }

  public void newSize(ActionEvent actionEvent) {
    double nodeValue = Double.parseDouble(nodeSize.getText());
    double edgeValue = Double.parseDouble(pathSize.getText());
    if (((JFXTextField) actionEvent.getSource()).getId().equals("nodeSize")
        && nodeValue <= 5
        && nodeValue >= 3) {
      for (int i = 1; mapAnchor.getChildren().size() - 1 > i; i++) {
        if (pathFinderMap.getNodeSet().containsKey(mapAnchor.getChildren().get(i).getId())) {
          ((Circle) ((Group) mapAnchor.getChildren().get(i)).getChildren().get(0))
              .setRadius(nodeValue);
        }
      }
    } else if (((JFXTextField) actionEvent.getSource()).getId().equals("pathSize")
        && nodeValue <= 5
        && nodeValue >= 3) {
      for (int i = 1; mapAnchor.getChildren().size() - 1 > i; i++) {
        if (pathFinderMap.getEdgeSet().containsKey(mapAnchor.getChildren().get(i).getId())) {
          ((Line) ((Group) mapAnchor.getChildren().get(i)).getChildren().get(0))
              .setStrokeWidth(edgeValue);
        }
      }
    }
  }
}
