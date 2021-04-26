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

  public ArrayList<Node.Link> pathfind(Node start, Node end) {
    return impl.pathfindFull(start, end, (l) -> true);
  }

  public ArrayList<Node.Link> pathfindNoStairs(Node start, Node end) {
    return impl.pathfindFull(start, end, (l) -> !l._isStair);
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

  /**
   * @param start node to walk back to
   * @param curNode node to start walking back from
   * @return a stack of nodes where start is at the top and every next node's parent is the previous
   *     node in the stack
   */
  protected static ArrayList<Node.Link> rebuild(Node start, Node curNode) {
    ArrayList<Node.Link> ret = new ArrayList<>();
    while (curNode.get_parent() != null) {
      ret.add(curNode.get_parent());
      curNode = curNode.get_parent()._this;
    }
    return ret;
  }
}
