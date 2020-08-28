package it.saldimic.birds;

import static java.lang.Math.PI;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.List;
import java.util.Random;

public class Bird {
    static final VectorMotion migrate = new VectorMotion(0.02, 0);
    static final Random rand = new Random();
    static int size = 1;
    public static int getSize() {
		return size;
	}

	public static void setSize(int size) {
		Bird.size = size;
	}

	static Path2D shape = new Path2D.Double();
 
    static {
        shape.moveTo(0, -size * 2);
        shape.lineTo(-size, size * 2);
        shape.lineTo(size, size * 2);
        shape.closePath();        
    }
 
    final double maxForce, maxSpeed;
 
    VectorMotion location, velocity, acceleration;
    private boolean included = true;
 
    Bird(double x, double y, int sizeBird) {
    	size = sizeBird;
//        shape.moveTo(0, -size * 2);
//        shape.lineTo(-size, size * 2);
//        shape.lineTo(size, size * 2);
//        shape.closePath();
        acceleration = new VectorMotion();
        velocity = new VectorMotion(rand.nextInt(3) + 1, rand.nextInt(3) - 1);
        location = new VectorMotion(x, y);
        maxSpeed = 3.0;
        maxForce = 0.05;
    }
     
    void update() {
        velocity.add(acceleration);
        velocity.limit(maxSpeed);
        location.add(velocity);
        acceleration.mult(0);
    }
 
    void applyForce(VectorMotion force) {
        acceleration.add(force);
    }
 
    VectorMotion seek(VectorMotion target) {
    	VectorMotion steer = VectorMotion.sub(target, location);
        steer.normalize();
        steer.mult(maxSpeed);
        steer.sub(velocity);
        steer.limit(maxForce);
        return steer;
    }
 
    void flock(Graphics2D g, List<Bird> birds) {
        view(g, birds);
 
        VectorMotion rule1 = separation(birds);
        VectorMotion rule2 = alignment(birds);
        VectorMotion rule3 = cohesion(birds);
 
        rule1.mult(2.5);
        rule2.mult(1.5);
        rule3.mult(1.3);
 
        applyForce(rule1);
        applyForce(rule2);
        applyForce(rule3);
        applyForce(migrate);
    }
 
    void view(Graphics2D g, List<Bird> birds) {
        double sightDistance = 100;
        double peripheryAngle = PI * 0.85;
 
        for (Bird b : birds) {
            b.included = false;
 
            if (b == this)
                continue;
 
            double d = VectorMotion.dist(location, b.location);
            if (d <= 0 || d > sightDistance)
                continue;
 
            VectorMotion lineOfSight = VectorMotion.sub(b.location, location);
 
            double angle = VectorMotion.angleBetween(lineOfSight, velocity);
            if (angle < peripheryAngle)
                b.included = true;
        }
    }
 
    VectorMotion separation(List<Bird> birds) {
        double desiredSeparation = 25;
 
        VectorMotion steer = new VectorMotion(0, 0);
        int count = 0;
        for (Bird b : birds) {
            if (!b.included)
                continue;
 
            double d = VectorMotion.dist(location, b.location);
            if ((d > 0) && (d < desiredSeparation)) {
            	VectorMotion diff = VectorMotion.sub(location, b.location);
                diff.normalize();
                diff.div(d);        // weight by distance
                steer.add(diff);
                count++;
            }
        }
        if (count > 0) {
            steer.div(count);
        }
 
        if (steer.mag() > 0) {
            steer.normalize();
            steer.mult(maxSpeed);
            steer.sub(velocity);
            steer.limit(maxForce);
            return steer;
        }
        return new VectorMotion(0, 0);
    }
 
    VectorMotion alignment(List<Bird> birds) {
        double preferredDist = 50;
 
        VectorMotion steer = new VectorMotion(0, 0);
        int count = 0;
 
        for (Bird b : birds) {
            if (!b.included)
                continue;
 
            double d = VectorMotion.dist(location, b.location);
            if ((d > 0) && (d < preferredDist)) {
                steer.add(b.velocity);
                count++;
            }
        }
 
        if (count > 0) {
            steer.div(count);
            steer.normalize();
            steer.mult(maxSpeed);
            steer.sub(velocity);
            steer.limit(maxForce);
        }
        return steer;
    }
 
    VectorMotion cohesion(List<Bird> birds) {
        double preferredDist = 50;
 
        VectorMotion target = new VectorMotion(0, 0);
        int count = 0;
 
        for (Bird b : birds) {
            if (!b.included)
                continue;
 
            double d = VectorMotion.dist(location, b.location);
            if ((d > 0) && (d < preferredDist)) {
                target.add(b.location);
                count++;
            }
        }
        if (count > 0) {
            target.div(count);
            return seek(target);
        }
        return target;
    }
 
    void draw(Graphics2D g) {
        AffineTransform save = g.getTransform();
 
        g.translate(location.x, location.y);
        g.rotate(velocity.heading() + PI / 2);
        g.setColor(Color.green);
        g.fill(shape);
        g.setColor(Color.red);
        g.draw(shape);
 
        g.setTransform(save);
    }
 
    void run(Graphics2D g, List<Bird> birds, int w, int h) {
        flock(g, birds);
        update();
        draw(g);
    }

}
