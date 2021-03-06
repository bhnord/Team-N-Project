package edu.wpi.cs3733.d21.teamN.map;

import edu.wpi.cs3733.d21.teamN.services.algo.Edge;
import edu.wpi.cs3733.d21.teamN.services.algo.Node;
import edu.wpi.cs3733.d21.teamN.services.algo.PathFinder;
import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.shape.Line;

public class PathFinderMap {
  private DatabaseService db;

  private HashMap<String, Node> nodeSet = new HashMap<>();
  private HashMap<String, Edge> edgeSet = new HashMap<>();

  private final double downScale = .25;
  private final double upScale = 4;

  public PathFinderMap(DatabaseService db) {
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

  public ArrayList<Node.Link> pathfind(String startNodePath, String endNodePath) {
    PathFinder pathFinder = PathFinder.getPathFinder();
    Node node1 = getNodeSet().get(startNodePath);
    Node node2 = getNodeSet().get(endNodePath);
    ArrayList<Node.Link> ret = pathFinder.pathfind(node1, node2);

    return ret;
  }

  public void makeEdge(String id, Node node1, Node node2, Line simpleNode) {
    double distance = node1.heuristic(node2);
    node1.addNeighbor(id, node2, distance, simpleNode, false);
    node2.addNeighbor(id, node1, distance, simpleNode, false);
  }
}
