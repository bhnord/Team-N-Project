package edu.wpi.teamname.services.algo;

import java.util.*;

public class PathFinder<T extends Node<T>> {
  /**
   * @param start node to start search from
   * @param end target node
   * @return a stack of nodes containing the path from the start to the end
   */
  public Stack<Node<T>> Astar(Node<T> start, Node<T> end) {
    // Node.reset() should be called on every node accessible to start
    Node<T> curNode;
    start._localGoal = 0;
    start._globalGoal = start.heuristic(end);
    PriorityQueue<Node<T>> open = new PriorityQueue<>();
    ArrayList<Node<T>> used = new ArrayList<>();
    used.add(start);
    open.add(start);
    do {
      curNode = open.remove();
      curNode._seen = true;
      if (curNode == end) {
        break;
      }
      for (Node<T>.Link l : curNode._neighbors) {
        Node<T> n = l._other;
        int local = curNode._localGoal + l._distance;
        if (!n._seen || n._localGoal > local) {
          n._localGoal = local;
          n._globalGoal = n.heuristic(end);
          n._parent = curNode;
          open.add(n);
          used.add(n);
        }
      }
    } while (!open.isEmpty());
    Stack<Node<T>> ret = rebuild(start, end);
    // reset manipulated nodes
    for (Node<T> n : used) {
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
  private Stack<Node<T>> rebuild(Node<T> start, Node<T> curNode) {
    Stack<Node<T>> ret = new Stack<>();
    while (curNode != null) {
      ret.push(curNode);
      curNode = curNode._parent;
    }
    return ret;
  }
}
