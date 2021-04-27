package edu.wpi.TeamN.MapEntity;

import edu.wpi.TeamN.services.algo.Node;
import edu.wpi.TeamN.views.mapControllerI;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MapDrawing {
  private final mapControllerI mapController;
  private final String[] maps = {"L1", "L2", "g", "F1", "F2", "F3"};
  private String currentMap;

  public MapDrawing(mapControllerI mapControllerI) {
    this.mapController = mapControllerI;
    currentMap = maps[0];
  }

  public Group drawLine(String id, Node node1, Node node2) {
    Line simpleNode =
        new Line(
            node1.get_x() * mapController.getDownScale(),
            node1.get_y() * mapController.getDownScale(),
            node2.get_x() * mapController.getDownScale(),
            node2.get_y() * mapController.getDownScale());
    simpleNode.setTranslateZ(5);
    simpleNode.setStrokeWidth(3.5);
    Group root = new Group(simpleNode);
    root.setId(id);
    return root;
  }

  public Group drawNode(String id, double x, double y, Color color) {
    Circle simpleNode = new Circle(x, y, mapController.getNodeSize());
    simpleNode.setTranslateZ(10);
    simpleNode.setFill(color);
    Group root = new Group(simpleNode);
    root.setId(id);
    return root;
  }

  public void resetColors(HashMap<String, Node> nodeSet) {
    for (Node n : nodeSet.values()) {
      for (Node.Link l : n.get_neighbors()) {
        l._shape.setStroke(Color.BLACK);
        l._shape.getParent().setVisible(false);
      }
    }
  }

  public void colorPath(Color color, ArrayList<Node.Link> ret) {
    for (Node.Link c2 : ret) {
      Line simpleNode = c2._shape;
      simpleNode.setStroke(color);
      simpleNode.getParent().setVisible(true);
    }
  }

  public String getCurrentMap() {
    return currentMap;
  }

  public void setMap(String floor) {
    mapController
        .getMapImageView()
        .setImage(
            new Image(
                Objects.requireNonNull(
                    ClassLoader.getSystemResourceAsStream("images/" + floor + ".png"))));
    currentMap = floor;
  }
}
