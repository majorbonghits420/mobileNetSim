package simulators;

import blockchain.Block;
import network.FastChannel;
import node.Node;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.List;

public class SpreadSim extends Simulator {

    private Block toCheckFor; /**< The current Block we are checking every node gets */
    private double channelBw; /**< Bandwidth of created channels */
    private double currentTime;
    private double blockGenTime;

    public SpreadSim(double timestep, List<Node> nodes, double bandwidth) {
        super(timestep, nodes);
        // Set to Genisis as default
        toCheckFor= Block.getGenisis();
        channelBw = bandwidth;
        blockGenTime = 0.0;
        currentTime = 0.0;
    }

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
                System.out.println((currentTime - blockGenTime));

                int randNodeIndex = ThreadLocalRandom.current().nextInt(0, nodes.size());
                Block newBlock = nodes.get(randNodeIndex).generateBlock();
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

    private boolean allNodesContain(Block b) {
        boolean spread = true;
        for (Node n : nodes) {
            spread = spread && n.containsBlock(toCheckFor);
        }
        return spread;
    }
}
