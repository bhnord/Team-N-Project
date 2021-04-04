package edu.wpi.teamname.services.algo;

import java.util.*;

public class PathFinder {
    /**
     *
     * @param start node to start search from
     * @param end target node
     * @return a stack of nodes containing the path from the start to the end
     */
    Stack<Node> Astar(Node start, Node end){
        // Node.reset() should be called on every node accessible to start
        Node curNode;
        start._localGoal = 0;
        start._globalGoal = start.hueristic(end);
        PriorityQueue<Node> open = new PriorityQueue<>();
        ArrayList<Node> used = new ArrayList<>();
        used.add(start);
        open.add(start);
        do {
            curNode = open.remove();
            curNode._seen = true;
            for (Node.Link l : (List<Node.Link>)curNode._neighbors) {
                Node n = l._other;
                float local = curNode._localGoal + l._distance;
                if (!n._seen || n._localGoal < local) {
                    n._localGoal = local;
                    n._globalGoal = n.hueristic(end);
                    n._parent = curNode;
                    open.add(n);
                    used.add(n);
                }
            }
        } while(!open.isEmpty());
        // reset manipulated nodes
        for(Node n : used){
           n.reset();
        }
        return rebuild(start,curNode);
    }
    /**
     *
     * @param start node to walk back to
     * @param curNode node to start walking back from
     * @return a stack of nodes where start is at the top and every next node's parent is the previous node in the stack
     */
    private Stack<Node> rebuild(Node start, Node curNode){
        Stack<Node> ret = new Stack<>();
        while(curNode != start){
            ret.push(curNode);
            curNode = curNode._parent;
        }
        return ret;
    }
}
