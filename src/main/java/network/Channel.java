package network;

import node.Node;
import utils.Tuple;

public abstract class Channel {
    private Node a;
    private Node b;

    /**
     * Creates a channel between nodes a and b.
     */
    public Channel(Node a, Node b) {
        this.a = a;
        this.b = b;
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
