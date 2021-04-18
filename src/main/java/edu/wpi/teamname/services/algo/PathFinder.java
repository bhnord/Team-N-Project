package edu.wpi.teamname.services.algo;

import java.util.*;

public class PathFinder {
  /**
   * @param start node to start search from
   * @param end target node
   * @return a stack of nodes containing the path from the start to the end
   */
  public Stack<Node> Astar(Node start, Node end) {
    // Node.reset() should be called on every node accessible to start
    Node curNode;
    start.set_localGoal(0);
    start.set_globalGoal(start.heuristic(end));
    PriorityQueue<Node> open = new PriorityQueue<>();
    ArrayList<Node> used = new ArrayList<>();
    used.add(start);
    open.add(start);
    do {
      curNode = open.remove();
      curNode.set_seen(true);
      //      if (curNode == end) {
      //        break;
      //      }
      for (Edge l : curNode.get_neighbors()) {
        Node n = l.get_endNode();
        double local = curNode.get_localGoal() + l.get_distance();
        if (!n.is_seen() || n.get_localGoal() > local) {
          n.set_localGoal(local);
          n.set_globalGoal(n.heuristic(end));
          n.set_parent(curNode);
          open.add(n);
          used.add(n);
        }
      }
    } while (!open.isEmpty());
    Stack<Node> ret = rebuild(start, end);
    // reset manipulated nodes
    for (Node n : used) {
      n.reset();
    }
    return ret;
  }
  /**
   * @param start node to walk back to
   * @param curNode node to start walking back from
   * @return a stack of nodes where start is at the top and every next node's parent is the previous
   *     node in the stack
   */
  private Stack<Node> rebuild(Node start, Node curNode) {
    Stack<Node> ret = new Stack<>();
    while (curNode != null) {
      ret.push(curNode);
      curNode = curNode.get_parent();
    }
    return ret;
  }
}
