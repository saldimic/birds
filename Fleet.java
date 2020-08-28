package it.saldimic.birds;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class Fleet {
    List<Bird> birds;
    
    Fleet() {
        birds = new ArrayList<Bird>();
    }
 
    void run(Graphics2D g,  int w, int h) {
        for (Bird b : birds) {
            b.run(g, birds, w, h);
        }
    }
 
    boolean hasLeftTheBuilding(int w) {
        int count = 0;
        for (Bird b : birds) {
            if (b.location.x + Bird.size > w)
                count++;
        }
        return birds.size() == count;
    }
 
    void addBird(Bird b) {
        birds.add(b);
    }
 
    static Fleet spawn(double w, double h, int numBirds, int sizeBird) {
    	Fleet flock = new Fleet();
        for (int i = 0; i < numBirds; i++) {
//        	Bird.size = sizeBird;
            flock.addBird(new Bird(w, h, sizeBird));
        }
        return flock;
    }

}
