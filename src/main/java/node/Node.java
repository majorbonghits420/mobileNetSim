package node;

public class Node {
    private double x_pos; /** X position of the node */
    private double y_pos; /** Y position of the node */
    private double x_vel; /** X velocity of the node */
    private double y_vel; /** Y velocity of the node */
    private double range; /** Data transfer range of the node */

    /**
     * Creates a node with all fields set to 0.
     *
     */
    public Node() {
        this(0.0, 0.0, 0.0, 0.0, 0.0);
    }

    /**
     * Node with an initial position x, y
     *
     * @param x the initial x position of the Node
     * @param y the initial y position of the Node
     */
    public Node(double x, double y) {
        this(x, y, 0.0, 0.0, 0.0);
    }

    /**
     * Node with an initial position x, y
     *
     * @param x     the initial x position of the Node
     * @param y     the initial y position of the Node
     * @param range the communication range of the Node
     */
    public Node(double x, double y, double range) {
        this(x, y, 0.0, 0.0, range);
    }

    /**
     * Node with an initial position x, y
     *
     * @param x     the initial x position of the Node
     * @param y     the initial y position of the Node
     * @param vx    the initail x velocity of the Node
     * @param vy    the initial y velocity of the Node
     * @param range the communication range of the Node
     */
    public Node(double x, double y, double vx, double vy, double range) {
        x_pos = x;
        y_pos = y;
        x_vel = vx;
        y_vel = vy;
        this.range = range;
    }
    /* Getters and setters */
    public double getXPos() {
        return x_pos;
    }

    public void setXPos(double x) {
        x_pos = x;
    }

    public double getYPos() {
        return y_pos;
    }

    public void setYPos(double y) {
        y_pos = y;
    }

    public double getXVel() {
        return x_vel;
    }

    public void setXVel(double vx) {
        x_vel = vx;
    }

    public double getYVel() {
        return y_vel;
    }

    public void setYVel(double vy) {
        y_vel = vy;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double r) {
        range = r;
    }

}
