package edu.wpi.TeamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.TeamN.MapEntity.ActionHandlingI;
import edu.wpi.TeamN.MapEntity.MapDrawing;
import edu.wpi.TeamN.MapEntity.PathFinderMap;
import edu.wpi.TeamN.services.algo.Node;
import edu.wpi.TeamN.services.database.DatabaseService;
import edu.wpi.TeamN.state.HomeState;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
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
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

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

  @FXML private AnchorPane mapAnchor;
  @FXML private Label XLabel;
  @FXML private ImageView mapImageView;
  @FXML private JFXColorPicker nodeColor;
  @FXML private JFXColorPicker EXIT;
  @FXML private JFXColorPicker ELEV;
  @FXML private JFXColorPicker STAI;
  @FXML private JFXColorPicker pathColor;
  @FXML private JFXColorPicker selectedNodeColor;
  @FXML private JFXTextField nodeSize;
  @FXML private JFXTextField pathSize;

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
    nodeSize.setText("3.5");
    pathSize.setText("2.5");
    mapController = new MapController();
    pathFinderMap = new PathFinderMap(this.db);
    mapDrawing = new MapDrawing(this);

    load();
    mapFloor();
  }

  private void updatePath() {
    StringBuilder str = new StringBuilder();
    for (String node : path) {
      if (str.toString().equals("")) str.append(node);
      else str.append(", ").append(node);
    }
    XLabel.setText(str.toString());
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
            else if (event.getButton() == MouseButton.SECONDARY && path.contains(id))
              path.remove(id);
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
      mapDrawing.colorPath(pathColor.getValue(), pathLink);
    }
  }

  public void newColorPath(ActionEvent actionEvent) {
    for (int i = 0; path.size() - 1 > i; i++) {
      ArrayList<Node.Link> pathLink = pathFinderMap.pathfind(path.get(i), path.get(i + 1));
      mapDrawing.colorPath(pathColor.getValue(), pathLink);
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
  }

  public void logOut(ActionEvent actionEvent) {}

  @Override
  public double getDownScale() {
    return downScale;
  }

  @Override
  public ImageView getMapImageView() {
    return mapImageView;
  }

  public void newColorNodeaf(ActionEvent actionEvent) {
    JFXColorPicker a = ((JFXColorPicker) actionEvent.getSource());
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
          && path.contains(mapAnchor.getChildren().get(i).getId()))
        ((Shape) ((Group) mapAnchor.getChildren().get(i)).getChildren().get(0))
            .setFill(selectedNodeColor.getValue());
    }
  }
}
