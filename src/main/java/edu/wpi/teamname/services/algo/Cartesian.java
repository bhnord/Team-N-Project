package edu.wpi.teamname.services.algo;

public class Cartesian extends Node<Cartesian>{
    float _x,_y;
    @Override
    public float hueristic(Cartesian other){
        return (other._x - this._x) * (other._x - this._x) + (other._y - this._y) * (other._y - this._y);
    }
}

