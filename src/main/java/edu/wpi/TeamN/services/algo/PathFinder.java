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

  private double getDirection(Node.Link input) {
    double dx = (input._other.get_x() - input._this.get_x());
    double dy = -(input._other.get_y() - input._this.get_y());
    double ret = Math.atan(Math.abs(dy / dx));
    if (dx * dy < 0) {
      ret = Math.PI / 2 - ret;
    }
    ret += dx < 0 ? Math.PI / 2 : 0;
    ret += dy < 0 ? Math.PI / 2 : 0;
    ret += (dx > 0 && dy < 0) ? Math.PI : 0;
    return ret;
  }

  public String getDescription(ArrayList<Node.Link> input) {
    double previousDirection = 0;
    double currentDirection = 0;
    double minAngle = .45;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder
        .append("walk to ")
        .append(input.get(input.size() - 1)._other.get_longName())
        .append('\n');
    currentDirection = getDirection(input.get(0));
    previousDirection = getDirection(input.get(0));

    for (int i = input.size() - 2; i >= 0; i--) {
      Node.Link l = input.get(i);
      currentDirection = getDirection(l);
      double directionDiff = (currentDirection - previousDirection);
      if (directionDiff > minAngle || directionDiff < (-minAngle)) {
        if (directionDiff > 0) {
          stringBuilder.append("turn left to ");
        } else {
          stringBuilder.append("turn right to ");
        }
      } else {
        stringBuilder.append("continue straight to ");
      }
      stringBuilder.append(l._other.get_longName());
      stringBuilder.append('\n');
      previousDirection = currentDirection;
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
