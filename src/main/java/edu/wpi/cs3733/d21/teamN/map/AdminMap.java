package edu.wpi.cs3733.d21.teamN.map;

import edu.wpi.cs3733.d21.teamN.services.algo.Edge;
import edu.wpi.cs3733.d21.teamN.services.algo.Node;
import edu.wpi.cs3733.d21.teamN.services.algo.PathFinder;
import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.shape.Line;

public class AdminMap {
  private DatabaseService db;

  private HashMap<String, Node> nodeSet = new HashMap<>();
  private HashMap<String, Edge> edgeSet = new HashMap<>();

  private final double downScale = .25;
  private final double upScale = 4;

  private String startNodePath;
  private String endNodePath;

  public AdminMap(DatabaseService db) {
    this.db = db;
    nodeSet = db.getAllNodesMap();
    edgeSet = db.getAllEdgesMap();

    System.out.println(nodeSet);
    System.out.println(edgeSet);
  }

  public HashMap<String, Node> getNodeSet() {
    return nodeSet;
  }

  public HashMap<String, Edge> getEdgeSet() {
    return edgeSet;
  }

  public void updateNode(Node node) {
    db.updateNode(
        node.get_nodeID(),
        node.get_x(),
        node.get_y(),
        node.get_floor(),
        node.get_building(),
        node.get_nodeType(),
        node.get_longName(),
        node.get_shortName());
  }

  public void updateEdge(String id, String startNode, String endNode) {
    db.addEdge(new Edge(id, startNode, endNode));
  }

  public void addNode(Node node) {
    nodeSet.put(node.get_nodeID(), node);
    db.addNode(node);
  }

  public void addEdge(Edge edge) {
    edgeSet.put(edge.getEdgeID(), edge);
    db.addEdge(edge);
  }

  public void deleteNode(String id) {
    db.deleteNode(id);
    nodeSet.remove(id);
  }

  public void deleteEdge(String id) {
    db.deleteEdge(id);
    edgeSet.remove(id);
  }

  public Node get(double x, double y, String floor) {
    x *= upScale;
    y *= upScale;
    double min = Double.MAX_VALUE;
    Node closest = null;
    for (Node c : getNodeSet().values()) {
      if (c.get_floor().equals(floor)) {
        double distance = (c.get_x() - x) * (c.get_x() - x) + (c.get_y() - y) * (c.get_y() - y);
        if (distance < min) {
          closest = c;
          min = distance;
        }
      }
    }
    return closest;
  }

  public ArrayList<Node.Link> pathfind() {
    PathFinder pathFinder = PathFinder.getPathFinder();
    Node node1 = getNodeSet().get(startNodePath);
    Node node2 = getNodeSet().get(endNodePath);

    return pathFinder.pathfindNoStairs(node1, node2);
  }

  public void SetStartNode(String snp) {

    if (getNodeSet().containsKey(snp)) {
      //      if (snp.toLowerCase().contains("park")) User.setParkingSpot(snp);
      startNodePath = snp;
    }
  }

  public void SetEndNode(String enp) {
    if (getNodeSet().containsKey(enp)) {
      //      if (enp.toLowerCase().contains("park")) User.setParkingSpot(enp);
      endNodePath = enp;
    }
  }

  public void makeEdge(String id, Node node1, Node node2, Line simpleNode) {
    double distance = node1.heuristic(node2);
    node1.addNeighbor(id, node2, distance, simpleNode, false);
    node2.addNeighbor(id, node1, distance, simpleNode, false);
  }
}
