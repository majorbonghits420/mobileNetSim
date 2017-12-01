package simulators;

import node.Node;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class that defines what all simulations will have. Each simulation will have a timestep
 * and a set of nodes associated with it which will be simulated. The Simulator object will be in
 * charge of simulating each node, creating channels between nodes, and updating any other state
 * that is specific to the given simulation.
 */
public abstract class Simulator {
    private ArrayList<Node> nodes; /**< A list of all the nodes contained in the simulation */
    private double timeStep; /**< The timestep of the simulation in seconds. */

    /**
     * Constructs a simulator object with a given timestep and no associated Nodes.
     *
     * @param timeStep The timestep in seconds of the simulation
     */
    protected Simulator(double timeStep) {
        this(timeStep, new ArrayList<Node>());
    }

    /**
     * Constructs a simulator object with a given timestep and list of accosciated nodes/
     *
     * @param timeStep The timestep in seconds of the simulation
     * @param nodes A List of Nodes that are to be simulated
     */
    protected Simulator(double timeStep, List<Node> nodes) {
        this.nodes = new ArrayList<Node>(nodes);
        this.timeStep = timeStep;
    }

    /**
     * Performs the simulation for a given duration of time.
     *
     * @param duration A double describing the amount of time to run the simulation in seconds
     */
    public abstract void run(double duration);

    /* Getters and setters */
    /**
     * Returns the timestep of the simulation
     *
     * @return double representing the timestep in seconds
     */
    public double getTimeStep() {
        return timeStep;
    }

    /**
     * Sets the timestep for the simulation
     *
     * @param t A double representing the new timestep for the simulation in seconds
     */
    public void setTimeStep(double t) {
        timeStep = t;
    }

    /**
     * Adds a node to the list of nodes to be included in the simulation
     *
     * @param n A Node object to add to be simulated by this object
     */
    public void addNode(Node n) {
        nodes.add(n);
    }

    /**
     * Returns the list of nodes simulated by this Simulator object
     *
     * @param A list of Nodes
     */
    public List<Node> getNodes() {
        return nodes;
    }

}
