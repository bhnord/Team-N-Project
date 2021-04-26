package edu.wpi.TeamN.services.algo;

import java.util.ArrayList;

public class PathFinder implements PathFinderI {
  private PathFinderI impl;

  public PathFinder(PathFinderI impl) {
    this.impl = impl;
  }

  public PathFinder() {
    this.impl = new Astar();
  }

  @Override
  public ArrayList<Node.Link> pathfind(Node start, Node end) {
    return impl.pathfind(start, end);
  }

  @Override
  public ArrayList<Node.Link> pathfindNoStairs(Node start, Node end) {
    return impl.pathfindNoStairs(start, end);
  }

  @Override
  public ArrayList<Node.Link> pathfindFull(Node start, Node end, Reduce filter) {
    return impl.pathfindFull(start, end, filter);
  }

  public String getDescription(ArrayList<Node.Link> input) {
    StringBuilder stringBuilder = new StringBuilder();
    for (Node.Link l : input) {
      stringBuilder.append(l._this.get_nodeID());
      stringBuilder.append("\n");
    }
    return stringBuilder.toString();
  }
}
