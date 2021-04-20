package edu.wpi.teamname.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXColorPicker;
import edu.wpi.teamname.services.algo.Edge;
import edu.wpi.teamname.services.algo.Node;
import edu.wpi.teamname.services.database.DatabaseService;
import edu.wpi.teamname.state.HomeState;
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
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;

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

  private String selectedID;
  private Node startNode;
  private Node endNode;

  private Scene appPrimaryScene;
  int[] nodeinfo = new int[3];
  ObservableList<int[]> nodes = FXCollections.observableArrayList();
  HashMap<String, Node> nodeSet = new HashMap<>();
  HashMap<String, Edge> edgeSet = new HashMap<>();

  String nodeName;
  Boolean cancelOrSubmit;

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

    nodeSet = db.getAllNodesMap();
    edgeSet = db.getAllEdgesMap();
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

  private Node get(double x, double y) {
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
      if (cancelOrSubmit) {
        placeNode(nodeName, mouseEvent.getX(), mouseEvent.getY());
      }
    }

    setCancelOrSubmit(false);
  }

  public void setNodeName(String nodeName) {
    this.nodeName = nodeName;
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
      if (other != startNode) {
        placeLink(startNode.get_nodeID() + "_" + other.get_nodeID(), startNode, other);
      }
    }
  }

  private void placeNode(String id, double x, double y) {
    Circle simpleNode = new Circle(x, y, 2.5);
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
    AnchorPane scene = (AnchorPane) appPrimaryScene.getRoot();
    scene.getChildren().add(root);
    Node n = new Node(x, y, id);
    if (!nodeSet.containsKey(id)) {
      nodeSet.put(id, n);
      db.addNode(n);
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
    node1.addNeighbor(id, node2, distance);
    node2.addNeighbor(id, node1, distance);
    Line simpleNode = new Line(node1.get_x(), node1.get_y(), node2.get_x(), node2.get_y());
    simpleNode.setStrokeWidth(2);
    simpleNode.setFill(Color.RED);
    Group root = new Group(simpleNode);
    root.setId(id1 + "_" + id2);
    root.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            current.setText(root.getId());
          }
        });
    AnchorPane scene = (AnchorPane) appPrimaryScene.getRoot();
    scene.getChildren().add(root);
    if (!edgeSet.containsKey(id)) {
      edgeSet.put(id, new Edge(id, node1.get_nodeID(), node2.get_nodeID()));
      db.addEdge(new Edge(id, node1.get_nodeID(), node2.get_nodeID()));
    }
  }

  public void PrintNode(ActionEvent actionEvent) {
    for (int[] node : nodes) {
      System.out.println(Arrays.toString(node) + ", ");
    }
  }

  //  public void examplePathFind(ActionEvent actionEvent) throws IOException { CSV nodesCSV = new
  // CSV("src/main/resources/MapCSV/MapNNodesAll.csv");
  //    CSV edgesCSV = new CSV("src/main/resources/MapCSV/MapNEdgesAll.csv");
  //    int nodesSize = nodesCSV.getLineCount();
  //    int edgeSize = edgesCSV.getLineCount();
  //
  //    for (String node : nodesCSV) {
  //      String[] line = node.split("[,]");
  //      placeNode(
  //          line[0],
  //          ((int) (Integer.parseInt(line[1]) * 0.25 + 10)),
  //          (int) (Integer.parseInt(line[2]) * 0.25 + 10));
  //    }
  //    for (String edge : edgesCSV) {
  //      String[] line = edge.split("[,]");
  //      placeLink(line[0], nodeSet.get(line[1]), nodeSet.get(line[2]));
  //    }
  //
  //    Node node1 = nodeSet.get("CHALL004L1");
  //    Node node2 = nodeSet.get("CLABS002L1");
  //    PathFinder pathFinder = new PathFinder();
  //    pathFinder.Astar(node1, node2);
  //
  //    Stack<Node> ret = pathFinder.Astar(node1, node2);
  //    Cartesian c1 = (Cartesian) ret.pop();
  //    while (!ret.empty()) {
  //      Cartesian c2 = (Cartesian) ret.pop();
  //      Line simpleNode = new Line(c1._x, c1._y, c2._x, c2._y);
  //      simpleNode.setStroke(Color.BLUE);
  //      Group root = new Group(simpleNode);
  //      AnchorPane scene = (AnchorPane) appPrimaryScene.getRoot();
  //      scene.getChildren().add(root);
  //      c1 = c2;
  //    }
  //  }

  /**
   * restarts the map class. Needs work.
   *
   * @param actionEvent
   * @throws IOException
   */
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
  }

  // public void submit(ActionEvent actionEvent) {
  // String input = Text.getText();
  // String[] l = input.split("[ \t\n]+");
  // for (int i = 0; i < l.length; ) {
  // if (l[i].equals("node:")) {
  // int x = Integer.parseInt(l[i + 2]);
  // int y = Integer.parseInt(l[i + 3]);
  // placeNode(l[i + 1], x, y);
  // i += 4;
  // } else if (l[i].equals("link:")) {
  // placeLink(l[i + 1], l[i + 2]);
  // i += 3;
  // } else if (l[i].equals("path:")) {
  // PathFinder pathFinder = new PathFinder();
  // Cartesian node1 = nodeSet.get(l[i + 1]);
  // Cartesian node2 = nodeSet.get(l[i + 2]);
  // Stack<Node> ret = pathFinder.Astar(node1, node2);
  // Cartesian c1 = (Cartesian) ret.pop();
  // while (!ret.empty()) {
  // Cartesian c2 = (Cartesian) ret.pop();
  // Line simpleNode = new Line(c1._x, c1._y, c2._x, c2._y);
  // simpleNode.setStroke(Color.RED);
  // Group root = new Group(simpleNode);
  // AnchorPane scene = (AnchorPane) appPrimaryScene.getRoot();
  // scene.getChildren().add(root);
  // c1 = c2;
  // }
  // i += 3;
  // }
  // }
  // }

  /**
   * replaces MapNodes.csv with a new csv of the current nodesSet
   *
   * @param actionEvent
   * @throws IOException
   */
  public void CreateCSV(ActionEvent actionEvent) throws IOException {
    //    String[] nodeList = NodeNames.getText().split("[\n]");
    //    FileWriter csvWriter = new FileWriter("src/main/resources/MapCSV/MapNodes.csv");
    //    AnchorPane scene = (AnchorPane) appPrimaryScene.getRoot();
    //    for (int i = 0; nodeList.length > i; i++) {
    //      String[] singleNode = nodeList[i].split("[,]");
    //      nodeSet.put(nodeList[i], new Cartesian(nodes.get(i)[0], nodes.get(i)[1]));
    //      scene.getChildren().get(i + 1).setId(singleNode[0]);
    //      csvWriter.append(singleNode[0]);
    //      csvWriter.append(", ");
    //      csvWriter.append(String.valueOf(nodes.get(i)[0]));
    //      csvWriter.append(", ");
    //      csvWriter.append(String.valueOf(nodes.get(i)[1]));
    //      csvWriter.append("\n");
    //    }
    //    csvWriter.flush();
    //    csvWriter.close();
  }

  //  public void CreateLinks(ActionEvent actionEvent) {
  //    String[] links = LinkField.getText().split("[\n]");
  //    for (String link : links) {
  //      String[] l = link.split(",");
  //      placeLink(l[0], l[1]);
  //    }
  //  }

  public void PathFind(ActionEvent actionEvent) {
    //    String[] S_E_nodes = pathFindNodes.getText().split("[,]");
    //    PathFinder pathFinder = new PathFinder();
    //    Node node1 = nodeSet.get(S_E_nodes[0]);
    //    Node node2 = nodeSet.get(S_E_nodes[1]);
    //    pathFinder.Astar(node1, node2);
    //
    //    Stack<Node> ret = pathFinder.Astar(node1, node2);
    //    Cartesian c1 = (Cartesian) ret.pop();
    //    while (!ret.empty()) {
    //      Cartesian c2 = (Cartesian) ret.pop();
    //      Line simpleNode = new Line(c1._x, c1._y, c2._x, c2._y);
    //      simpleNode.setStroke(Color.BLUE);
    //      Group root = new Group(simpleNode);
    //      AnchorPane scene = (AnchorPane) appPrimaryScene.getRoot();
    //      scene.getChildren().add(root);
    //      c1 = c2;
    //    }
  }

  private void DeleteNodesFromMap() {
    AnchorPane scene = (AnchorPane) appPrimaryScene.getRoot();
    scene.getChildren().remove(current.getText());
  }

  public static int getLineCount(String csv) throws IOException {
    BufferedReader readCSV =
        Files.newBufferedReader(Paths.get("src/main/resources/MapCSV/" + csv + ".csv"));
    int lineCount = 0;
    String data;
    while ((data = readCSV.readLine()) != null) {
      lineCount++;
    }
    return lineCount;
  }

  public void DeleteObjectDataBase() throws IOException {
    if (nodeSet.containsKey(current.getText())) {
      db.deleteNode(current.getText());
    } else if (edgeSet.containsKey(current.getText())) {
      db.deleteEdge(current.getText());
    } else {
      System.out.println("Object does not exist");
    }
  }

  public void examplePathFind(ActionEvent actionEvent) {
    for (HashMap.Entry<String, Node> node : nodeSet.entrySet()) {
      node.getValue().set_x(node.getValue().get_x() * .25);
      node.getValue().set_y(node.getValue().get_y() * .25);
      placeNode(node.getKey(), node.getValue().get_x(), node.getValue().get_y());
    }

    for (HashMap.Entry<String, Edge> edge : edgeSet.entrySet()) {
      placeLink(
          edge.getKey(),
          nodeSet.get(edge.getValue().getStartNode()),
          nodeSet.get(edge.getValue().getEndNode()));
    }
  }

  public void deleteCurrent(ActionEvent actionEvent) throws IOException {
    DeleteObjectDataBase();
    DeleteNodesFromMap();
  }
}
