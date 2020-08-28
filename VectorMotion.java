package it.saldimic.birds;

import static java.lang.Math.acos;
import static java.lang.Math.atan2;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class VectorMotion {
    double x, y;
    
    VectorMotion() {
    }
 
    VectorMotion(double x, double y) {
        this.x = x;
        this.y = y;
    }
 
    void add(VectorMotion v) {
        x += v.x;
        y += v.y;
    }
 
    void sub(VectorMotion v) {
        x -= v.x;
        y -= v.y;
    }
 
    void div(double val) {
        x /= val;
        y /= val;
    }
 
    void mult(double val) {
        x *= val;
        y *= val;
    }
 
    double mag() {
        return sqrt(pow(x, 2) + pow(y, 2));
    }
 
    double dot(VectorMotion v) {
        return x * v.x + y * v.y;
    }
 
    void normalize() {
        double mag = mag();
        if (mag != 0) {
            x /= mag;
            y /= mag;
        }
    }
 
    void limit(double lim) {
        double mag = mag();
        if (mag != 0 && mag > lim) {
            x *= lim / mag;
            y *= lim / mag;
        }
    }
 
    double heading() {
        return atan2(y, x);
    }
 
    static VectorMotion sub(VectorMotion v, VectorMotion v2) {
        return new VectorMotion(v.x - v2.x, v.y - v2.y);
    }
 
    static double dist(VectorMotion v, VectorMotion v2) {
        return sqrt(pow(v.x - v2.x, 2) + pow(v.y - v2.y, 2));
    }
 
    static double angleBetween(VectorMotion v, VectorMotion v2) {
        return acos(v.dot(v2) / (v.mag() * v2.mag()));
    }

}
