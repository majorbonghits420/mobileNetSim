package mobility;

/**
 * This class simulates a static point in the environment.
 * It is initialized with a point and stays there.
 */
public class StaticPoint extends MobilityModel {

    public StaticPoint(double x, double y) {
        this.x_pos = x;
        this.y_pos = y;
        this.x_vel = 0;
        this.y_vel = 0;
    }

    public void model(double time) {
        return;
    }

}
