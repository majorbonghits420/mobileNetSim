package node;

import blockchain.Block;
import mobility.MobilityModel;
import node.Node;
import utils.Sha256;

import java.util.ArrayList;
import java.util.List;

/**
 * A PassiveNode is a Node that collect blocks but does not send them.
 */
public class PassiveNode extends Node {

    public PassiveNode(MobilityModel m, double maxBandwidth) {
        super(m, maxBandwidth, 0.0);
    }

    public PassiveNode(MobilityModel m, double maxBandwidth, double range) {
        super(m, maxBandwidth, range);
    }

    @Override
    public double getFrontierSize() {
        return 0.0;
    }

    @Override
    public List<Block> getFrontier() {
        return new ArrayList<Block>();
    }

    @Override
    public Block getBlock(Sha256 hash) {
        return null;
    }

    @Override
    public Block generateBlock() {
        return null;
    }
}
