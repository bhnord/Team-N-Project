package edu.wpi.teamname.views;

import com.google.inject.Inject;
import edu.wpi.teamname.services.ServiceTwo;
import edu.wpi.teamname.services.algo.*;
import edu.wpi.teamname.services.algo.Cartesian;
import edu.wpi.teamname.services.database.DatabaseService;
import edu.wpi.teamname.state.HomeState;
import java.io.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Stack;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Map extends masterController implements Initializable {
  @Inject DatabaseService db;
  @Inject ServiceTwo graph;
  @Inject FXMLLoader loader;
  @Inject HomeState state;

  @FXML private Label XLabel;
  @FXML private Label YLabel;
  @FXML private TextArea Text;
  @FXML private Label NodeValues;
  @FXML private TextArea NodeNames;
  @FXML private ImageView mapimage;

  private Scene appPrimaryScene;
  int[] nodeinfo = new int[3];
  ObservableList<Cartesian> nodes = FXCollections.observableArrayList();
  Cartesian startNode = null;
  ArrayList<Line> lines = new ArrayList<>();

  /**
   * This method allows the tests to inject the scene at a later time, since it must be done on the
   * JavaFX thread
   *
   * @param appPrimaryScene Primary scene of the app whose root will be changed
   */
  @Inject
  public void setAppPrimaryScene(Scene appPrimaryScene) {
    this.appPrimaryScene = appPrimaryScene;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    log.debug(state.toString());
  }

  @FXML
  public void advanceHome() throws IOException {
    super.advanceHome(loader, appPrimaryScene);
    Stage stage = (Stage) appPrimaryScene.getWindow();
    stage.setHeight(435);
    stage.setWidth(600);
    stage.centerOnScreen();
  }

  public void xyPrint(MouseEvent mouseDragEvent) {
    XLabel.setText(String.valueOf(mouseDragEvent.getX()));
    YLabel.setText(String.valueOf(mouseDragEvent.getY()));
  }

  private Cartesian get(double x, double y) {
    Cartesian temp = new Cartesian(x, y);
    double min = Double.MAX_VALUE;
    Cartesian closest = null;
    for (Cartesian c : nodes) {
      double distance = c.heuristic(temp);
      if (distance < min) {
        closest = c;
        min = distance;
      }
    }
    return closest;
  }

  public void placeNode(MouseEvent mouseEvent) {
    if (mouseEvent.getButton() == MouseButton.PRIMARY) {
      Circle simpleNode = new Circle(mouseEvent.getX(), mouseEvent.getY(), 4);
      simpleNode.setFill(Color.BLUE);
      Group root = new Group(simpleNode);
      AnchorPane scene = (AnchorPane) appPrimaryScene.getRoot();
      scene.getChildren().add(root);
      nodes.add(new Cartesian(mouseEvent.getX(), mouseEvent.getY()));
    }
  }

  public void startLink(MouseEvent mouseEvent) {
    if (mouseEvent.getButton() != MouseButton.PRIMARY) {
      System.out.println("startingLink");
      startNode = get(mouseEvent.getX(), mouseEvent.getY());
    }
  }

  public void releaseMouse(MouseEvent mouseEvent) {
    if (mouseEvent.getButton() != MouseButton.PRIMARY) {
      System.out.println("endingLink");
      Cartesian other = get(mouseEvent.getX(), mouseEvent.getY());
      if (other != startNode) {
        placeLink(startNode, other);
      }
    }
  }

  //  private void placeNode(String id, int x, int y) {
  //    Circle simpleNode = new Circle(x, y, 2.5);
  //    simpleNode.setFill(Color.BLUE);
  //    Group root = new Group(simpleNode);
  //    AnchorPane scene = (AnchorPane) appPrimaryScene.getRoot();
  //    scene.getChildren().add(root);
  //    Cartesian n = new Cartesian(x, y);
  //    nodeSet.put(id, n);
  //  }

  private void placeLink(Cartesian node1, Cartesian node2) {
    double distance = node1.heuristic(node2);
    node1.addNeighbor(node2, distance);
    node2.addNeighbor(node1, distance);
    Line simpleNode = new Line(node1._x, node1._y, node2._x, node2._y);
    lines.add(simpleNode);
    Group root = new Group(simpleNode);
    AnchorPane scene = (AnchorPane) appPrimaryScene.getRoot();
    scene.getChildren().add(root);
  }

  public void PrintNode(ActionEvent actionEvent) {
    for (Cartesian node : nodes) {
      // System.out.println(Arrays.toString(node) + ", ");
    }
  }

  public void clear(ActionEvent actionEvent) throws IOException {
    Parent root = loader.load(getClass().getResourceAsStream("map.fxml"));
    Screen screen = Screen.getPrimary();
    Rectangle2D bounds = screen.getVisualBounds();

    Stage stage = (Stage) appPrimaryScene.getWindow();
    stage.setX(bounds.getMinX());
    stage.setY(bounds.getMinY());
    stage.setWidth(bounds.getWidth());
    stage.setHeight(bounds.getHeight());
    appPrimaryScene.setRoot(root);

    // this resets image but does not delete nodes since they are attached to the root not the
    // image.
    // mapimage.setImage(new Image("@../../images/level1.png"));
  }

  public void submit(ActionEvent actionEvent) {
    PathFinder<Cartesian> pathFinder = new PathFinder<>();
    Cartesian node1 = nodes.get(0);
    Cartesian node2 = nodes.get(nodes.size() - 1);
    Stack<Node<Cartesian>> ret = pathFinder.Astar(node1, node2);
    Cartesian c1 = (Cartesian) ret.pop();
    while (!ret.empty()) {
      Cartesian c2 = (Cartesian) ret.pop();
      Line simpleNode = new Line(c1._x, c1._y, c2._x, c2._y);
      simpleNode.setStroke(Color.RED);
      Group root = new Group(simpleNode);
      AnchorPane scene = (AnchorPane) appPrimaryScene.getRoot();
      scene.getChildren().add(root);
      c1 = c2;
    }
  }

  //  public void submit(ActionEvent actionEvent) {
  //    String input = Text.getText();
  //    String[] l = input.split("[ \t\n]+");
  //    for (int i = 0; i < l.length; ) {
  //      if (l[i].equals("node:")) {
  //        int x = Integer.parseInt(l[i + 2]);
  //        int y = Integer.parseInt(l[i + 3]);
  //        placeNode(l[i + 1], x, y);
  //        i += 4;
  //      } else if (l[i].equals("link:")) {
  //        placeLink(l[i + 1], l[i + 2]);
  //        i += 3;
  //      } else if (l[i].equals("path:")) {
  //        i += 3;
  //      }
  //    }
  //  }

  //  public void CreateCSV(ActionEvent actionEvent) throws IOException {
  //    String[] nodeList = NodeNames.getText().split("[\n]");
  //    FileWriter csvWriter = new FileWriter("src/main/resources/MapCSV/MapNodes.csv");
  //    for (int i = 0; nodeList.length > i; i++) {
  //      nodeSet.put(nodeList[i], new Cartesian(nodes.get(i)[0], nodes.get(i)[1]));
  //      String[] singleNode = nodeList[i].split("[,]");
  //      csvWriter.append(singleNode[0]);
  //      csvWriter.append(", ");
  //      csvWriter.append(String.valueOf(nodes.get(i)[0]));
  //      csvWriter.append(", ");
  //      csvWriter.append(String.valueOf(nodes.get(i)[1]));
  //      csvWriter.append("\n");
  //    }
  //    placeLink("node1", "node2");
  //    csvWriter.flush();
  //    csvWriter.close();
  //  }
}
