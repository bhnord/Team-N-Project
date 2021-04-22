package edu.wpi.TeamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXScrollPane;
import edu.wpi.TeamN.services.algo.Edge;
import edu.wpi.TeamN.services.algo.Node;
import edu.wpi.TeamN.services.algo.PathFinder;
import edu.wpi.TeamN.services.database.DatabaseService;
import edu.wpi.TeamN.state.HomeState;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MapController extends masterController implements Initializable {
  @FXML private JFXColorPicker colorPicker;
  @Inject DatabaseService db;
  // @Inject ServiceTwo graph;
  @Inject FXMLLoader loader;
  @Inject HomeState state;

  @FXML private Label XLabel;
  @FXML private Label YLabel;
  @FXML private Label current;
  @FXML private ImageView mapImageView;
  @FXML private JFXScrollPane scrollPane;

  private String selectedID;
  private Node startNode;
  private Node endNode;
  private double scale = 1;

  private String startNodePath;
  private String endNodePath;
  final DoubleProperty zoomProperty = new SimpleDoubleProperty();

  private Scene appPrimaryScene;
  int[] nodeinfo = new int[3];
  ObservableList<int[]> nodes = FXCollections.observableArrayList();
  HashMap<String, Node> nodeSet = new HashMap<>();
  HashMap<String, Edge> edgeSet = new HashMap<>();
  HashMap<String, Integer> mapObjects = new HashMap<>();
  ArrayList<Node.Link> path = new ArrayList<>();

  String nodeName;
  String building;
  String floor;
  String longname;
  String shortname;
  Boolean cancelOrSubmit = false;

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

  @SneakyThrows
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    log.debug(state.toString());
    colorPicker.setValue(Color.BLUE);

    // nodeSet = db.getAllNodesMap();
    // edgeSet = db.getAllEdgesMap();
    nodeSet = new HashMap<>();
    edgeSet = new HashMap<>();
    mapObjects = new HashMap<>();

    zoomProperty.set(841);
  }

  @FXML
  public void advanceHome() throws IOException {
    super.advanceHome(loader, appPrimaryScene);
    Stage stage = (Stage) appPrimaryScene.getWindow();
    stage.setHeight(800);
    stage.setWidth(1366);
    stage.centerOnScreen();
  }

  /**
   * Prints the x and y values of the cursor on the bottom left of the screen
   *
   * @param mouseDragEvent
   */
  public void xyPrint(MouseEvent mouseDragEvent) {
    XLabel.setText(String.valueOf(mouseDragEvent.getX()));
    YLabel.setText(String.valueOf(mouseDragEvent.getY()));
  }

  // Getting the closest node to the mouse
  private Node get(double x, double y) {
    x *= 4;
    y *= 4;
    double min = Double.MAX_VALUE;
    Node closest = null;
    for (Node c : nodeSet.values()) {
      double distance = (c.get_x() - x) * (c.get_x() - x) + (c.get_y() - y) * (c.get_y() - y);
      if (distance < min) {
        closest = c;
        min = distance;
      }
    }
    return closest;
  }

  /**
   * on mouse click on the map places a node with no id but when clicked it returns the node FX:ID
   *
   * @param mouseEvent
   */
  public void placeNodeClick(MouseEvent mouseEvent) throws IOException {
    if (mouseEvent.getButton() == MouseButton.PRIMARY) {
      NameNode nameNodeClass = new NameNode();
      nameNodeClass.confirm(this);
      if (cancelOrSubmit && !nodeName.equals("")) {
        placeNode(nodeName, mouseEvent.getX(), mouseEvent.getY());
      }
    }

    setCancelOrSubmit(false);
  }

  public void setNodeProperties(
      String nodeName, String floor, String building, String longname, String shortname) {
    this.nodeName = nodeName;
    this.floor = floor;
    this.longname = longname;
    this.shortname = shortname;
    this.building = building;
  }

  public void setCancelOrSubmit(Boolean sm) {
    cancelOrSubmit = sm;
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
      Node other = get(mouseEvent.getX(), mouseEvent.getY());
      System.out.println(startNode.get_nodeID() + "_" + other.get_nodeID());
      if (other != startNode) {
        placeLink(startNode.get_nodeID() + "_" + other.get_nodeID(), startNode, other);
      }
    }
  }

  private void placeNode(String id, double x, double y) {
    Circle simpleNode = new Circle(x, y, 4.5);
    simpleNode.setTranslateZ(10);
    simpleNode.setFill(Color.BLUE);
    Group root = new Group(simpleNode);
    root.setId(id);
    root.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            current.setText(root.getId());
          }
        });
    root.setOnMousePressed(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            startLink(event);
          }
        });
    root.setOnMouseReleased(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            releaseMouse(event);
          }
        });
    AnchorPane scene = (AnchorPane) appPrimaryScene.getRoot();
    scene.getChildren().add(root);
    Node n =
        new Node(x * 4, y * 4, id, this.floor, this.building, "", this.longname, this.shortname);
    n.set_shape(simpleNode);
    if (!nodeSet.containsKey(id)) {
      mapObjects.put(id, scene.getChildren().size() - 1);
      nodeSet.put(id, n);
      db.addNode(n);
      setNodeProperties("", "", "", "", "");
    }
  }

  /**
   * Places a line between two nodes
   *
   * @param node1 node id of first node
   * @param node2 node id of the second node
   */
  private void placeLink(String id, Node node1, Node node2) {
    String id1 = node1.get_nodeID();
    String id2 = node2.get_nodeID();
    double distance = node1.heuristic(node2);
    Line simpleNode =
        new Line(node1.get_x() / 4, node1.get_y() / 4, node2.get_x() / 4, node2.get_y() / 4);
    simpleNode.setTranslateZ(5);
    node1.addNeighbor(id, node2, distance, simpleNode);
    node2.addNeighbor(id, node1, distance, simpleNode);
    simpleNode.setStrokeWidth(3);
    Group root = new Group(simpleNode);
    String edgeID = id1 + "_" + id2;
    root.setId(edgeID);
    root.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            current.setText(root.getId());
          }
        });
    AnchorPane scene = (AnchorPane) appPrimaryScene.getRoot();
    scene.getChildren().add(root);
    mapObjects.put(edgeID, scene.getChildren().size() - 1);
    if (!edgeSet.containsKey(id)) {
      edgeSet.put(edgeID, new Edge(edgeID, node1.get_nodeID(), node2.get_nodeID()));

      db.addEdge(new Edge(edgeID, node1.get_nodeID(), node2.get_nodeID()));
    }
  }

  /**
   * restarts the map class. Needs work.
   *
   * @param actionEvent
   * @throws IOException
   */
  // Reloading the map scene
  public void clear(ActionEvent actionEvent) throws IOException {
    Parent root = loader.load(getClass().getResourceAsStream("map.fxml"));
    Screen screen = Screen.getPrimary();
    Rectangle2D bounds = screen.getVisualBounds();

    Stage stage = (Stage) appPrimaryScene.getWindow();
    // stage.setX(bounds.getMinX());
    // stage.setY(bounds.getMinY());
    // stage.setWidth(bounds.getWidth());
    // stage.setHeight(bounds.getHeight());
    appPrimaryScene.setRoot(root);
  }

  public void newColor(ActionEvent actionEvent) {
    System.out.println(path.size());
    colorPath(colorPicker.getValue(), path);
  }

  public void PathFind(ActionEvent actionEvent) {
    resetColors();
    PathFinder pathFinder = new PathFinder();
    Node node1 = nodeSet.get(startNodePath);
    Node node2 = nodeSet.get(endNodePath);

    path = pathFinder.Astar(node1, node2);
    colorPath(colorPicker.getValue(), path);
  }

  private void colorPath(Color color, ArrayList<Node.Link> ret) {
    for (Node.Link c2 : ret) {
      System.out.println("HERE");
      Line simpleNode = c2._shape;
      simpleNode.setStroke(color);
    }
  }

  private void resetColors() {
    for (Node n : nodeSet.values()) {
      for (Node.Link l : n.get_neighbors()) {
        l._shape.setStroke(Color.BLACK);
      }
    }
  }

  private void DeleteNodesFromMap() throws IOException {
    AnchorPane scene = (AnchorPane) appPrimaryScene.getRoot();
    int i = 9;
    System.out.println(scene.getChildren().size());
    for (javafx.scene.Node root : scene.getChildren().subList(9, scene.getChildren().size() - 1)) {
      if (root.getId().equals(current.getText())) {
        scene.getChildren().remove(i);
        return;
      } else {
        i++;
      }
    }
  }

  public void DeleteObjectDataBase() throws IOException {
    String id = current.getText();

    if (nodeSet.containsKey(id)) {
      db.deleteNode(id);
      nodeSet.remove(id);
    } else if (edgeSet.containsKey(id)) {
      db.deleteEdge(id);
      edgeSet.remove(id);
    } else {
      System.out.println("Object does not exist");
    }

    current.setText("No object Selected");
  }

  // Loading from the database
  public void Load(ActionEvent actionEvent) {
    nodeSet.clear();
    edgeSet.clear();
    nodeSet = db.getAllNodesMap();
    edgeSet = db.getAllEdgesMap();

    for (HashMap.Entry<String, Edge> edge : edgeSet.entrySet()) {
      placeLink(
          edge.getKey(),
          nodeSet.get(edge.getValue().getStartNode()),
          nodeSet.get(edge.getValue().getEndNode()));
    }
    for (HashMap.Entry<String, Node> node : nodeSet.entrySet()) {
      placeNode(node.getKey(), node.getValue().get_x() * .25, node.getValue().get_y() * .25);
    }
  }

  public void deleteCurrent(ActionEvent actionEvent) throws IOException, InterruptedException {
    DeleteNodesFromMap();
    DeleteObjectDataBase();
  }

  public void SetStartNode(ActionEvent actionEvent) {
    if (nodeSet.containsKey(current.getText())) startNodePath = current.getText();
  }

  public void SetEndNode(ActionEvent actionEvent) {
    if (nodeSet.containsKey(current.getText())) endNodePath = current.getText();
  }

  @FXML
  public void logOut() throws IOException {
    super.logOut(loader, appPrimaryScene);
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

  public void zoom(ScrollEvent scrollEvent) throws Exception {
    zoomProperty.addListener(
        new InvalidationListener() {
          @Override
          public void invalidated(Observable arg0) {
            mapImageView.setFitWidth(zoomProperty.get());
            mapImageView.setFitHeight(zoomProperty.get());
          }
        });

    mapImageView.addEventFilter(
        ScrollEvent.ANY,
        new EventHandler<ScrollEvent>() {
          @Override
          public void handle(ScrollEvent event) {
            if (event.getDeltaY() > 0 && zoomProperty.get() < 1500 /*841*/) {
              // zoomProperty.set(zoomProperty.get() * 1.001);
              scale = scale * 1.001;
              Rectangle2D rec =
                  new Rectangle2D(
                      200,
                      200,
                      mapImageView.getFitWidth() * scale,
                      mapImageView.getFitHeight() * scale);

              mapImageView.setViewport(rec);
            } else if (event.getDeltaY() < 0 && zoomProperty.get() > 400) {
              // zoomProperty.set(zoomProperty.get() / 1.001);
              scale = scale / 1.001;
              Rectangle2D rec =
                  new Rectangle2D(
                      0,
                      0,
                      mapImageView.getFitWidth() * scale,
                      mapImageView.getFitHeight() * scale);

              mapImageView.setViewport(rec);
            }
          }
        });
  }
}
