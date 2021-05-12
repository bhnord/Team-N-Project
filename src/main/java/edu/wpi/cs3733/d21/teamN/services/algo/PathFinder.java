package edu.wpi.cs3733.d21.teamN.services.algo;

import java.util.ArrayList;

public class PathFinder implements IPathFinder {
  private IPathFinder impl;
  private static PathFinder singleton = new PathFinder();

  public static void setImpl(IPathFinder impl) {
    singleton.impl = impl;
  }

  private PathFinder() {
    this.impl = new Astar();
  }

  public static PathFinder getPathFinder() {
    return singleton;
  }

  public ArrayList<Node.Link> pathfind(Node start, Node end) {
    return singleton.impl.pathfindFull(start, end, (l) -> true);
  }

  public ArrayList<Node.Link> pathfindNoStairs(Node start, Node end) {
    return singleton.impl.pathfindFull(start, end, (l) -> true);
  }

  @Override
  public ArrayList<Node.Link> pathfindFull(Node start, Node end, Reduce filter) {
    return singleton.impl.pathfindFull(start, end, filter);
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
    return ret;
  }

  public static double normalizeAngle(double angle) {
    return Math.atan2(Math.sin(angle), Math.cos(angle));
  }

  public ArrayList<String> getDescription(ArrayList<Node.Link> input) {
    if (input.size() == 0) {
      ArrayList<String> ret = new ArrayList<>();
      ret.add("No Path");
      return ret;
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
      double directionDiff = normalizeAngle(currentDirection - previousDirection);

      System.out.println(i + " : " + directionDiff);
      if (!l._this.get_floor().equals(l._other.get_floor())) {
        ret.add("go to floor " + l._other.get_floor());
      } else {
        if (directionDiff > minAngle || directionDiff < (-minAngle)) {
          if (directionDiff > 0) {
            ret.add("turn left to " + l._other.get_longName() + ": (" + getDistance(l) + " ft)");
          } else {
            ret.add("turn right to " + l._other.get_longName() + ": (" + getDistance(l) + " ft)");
          }
        } else {
          //          if (!l._other.get_nodeType().equals("HALL"))
          ret.add(
              "continue straight to " + l._other.get_longName() + ": (" + getDistance(l) + " ft)");
        }
      }
      previousDirection = currentDirection;
    }
    return ret;
  }
  // just a helper function to get estimated distance
  private int getDistance(Node.Link l) {
    return (int) l._distance;
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

  private double getLen(Node.Link input) {
    return Math.sqrt(
        (input._this.get_x() - input._other.get_x()) * (input._this.get_x() - input._other.get_x())
            + (input._this.get_y() - input._other.get_y())
                * (input._this.get_y() - input._other.get_y()));
  }

  public double getTotalLen(ArrayList<Node.Link> links) {
    double totalLen = 0;
    for (Node.Link l : links) {
      totalLen += getLen(l);
    }
    return totalLen;
  }

  public double[] getParametric(double percent, ArrayList<Node.Link> links, String floor) {
    if (links.size() == 0) {
      return new double[] {-1, -1, 0};
    }
    double totalLen = 0;
    for (Node.Link l : links) {
      totalLen += getLen(l);
    }
    for (int i = links.size() - 1; i >= 0; i--) {
      double curPercent = getLen(links.get(i)) / totalLen;
      percent -= curPercent;
      if (percent < 0) {
        percent += curPercent;
        percent /= curPercent;
        double dx = -(links.get(i)._this.get_x() - links.get(i)._other.get_x());
        double dy = -(links.get(i)._this.get_y() - links.get(i)._other.get_y());
        dx *= percent;
        dy *= percent;
        dx += links.get(i)._this.get_x();
        dy += links.get(i)._this.get_y();
        double bool = 0;
        if (links.get(i)._this.get_floor().equals(floor)) {
          bool = 1;
        }
        return new double[] {dx, dy, bool};
      }
    }
    double bool = 0;
    if (links.get(links.size() - 1)._this.get_floor().equals(floor)) {
      bool = 1;
    }
    return new double[] {
      links.get(links.size() - 1)._this.get_x(), links.get(links.size() - 1)._this.get_y(), bool
    };
  }
}
