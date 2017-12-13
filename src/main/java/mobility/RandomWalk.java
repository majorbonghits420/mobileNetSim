package mobility;

import mobility.MobilityModel;

import java.lang.Math;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Implements a random walk movement model
 */
public class RandomWalk extends MobilityModel {

    private static final double MAX_VELOCITY = 4.0; /**< Max velocity that is modeled */
    protected double velMag; /**< Velocity magnitude of this model */

    public RandomWalk() {
        this(0.0, 0.0);
    }

    public RandomWalk(double x, double y) {
        this(x, y, ThreadLocalRandom.current().nextDouble(1, MAX_VELOCITY));
    }

    public RandomWalk(double x, double y, double vel) {
        x_pos = x;
        y_pos = y;
        velMag = vel;
        x_vel = 0;
        y_vel = 0;
    }

    /**
     * Randomly walks is a given direction for the given time
     *
     * @param time to walk in a random direction
     */
    public void model(double time) {
        // Random walk in x,y (-1,1)
        double xdiff = ThreadLocalRandom.current().nextDouble(-1, 1);
        double ydiff = ThreadLocalRandom.current().nextDouble(-1, 1);
        x_vel = velMag * xdiff;
        y_vel = velMag * ydiff;
        // Update our position
        x_pos += x_vel * time;
        y_pos += y_vel * time;
    }

}
