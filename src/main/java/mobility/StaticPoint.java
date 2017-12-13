package mobility;

/**
 * This class simulates a static point in the environment.
 * It is initialized with a point and stays there.
 */
public class StaticPoint extends MobilityModel {

    /**
     * Creates the point with a set position
     *
     * @param x x coordinate in meters
     * @param y y coordinate in meters
     */
    public StaticPoint(double x, double y) {
        this.x_pos = x;
        this.y_pos = y;
        this.x_vel = 0;
        this.y_vel = 0;
    }

    /**
     * Simply returns as there is nothing to do since the point doesn't move
     */
    public void model(double time) {
        return;
    }

}
