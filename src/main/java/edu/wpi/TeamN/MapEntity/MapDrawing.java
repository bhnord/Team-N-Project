package edu.wpi.TeamN.MapEntity;

import edu.wpi.TeamN.services.algo.Node;
import edu.wpi.TeamN.views.MapController;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.ArrayList;

public class MapDrawing {
  private MapController mapController;

  public MapDrawing(MapController mapController) {
    this.mapController = mapController;
  }

  public Group drawLine(String id, Node node1, Node node2) {
    Line simpleNode =
        new Line(
            node1.get_x() * mapController.getDownScale(),
            node1.get_y() * mapController.getDownScale(),
            node2.get_x() * mapController.getDownScale(),
            node2.get_y() * mapController.getDownScale());
    simpleNode.setTranslateZ(5);
    simpleNode.setStrokeWidth(3);
    Group root = new Group(simpleNode);
    root.setId(id);
    return root;
  }

  public Group drawNode(String id, double x, double y) {
    Circle simpleNode = new Circle(x, y, 4.5);
    simpleNode.setTranslateZ(10);
    simpleNode.setFill(Color.BLUE);
    Group root = new Group(simpleNode);
    root.setId(id);
    return root;
  }

  public void resetColors() {
    for (Node n : mapController.getAdminMap().getNodeSet().values()) {
      for (Node.Link l : n.get_neighbors()) {
        l._shape.setStroke(Color.BLACK);
      }
    }
  }

  public void colorPath(Color color, ArrayList<Node.Link> ret) {
    for (Node.Link c2 : ret) {
      Line simpleNode = c2._shape;
      simpleNode.setStroke(color);
    }
  }
}
