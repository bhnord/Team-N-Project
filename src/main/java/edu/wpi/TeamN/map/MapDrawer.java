package edu.wpi.TeamN.map;

import edu.wpi.TeamN.services.algo.Node;
import edu.wpi.TeamN.views.MapController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class MapDrawer {
  private final MapController mapController;
  private final String[] maps = {"L1", "L2", "g", "F1", "F2", "F3"};
  private String currentMap;
  final DoubleProperty zoomProperty = new SimpleDoubleProperty(1);
  private double pressedX, pressedY;
  private double offsetX, offsetY;
  private final double maxZoom = 5.6;
  private final double minZoom = 1;
  private double maxImgWidth;
  private double maxImgHeight;

  private Group draggedLineGroup;
  private Line draggedLine;

  public MapDrawer(MapController mapControllerI) {
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

  public Group startLine(Node node1) {
    Line simpleNode =
        new Line(
            node1.get_x() * mapController.getDownScale(),
            node1.get_y() * mapController.getDownScale(),
            node1.get_x() * mapController.getDownScale(),
            node1.get_y() * mapController.getDownScale());
    simpleNode.setTranslateZ(5);
    simpleNode.setStrokeWidth(3.5);
    Group root = new Group(simpleNode);
    root.setId(node1.get_nodeID() + "_");
    this.draggedLineGroup = root;
    this.draggedLine = simpleNode;
    mapController.getMapAnchor().getChildren().add(root);
    return root;
  }

  public Group endLine(Node node2) {
    draggedLineGroup.setId(draggedLine.getId() + node2.get_nodeID());
    Group temp = draggedLineGroup;
    this.draggedLine.setEndX(node2.get_x() * mapController.getDownScale());
    this.draggedLine.setEndY(node2.get_y() * mapController.getDownScale());
    this.draggedLine = null;
    this.draggedLineGroup = null;
    return temp;
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

  public void captureClick(MouseEvent e) {
    if (e.getButton() == MouseButton.MIDDLE) {
      pressedX = e.getX();
      pressedY = e.getY();
    }
  }

  public void captureMouseDrag(MouseEvent event) {
    event.consume();
    if (event.getButton() == MouseButton.SECONDARY) {
      if (draggedLine != null) {
        draggedLine.setEndX(event.getX());
        draggedLine.setEndY(event.getY());
      }
    }
    if (event.getButton() == MouseButton.MIDDLE) {
      mapController
          .getMapAnchor()
          .setTranslateX(mapController.getMapAnchor().getTranslateX() + event.getX() - pressedX);
      mapController
          .getMapAnchor()
          .setTranslateY(mapController.getMapAnchor().getTranslateY() + event.getY() - pressedY);
      offsetX = mapController.getMapAnchor().getTranslateX();
      offsetX -= (maxImgWidth * (zoomProperty.get() - 1)) / 2;
      offsetY = mapController.getMapAnchor().getTranslateY();
      offsetY -= (maxImgHeight * (zoomProperty.get() - 1)) / 2;
      correctImage(mapController.getMapAnchor());
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

  public double invTransformX(double input) {
    return input * zoomProperty.get() + offsetX;
  }

  public double invTransformY(double input) {
    return input * zoomProperty.get() + offsetY;
  }

  private double invOffsetX(double input) {
    return input + maxImgWidth * (zoomProperty.get() - 1) / 2;
  }

  private double invOffsetY(double input) {
    return input + maxImgHeight * (zoomProperty.get() - 1) / 2;
  }

  public double transformX(double input) {
    return ((input - offsetX) / zoomProperty.get());
  }

  public double transformY(double input) {
    return ((input - offsetY) / zoomProperty.get());
  }

  private void correctImage(AnchorPane mapContainer) {
    if (transformX(0) < 0) {
      mapContainer.setTranslateX(invOffsetX(0));
    }
    if (transformY(0) < 0) {
      mapContainer.setTranslateY(invOffsetY(0));
    }
    if (transformX(maxImgWidth) > maxImgWidth) {
      mapContainer.setTranslateX(invOffsetX(-maxImgWidth * (zoomProperty.get() - 1)));
    }
    if (transformY(maxImgHeight) > maxImgHeight) {
      mapContainer.setTranslateY(invOffsetY(-maxImgHeight * (zoomProperty.get() - 1)));
    }
  }

  public void setUpZoom(ImageView imageView, AnchorPane mapContainer) {
    maxImgWidth = imageView.getFitWidth();
    maxImgHeight = imageView.getFitHeight();
    zoomProperty.addListener(
        new InvalidationListener() {
          @Override
          public void invalidated(Observable arg0) {
            mapContainer.setScaleX(zoomProperty.get());
            mapContainer.setScaleY(zoomProperty.get());
          }
        });

    imageView.addEventFilter(
        ScrollEvent.ANY,
        new EventHandler<ScrollEvent>() {
          @Override
          public void handle(ScrollEvent event) {
            if (event.getDeltaY() > 0 && zoomProperty.get() <= maxZoom) {
              zoomProperty.set(zoomProperty.get() * 1.08);
            } else if (event.getDeltaY() < 0 && zoomProperty.get() >= minZoom) {
              zoomProperty.set(zoomProperty.get() / 1.08);
            }
            correctImage(mapContainer);
          }
        });
  }
}
