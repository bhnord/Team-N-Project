package edu.wpi.teamname.services.algo;

import java.util.ArrayList;
import java.util.List;

public abstract class Node <T extends Node> implements Comparable<Node> {
    public static class Link{
       float _distance;
       Node _other;
    }
    float _localGoal;
    float _globalGoal;
    boolean _seen;
    Node _parent;
    List<Link> _neighbors;

    public Node(){
        _neighbors = new ArrayList<>();
        reset();
    }

    /**
     * resets all node parameters to default
     */
    public void reset(){
        this._localGoal = 0;
        this._globalGoal = 0;
        this._seen = false;
        this._parent = null;
    }
    abstract float hueristic(T other);
    @Override
    public int compareTo(Node Other){
        return (this._localGoal + this._globalGoal) > (this._localGoal + this._globalGoal) ? 1 : -1;
    }
}

