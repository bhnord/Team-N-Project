package edu.wpi.TeamN.MapEntity;

import edu.wpi.TeamN.services.algo.Edge;
import edu.wpi.TeamN.services.algo.Node;
import edu.wpi.TeamN.services.algo.PathFinder;
import edu.wpi.TeamN.services.database.DatabaseService;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class AdminMap {
  private DatabaseService db;

  private HashMap<String, Node> nodeSet = new HashMap<>();
  private HashMap<String, Edge> edgeSet = new HashMap<>();

  private final double downScale = .168;
  private final double upScale = 5.9523;

  private String startNodePath;
  private String endNodePath;

  public AdminMap(DatabaseService db) {
    this.db = db;
    nodeSet = db.getAllNodesMap();
    edgeSet = db.getAllEdgesMap();
  }

  public HashMap<String, Node> getNodeSet() {
    return nodeSet;
  }

  public HashMap<String, Edge> getEdgeSet() {
    return edgeSet;
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

  public Node get(double x, double y) {
    x *= upScale;
    y *= upScale;
    double min = Double.MAX_VALUE;
    Node closest = null;
    for (Node c : getNodeSet().values()) {
      double distance = (c.get_x() - x) * (c.get_x() - x) + (c.get_y() - y) * (c.get_y() - y);
      if (distance < min) {
        closest = c;
        min = distance;
      }
    }
    return closest;
  }

  public ArrayList<Node.Link> pathfind() {
    PathFinder pathFinder = new PathFinder();
    Node node1 = getNodeSet().get(startNodePath);
    Node node2 = getNodeSet().get(endNodePath);

    return pathFinder.Astar(node1, node2);
  }

  public void SetStartNode(String snp) {
    if (getNodeSet().containsKey(snp)) startNodePath = snp;
  }

  public void SetEndNode(String enp) {
    if (getNodeSet().containsKey(enp)) endNodePath = enp;
  }

  public void colorPath(Color color, ArrayList<Node.Link> ret) {
    for (Node.Link c2 : ret) {
      Line simpleNode = c2._shape;
      simpleNode.setStroke(color);
    }
  }

  public void resetColors() {
    for (Node n : getNodeSet().values()) {
      for (Node.Link l : n.get_neighbors()) {
        l._shape.setStroke(Color.BLACK);
      }
    }
  }

  public void makeEdge(String id, Node node1, Node node2, Line simpleNode) {
    double distance = node1.heuristic(node2);
    node1.addNeighbor(id, node2, distance, simpleNode);
    node2.addNeighbor(id, node1, distance, simpleNode);
  }
}
