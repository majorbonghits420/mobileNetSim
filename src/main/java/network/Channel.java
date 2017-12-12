package network;

import node.Node;
import utils.Tuple;

import java.lang.Math;

public abstract class Channel {
    protected Node a;
    protected Node b;
    protected double bandwidth;

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

    public boolean hasNode(Node n) {
        return (this.a == n || this.b == n);
    }

    public Tuple<Node, Node> getNodes() {
        return new Tuple<Node, Node>(a, b);
    }

    public double getBandwith() {
        return bandwidth;
    }

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
