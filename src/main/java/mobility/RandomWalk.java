package mobility;

import mobility.MobilityModel;

import java.lang.Math;
import java.util.Random;

public class RandomWalk extends MobilityModel {

    private static final double MAX_VELOCITY = 4.0;
    protected static final Random rng = new Random();
    protected double velMag;

    public RandomWalk() {
        this(0.0, 0.0);
    }

    public RandomWalk(double x, double y) {
        this(x, y, rng.nextDouble() * MAX_VELOCITY);
    }

    public RandomWalk(double x, double y, double vel) {
        x_pos = x;
        y_pos = y;
        velMag = vel;
        x_vel = 0;
        y_vel = 0;
    }

    public void model(double time) {
        // Random walk in x,y (0,1)
        double xdiff = rng.nextDouble();
        double ydiff = rng.nextDouble();
        // Calculate new velocity
        double theta = Math.atan(ydiff/xdiff);
        x_vel = velMag * Math.cos(theta);
        y_vel = velMag * Math.sin(theta);
        // Update our position
        x_pos += x_vel * time;
        y_pos += y_vel * time;
    }

}
