import node.Node;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

public class Simulator {
    private ArrayList<Node> nodes;
    private double timeStep; /** The timestep of the simulation in seconds. */

    Simulator(double timeStep) {
        this(timeStep, new ArrayList<Node>());
    }

    Simulator(double timeStep, List<Node> nodes) {
        this.nodes = new ArrayList<Node>(nodes);
        this.timeStep = timeStep;
    }

    /**
     * Initializes the simulator
     */
    public void init() {}

    /**
     * Performs the simulation for a given duration of time.
     *
     */
    public abstract void run(double duration);

    /* Getters and setters */
    public double getTimeStep() {
        return timeStep;
    }

    public void setTimeStep(double t) {
        timeStep = t;
    }

    public void addNode(Node n) {
        nodes.add(n);
    }

    public List<Node> getNodes() {
        return nodes;
    }

}
