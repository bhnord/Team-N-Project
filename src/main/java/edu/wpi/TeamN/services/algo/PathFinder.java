package edu.wpi.TeamN.services.algo;

import java.util.ArrayList;
import java.util.Collections;

public class PathFinder implements IPathFinder {
  private IPathFinder impl;

  public PathFinder(IPathFinder impl) {
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
    if (dx == 0) {
      return dy > 0 ? Math.PI / 2 : 3 * Math.PI / 2;
    }
    if (dy == 0) {
      return dx < 0 ? Math.PI : 0;
    }
    double ret = Math.atan(Math.abs(dy / dx));
    if (dx * dy < 0) {
      ret = Math.PI / 2 - ret;
    }
    ret += dx < 0 ? Math.PI / 2 : 0;
    ret += dy < 0 ? Math.PI / 2 : 0;
    ret += (dx > 0 && dy < 0) ? Math.PI : 0;
    System.out.println(dx + ", " + dy + ", " + ret);
    return ret;
  }

  public ArrayList<String> getDescription(ArrayList<Node.Link> input) {
    if (input.size() == 0) {
      return (ArrayList<String>) Collections.singleton("No Path");
    }
    double previousDirection = 0;
    double currentDirection = 0;
    double minAngle = .45;
    ArrayList<String> ret = new ArrayList<>();
    // ret.add("walk to " + input.get(input.size() - 1)._other.get_longName() + '\n');
    previousDirection = getDirection(input.get(input.size() - 1));

    for (int i = input.size() - 1; i >= 0; i--) {
      Node.Link l = input.get(i);
      currentDirection = getDirection(l);
      double directionDiff = (currentDirection - previousDirection);
      if (!l._this.get_floor().equals(l._other.get_floor())) {
        ret.add("go to floor " + l._other.get_floor());
      } else {
        if (directionDiff > minAngle || directionDiff < (-minAngle)) {
          if (directionDiff > 0) {
            ret.add("turn left to " + l._other.get_longName());
          } else {
            ret.add("turn right to " + l._other.get_longName());
          }
        } else {
          //          if (!l._other.get_nodeType().equals("HALL"))
          ret.add("continue straight to " + l._other.get_longName());
        }
      }
      previousDirection = currentDirection;
    }
    return ret;
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
