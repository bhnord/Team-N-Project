package edu.wpi.TeamN.mapEntity;

import edu.wpi.TeamN.services.algo.Node;
import edu.wpi.TeamN.views.IMapController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class MapDrawing {
  private final IMapController mapController;
  private final String[] maps = {"L1", "L2", "g", "F1", "F2", "F3"};
  private String currentMap;
  private DoubleProperty zoomProperty = new DoubleProperty();

  public MapDrawing(IMapController mapControllerI) {
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
      mapController.correctFloor(c2);
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


  public void zoom(ImageView imageView) throws Exception {

    zoomProperty.addListener(
            new InvalidationListener() {
              @Override
              public void invalidated(Observable arg0) {
                imageView.setFitWidth(zoomProperty.get() * 4);
                imageView.setFitHeight(zoomProperty.get() * 3);
              }
            });

    imageView.addEventFilter(
            ScrollEvent.ANY,
            new EventHandler<ScrollEvent>() {
              @Override
              public void handle(ScrollEvent event) {
                if (event.getDeltaY() > 0 && zoomProperty.get() < 500) {
                  zoomProperty.set(zoomProperty.get() * 1.1);
                } else if (event.getDeltaY() < 0 && zoomProperty.get() > 350) {
                  zoomProperty.set(zoomProperty.get() / 1.1);
                }
              }
            });

    imageView.setOnMousePressed(
            new EventHandler<MouseEvent>() {
              public void handle(MouseEvent event) {
                pressedX = event.getX();
                pressedY = event.getY();
              }
            });

    imageView.setOnMouseDragged(
            new EventHandler<MouseEvent>() {
              public void handle(MouseEvent event) {

                imageView.setTranslateX(imageView.getTranslateX() + event.getX() - pressedX);
                imageView.setTranslateY(imageView.getTranslateY() + event.getY() - pressedY);

                event.consume();
              }
            });
}
