package simulators;

import blockchain.Block;
import network.FastChannel;
import node.Node;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.List;

/**
 * Simulates and times the Spread of a single Block in a network of nodes
 */
public class SpreadSim extends Simulator {

    private Block toCheckFor; /**< The current Block we are checking every node gets */
    private double channelBw; /**< Bandwidth of created channels */
    private double currentTime; /**< Current time in the simulation */
    private double blockGenTime; /**< Time when the Block being distributed was generated */

    /**
     * Creates a SpreadSim with the given nodes, timestep, and bandwidth
     *
     * @param timestep The timestep the simulation should take in seconds
     * @param nodes Nodes to simulate
     * @param bandwidth The max bandwidth of created channels between nodes
     */
    public SpreadSim(double timestep, List<Node> nodes, double bandwidth) {
        super(timestep, nodes);
        // Set to Genisis as default
        toCheckFor= Block.getGenisis();
        channelBw = bandwidth;
        blockGenTime = 0.0;
        currentTime = 0.0;
    }

    /**
     * Creates a SpreadSim with the given timestep, and bandwidth.
     * Nodes must be added later for simulation to be useful
     *
     * @param timestep The timestep the simulation should take in seconds
     * @param nodes Nodes to simulate
     */
    public SpreadSim(double timestep, double bandwidth) {
        this(timestep, new ArrayList<Node>(), bandwidth);
    }

    /**
     * Has the node at the given index generate a block. This will be the block that is checked
     * for how many receive it after a given duration.
     */
    public void init(int index) {
        toCheckFor = nodes.get(index).generateBlock();
        blockGenTime = currentTime;
    }

    /**
     * Runs the simulation for a given duration.
     * Simulates a block being distributed to all nodes, once this occurs, it randomly chooses a node to generate a block.
     * It then waits for this block to spread throughout the network. This is repeated until the duration is hit. When
     * a block makes it to all blocks the amount of time it took is outputted to STDOUT
     *
     * @param duration How much simulated time should occur in seconds
     */
    public void run(double duration) {
        do {
            currentTime += timeStep;
            // Update positions of all nodes
            for (Node n : nodes) {
                n.move(timeStep);
            }
            // Create channels between nodes that are close enough to each other
            for (int i = 0; i < nodes.size() - 1; i++) {
                for (int j = i + 1; j < nodes.size(); j++) {
                    Node a = nodes.get(i);
                    Node b = nodes.get(j);
                    // If the two nodes are not and can connect, create channel
                    if (a.canConnect(b) && !a.isConnected(b) && !b.isConnected(a)) {
                        FastChannel channel = new FastChannel(a, b, channelBw);
                        a.addChannel(channel);
                    }
                }
            }

            // If all Nodes have the Block, pick a random node to generate a new one
            if (allNodesContain(toCheckFor)) {
                // Output how long it took to spread the Block, update vars
                System.out.print((currentTime - blockGenTime) + ", ");

                Block newBlock = generateRandomBlock();
                toCheckFor = newBlock;
                blockGenTime = currentTime;
            }

            // If we have met our duration, exit
        } while (currentTime < duration);
    }

    /**
     * Returns how many nodes in the network currently have the Block we are checking for
     *
     * @return percentage of nodes containing the current Block
     */
    public double percentRecieved() {
        double recvd = 0;
        for(Node n : nodes) {
            if (n.containsBlock(toCheckFor)) {
                recvd = recvd + 1.0;
            }
        }
        return (recvd / nodes.size()) * 100.0;
    }

    /**
     * Selected a random node in the simulation to randomly generate a Block.
     *
     * @return The generated block from the randomly selected node
     */
    protected Block generateRandomBlock() {
        Block newBlock = null;
        while (newBlock == null) {
            int randNodeIndex = ThreadLocalRandom.current().nextInt(0, nodes.size());
            newBlock = nodes.get(randNodeIndex).generateBlock();
        }
        return newBlock;
    }
    /**
     * Checks if all nodes in the simulation contatin a given Block
     *
     * @param b The block to check each node for
     * @return true is every block contains the given Block, false otherwise
     */
    private boolean allNodesContain(Block b) {
        boolean spread = true;
        for (Node n : nodes) {
            spread = spread && n.containsBlock(toCheckFor);
        }
        return spread;
    }
}
