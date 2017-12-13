package mobility;

import node.Node;
import utils.Tuple;

import java.lang.Math;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class simulates movement by assigning a node a destination waypoint.
 * The node then attempts to take the shortest path to this waypoint.
 * Once the node reaches the waypoint, the node gets a new waypoint to move towards.
 * Every waypoint is inside a bounded area, the dimensions of which are set by the
 * constructor.
 */
public class RandomWaypoint extends MobilityModel {
    public static final double MAX_VELOCITY = 4.0; /**< Max velicoty in meters per second */
    public static final double POINT_RADIUS = 2.0; /**< Meters away from point from which it is considered at the waypoint */
    protected Tuple<Double, Double> waypoint; /**< Waypoint that is being moved to */
    protected double xBound; /**< Max x cooridnate a waypoint can be inside of */
    protected double yBound; /**< Max y coordinate a waypoint can be inside of */

    /**
     * Takes in the x and y bounds for the area of the model.
     * The area if then bounded by a rectangle with top left point of [-maxX, -maxY]
     * and bottom right point of [maxX, maxY]
     */
    public RandomWaypoint(double maxX, double maxY) {
        this.xBound = maxX;
        this.yBound = maxY;
        waypoint = getRandPt();
        this.x_pos = getRandNum(maxX);
        this.y_pos = getRandNum(maxY);
        headToPt(waypoint);
    }

    /**
     * Simulates for time seconds moving to a random waypoint.
     */
    public void model(double time) {
        if (atPoint()) {
            // Set a new waypoint, set velocity to head towards it
            waypoint = getRandPt();
            headToPt(waypoint);
        } else {
            x_pos += x_vel * time;
            y_pos += y_vel * time;
        }
    }

    /**
     * Generates a random point inside the model bounds
     */
    private Tuple<Double, Double> getRandPt() {
        double x = ThreadLocalRandom.current().nextDouble(-1 * xBound, xBound);
        double y = ThreadLocalRandom.current().nextDouble(-1 * yBound, yBound);
        return new Tuple<Double, Double>(x,y);
    }

    private double getRandNum(double bound) {
        return ThreadLocalRandom.current().nextDouble(-1 * bound, bound);
    }

    private void headToPt(Tuple<Double, Double> pt) {
        double xDist = waypoint.left() - x_pos;
        double yDist = waypoint.right() - y_pos;
        double norm = Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));
        // Have the +1 so we are at least moving at 1 m/s
        norm = norm * (1 / ThreadLocalRandom.current().nextDouble(1, MAX_VELOCITY));
        x_vel = xDist / norm;
        y_vel = yDist / norm;
    }

    /**
     * Returns true if within the proper radius of a point
     */
    private boolean atPoint() {
        double dist = Math.pow((waypoint.left() - x_pos), 2) + Math.pow((waypoint.right() - y_pos), 2);
        return (dist < Math.pow(POINT_RADIUS, 2));
    }
}
