package mobility;

import node.Node;

import java.util.List;

/**
 * Abstract class used to represent the modeling of a given type of movement
 */
public abstract class MobilityModel {
    protected double x_pos; /**< X position of the node */
    protected double y_pos; /**< Y position of the node */
    protected double x_vel; /**< X velocity of the node */
    protected double y_vel; /**< Y velocity of the node */

    /**
     * Model a timestep for the given amount of time
     *
     * @param time duration to model movement in seconds
     */
    public abstract void model(double time);

    /* Getters and setters */
    public double getXPos() {
        return x_pos;
    }

    public double getYPos() {
        return y_pos;
    }

    public double getXVel() {
        return x_vel;
    }

    public double getYVel() {
        return y_vel;
    }
}
