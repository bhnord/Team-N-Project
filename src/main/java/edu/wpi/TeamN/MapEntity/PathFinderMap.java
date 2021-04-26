package edu.wpi.TeamN.MapEntity;

import edu.wpi.TeamN.services.algo.Edge;
import edu.wpi.TeamN.services.algo.Node;
import edu.wpi.TeamN.services.algo.PathFinder;
import edu.wpi.TeamN.services.database.DatabaseService;
import java.util.ArrayList;
import java.util.HashMap;

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
    PathFinder pathFinder = new PathFinder();
    Node node1 = getNodeSet().get(startNodePath);
    Node node2 = getNodeSet().get(endNodePath);

    return pathFinder.pathfindNoStairs(node1, node2);
  }
}
