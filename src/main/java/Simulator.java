import node.Node;
import mobility.MobilityModel;

import java.util.ArrayList;
import java.util.List;

public class Simulator {
    private ArrayList<Node> nodes;
    private double timeStep; /** The timestep of the simulation in seconds. */
    private MobilityModel model;

    Simulator(double timeStep, MobilityModel m) {
        this(timeStep, new ArrayList<Node>(), m);
    }

    Simulator(double timeStep, List<Node> nodes, MobilityModel m) {
        this.nodes = new ArrayList<Node>(nodes);
        this.timeStep = timeStep;
        this.model = m;
    }

    /**
     * This function performs the full simulation.
     *
     */
    public void run() {
        // Loop through all nodes to update their positions

        // Find nodes that are mutually in range of each other

        // Send data between connected nodes
    }

    /* Getters and setters */
    public void setMobilityModel(MobilityModel m) {
        model = m;
    }

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

    public MobilityModel getMobilityModel() {
        return model;
    }
}
