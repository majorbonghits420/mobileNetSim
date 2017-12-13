package node;

import blockchain.Block;
import blockchain.Blockchain;
import mobility.MobilityModel;
import network.Channel;
import utils.Sha256;

import java.lang.Math;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.List;

/**
 * A generic Node in a network that makes connections to other nodes and maintains a blockchain.
 * Actively tries to get and send blocks to other Nodes it has channels with.
 */
public class Node {
    private MobilityModel model; /**< Model used for determining position/veolicty of node */
    private double range; /**< Data transfer range of the node in meters */
    private ArrayList<Channel> channels; /**< All currently open channels */
    private Blockchain chain; /**< The blockchain maintained by the Node */
    private double maxBw; /**< The max bandwidth the node can use at any given time */
    private double availableBw; /**< The amount of bandwidth available to give to channels */

    /**
     * Creates a node with all fields set to 0.
     *
     */
    public Node(MobilityModel m, double maxBandwidth) {
        this(m, maxBandwidth, 0.0);
    }

    /**
     * Node with an initial position x, y
     *
     * @param x the initial x position of the Node
     * @param y the initial y position of the Node
     */
    public Node(MobilityModel m, double maxBandwidth, double range) {
        this.model = m;
        this.range = range;
        channels = new ArrayList<Channel>();
        chain = new Blockchain(Block.getGenisis());
        maxBw = maxBandwidth;
        availableBw = maxBandwidth;
    }

    public double availableBandwidth() {
        return availableBw;
    }

    public double getMaxBandwidth() {
        return maxBw;
    }

    public boolean useBandwidth(double bw) {
        if (bw > availableBw) {
            return false;
        }
        availableBw -= bw;
        return true;
    }

    public void returnBandwidth(double bw) {
        availableBw = Math.min(availableBw + bw, maxBw);
    }

    /**
     * Adds a channel to the list of open channels.
     * Will not add a duplicate channel.
     */
    public void addChannel(Channel c) {
        channels.add(c);
    }

    /**
     * Returns true is Node n has an open channel with this node, false otherwise.
     */
    public boolean isConnected(Node n) {
        boolean toReturn = false;
        for (Channel c: channels) {
            toReturn = toReturn || c.hasNode(n);
        }
        return toReturn;
    }

    public double getFrontierSize() {
        return chain.getFrontierHashes().size() * Sha256.HASH_SIZE;
    }

    public List<Block> getFrontier() {
        return chain.getFrontier();
    }

    public List<Sha256> getFrontierHashes() {
        return chain.getFrontierHashes();
    }

    public boolean containsBlock(Block b) {
        return chain.containsBlock(b);
    }

    public boolean containsBlock(Sha256 hash) {
        return chain.containsBlock(hash);
    }

    public Block getBlock(Sha256 hash) {
        return chain.getBlock(hash);
    }

    public void addBlock(Block b) {
        chain.addBlock(b);
    }

    public void addBlocks(List<Block> blocks) {
        chain.addBlocks(blocks);
    }

    public double getRange() {
        return range;
    }

    public void setRange(double r) {
        range = r;
    }

    public double getXPos() {
        return model.getXPos();
    }

    public double getYPos() {
        return model.getYPos();
    }

    public int numBlocks() {
        return chain.size();
    }

    /**
     * Generates a random block that stems from a frontier node.
     * This block is stored on the node's chain and returned.
     */
    public Block generateBlock() {
        List<Sha256> frontier = chain.getFrontierHashes();
        int randIndex = ThreadLocalRandom.current().nextInt(0, frontier.size());
        Block randBlock = new Block(frontier.get(randIndex));
        chain.addBlock(randBlock);
        return randBlock;
    }

    /**
     * Model the movement of this node for a period of time and simulates channels
     */
    public void move(double time) {
        model.model(time);
        ArrayList<Channel> toRemove = new ArrayList<Channel>();
        for (Channel c: channels) {
            c.update(time);
            if (c.timedout()) {
                toRemove.add(c);
                c.tearDown();
            }
            if (c.finished()) {
                toRemove.add(c);
                c.tearDown();
            }
        }
        channels.removeAll(toRemove);
    }

    public boolean canConnect(Node b) {
        // Need to have each other in range, so take the shorter for check
        double range = Math.min(this.range, b.range);
        double xdiff = this.getXPos() - b.getXPos();
        double ydiff = this.getYPos() - b.getYPos();

        // True i xdiff^2 + ydiff^2 < range^2
        return (Math.pow(xdiff, 2) + Math.pow(ydiff, 2) < Math.pow(range, 2));
    }
}
