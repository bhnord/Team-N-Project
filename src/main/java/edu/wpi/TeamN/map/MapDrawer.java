package edu.wpi.TeamN.map;

import com.jfoenix.controls.JFXScrollPane;
import edu.wpi.TeamN.services.algo.Node;
import edu.wpi.TeamN.views.IMapController;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class MapDrawer {
  private final IMapController mapController;
  private final String[] maps = {"L1", "L2", "g", "F1", "F2", "F3"};
  private String currentMap;
  final DoubleProperty zoomProperty = new SimpleDoubleProperty(1);
  private double pressedX, pressedY;
  private double offsetX, offsetY;
  private final double maxZoom = 5.6;
  private final double minZoom = 1;
  private double maxImgWidth = 1000;
  private double maxImgHeight = 700;

  public MapDrawer(IMapController mapControllerI) {
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

  private void correctImage(JFXScrollPane mapContainer) {
    if (transformX(0) < 0) {
      mapContainer.setTranslateX(invOffsetX(0));
    }
    if (transformY(0) < 0) {
      mapContainer.setTranslateY(invOffsetY(0));
    }
    //    System.out.println(transformX(maxImgWidth) - transformX(0));
    System.out.println(transformX(maxImgWidth));

    if (transformX(maxImgWidth) > maxImgWidth) {
      mapContainer.setTranslateX(invOffsetX(-maxImgWidth * (zoomProperty.get() - 1)));
    }
    if (transformX(maxImgWidth) > maxImgWidth) {
      mapContainer.setTranslateY(invOffsetY(-maxImgHeight * (zoomProperty.get() - 1)));
    }
  }

  public void setUpZoom(ImageView imageView, JFXScrollPane mapContainer) {
    maxImgWidth = imageView.getFitWidth();
    maxImgHeight = imageView.getFitHeight();
    zoomProperty.addListener(
        new InvalidationListener() {
          @Override
          public void invalidated(Observable arg0) {
            //            mapContainer.setFitWidth(zoomProperty.get() * 4);
            mapContainer.setScaleX(zoomProperty.get());
            mapContainer.setScaleY(zoomProperty.get());
            //            mapContainer.setFitHeight(zoomProperty.get() * 3);
          }
        });

    imageView.addEventFilter(
        ScrollEvent.ANY,
        new EventHandler<ScrollEvent>() {
          @Override
          public void handle(ScrollEvent event) {
            double oldCenterX = offsetX + imageView.getFitWidth() * zoomProperty.get();
            double oldCenterY = offsetY + imageView.getFitHeight() * zoomProperty.get();
            if (event.getDeltaY() > 0 && zoomProperty.get() <= maxZoom) {
              zoomProperty.set(zoomProperty.get() * 1.1);
            } else if (event.getDeltaY() < 0 && zoomProperty.get() >= minZoom) {
              zoomProperty.set(zoomProperty.get() / 1.1);
            }
            double newCenterX = offsetX + imageView.getFitWidth() * zoomProperty.get();
            double newCenterY = offsetY + imageView.getFitHeight() * zoomProperty.get();
            correctImage(mapContainer);
            //            mapContainer.setTranslateX(
            //                mapContainer.getTranslateX() + .5 * (newCenterX - oldCenterX));
            //            mapContainer.setTranslateY(
            //                mapContainer.getTranslateY() + .5 * (newCenterY - oldCenterY));
            //            System.out.println(newCenterX - oldCenterX);
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
            event.consume();
            mapContainer.setTranslateX(mapContainer.getTranslateX() + event.getX() - pressedX);
            mapContainer.setTranslateY(mapContainer.getTranslateY() + event.getY() - pressedY);
            offsetX = mapContainer.getTranslateX();
            offsetX -= (maxImgWidth * (zoomProperty.get() - 1)) / 2;
            offsetY = mapContainer.getTranslateY();
            offsetY -= (maxImgHeight * (zoomProperty.get() - 1)) / 2;

            correctImage(mapContainer);
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
}
