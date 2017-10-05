package node;

import mobility.MobilityModel;

public class Node {
    private MobilityModel model; /** Model used for determining position/veolicty of node */
    private double range; /** Data transfer range of the node in meters */

    /**
     * Creates a node with all fields set to 0.
     *
     */
    public Node(MobilityModel m) {
        this(m, 0.0);
    }

    /**
     * Node with an initial position x, y
     *
     * @param x the initial x position of the Node
     * @param y the initial y position of the Node
     */
    public Node(MobilityModel m, double range) {
        this.model = m;
        this.range = range;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double r) {
        range = r;
    }

    /**
     * Model the movement of this node for a period of time
     */
    public void move(double time) {
        model.model(time);
    }

}
