package network;

import node.Node;
import utils.Tuple;

import java.lang.Math;

/**
 * Abstract class that represents a data channel between two nodes.
 */
public abstract class Channel {
    protected Node a; /**< First endpoint of the chaneel */
    protected Node b; /**< Second endpoint of the channel */
    protected double bandwidth; /**< The bandwidth of the channel in Bytes per sec*/

    /**
     * Creates a channel between nodes a and b.
     */
    protected Channel(Node a, Node b, double bw) {
        this.a = a;
        this.b = b;
        double availB = b.availableBandwidth();
        double availA = a.availableBandwidth();
        double avail = Math.min(availA, availB);
        double attemptBandwidth = Math.min(avail, bw);
        // If we are able to claim bandwidth from the node
        if (a.useBandwidth(attemptBandwidth) && b.useBandwidth(attemptBandwidth)) {
            bandwidth = attemptBandwidth;
            // The node cannot give us any bandwidth
        } else {
            bandwidth = 0.0;
        }
    }

    /**
     * Tears down the channel, highly important to call before removing channel from a Node.
     * Returns taken bandwidth used to the Nodes the Channel is made between
     */
    public void tearDown() {
        a.returnBandwidth(bandwidth);
        b.returnBandwidth(bandwidth);
    }

    /**
     * Updates the data transfer/state machine for a given amount of time
     */
    public abstract void update(double timestep);

    /**
     * Returns true if our data transfer/state machine is finished. False otherwise
     */
    public abstract boolean finished();

    /**
     * Returns true if the connection has timed out, false otherwise
     */
    public abstract boolean timedout();

    /**
     * Returns true if this channel contains the given node
     *
     * @param n Node
     * @return true if node is an endpoint of the channel, false otherwise
     */
    public boolean hasNode(Node n) {
        return (this.a == n || this.b == n);
    }

    /**
     * Returns a Tuple containing both endpoints of the Channel
     *
     * @return Tuple with two Node endpoints
     */
    public Tuple<Node, Node> getNodes() {
        return new Tuple<Node, Node>(a, b);
    }

    public double getBandwith() {
        return bandwidth;
    }

    /**
     * Sets the bandwidth of the channel
     *
     * @param bw bandwidth to set in bytes per second
     */
    public void setBandwidth(double bw) {
        bandwidth = bw;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null || !(obj instanceof Channel)) {
            return false;
        }
        Channel c = (Channel) obj;
        return ((this.a == c.a && this.b == c.b) || (this.a == c.b && this.b == c.a));
    }

    @Override
    public int hashCode() {
        return a.hashCode() + b.hashCode();
    }
}
