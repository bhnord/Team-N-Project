package edu.wpi.TeamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXColorPicker;
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
import javafx.scene.shape.Line;

public class PathFinderController extends masterController
    implements Initializable, mapControllerI {
  @Inject FXMLLoader loader;
  @Inject HomeState state;
  @Inject DatabaseService db;

  private MapController mapController;
  private PathFinderMap pathFinderMap;
  private MapDrawing mapDrawing;
  private ActionHandlingI actionHandling;
  ArrayList<String> nodePath = new ArrayList<String>();

  @FXML private AnchorPane mapAnchor;
  @FXML private Label XLabel;
  @FXML private JFXColorPicker colorPicker;
  @FXML private ImageView mapImageView;

  public final double downScale = 0.25;
  public final double upScale = 4;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    mapController = new MapController();
    pathFinderMap = new PathFinderMap(this.db);
    mapDrawing = new MapDrawing(this);

    load();
    mapFloor();
  }

  private void updatePath() {
    StringBuilder str = new StringBuilder();
    for (String node : nodePath) {
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
              if (pathFinderMap
                  .getNodeSet()
                  .get(value.getStartNode())
                  .get_floor()
                  .equals(mapDrawing.getCurrentMap())) {
                placeLink(
                    key,
                    pathFinderMap.getNodeSet().get(value.getStartNode()),
                    pathFinderMap.getNodeSet().get(value.getEndNode()));
              }
            });
    pathFinderMap
        .getNodeSet()
        .forEach(
            (key, value) -> {
              if (value.get_floor().equals(mapDrawing.getCurrentMap())) {
                placeNode(key, value.get_x() * downScale, value.get_y() * downScale);
              }
            });
  }

  private void placeNode(String id, double x, double y) {
    Group root = mapDrawing.drawNode(id, x, y);
    root.setId(id);
    root.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            if (event.getButton() == MouseButton.PRIMARY && !nodePath.contains(id))
              nodePath.add(id);
            else if (event.getButton() == MouseButton.SECONDARY && nodePath.contains(id))
              nodePath.remove(id);
            updatePath();
          }
        });
    mapAnchor.getChildren().add(root);
    root.setCursor(Cursor.CROSSHAIR);
    root.setVisible(false);
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
    for (int i = 0; nodePath.size() - 1 > i; i++) {
      ArrayList<Node.Link> path = pathFinderMap.pathfind(nodePath.get(i), nodePath.get(i + 1));
      System.out.println(path);
      mapDrawing.colorPath(colorPicker.getValue(), path);
    }
  }

  public void setMap(ActionEvent actionEvent) {
    mapDrawing.setMap(((Button) actionEvent.getSource()).getId());
    this.mapFloor();
  }

  private void mapFloor() {
    for (int i = 1; mapAnchor.getChildren().size() > i; i++) {
      if (pathFinderMap.getNodeSet().containsKey(mapAnchor.getChildren().get(i).getId())) {
        if (pathFinderMap
            .getNodeSet()
            .get(mapAnchor.getChildren().get(i).getId())
            .get_floor()
            .equals(mapDrawing.getCurrentMap())) {
          mapAnchor.getChildren().get(i).setVisible(true);
        } else {
          mapAnchor.getChildren().get(i).setVisible(false);
        }
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
}
