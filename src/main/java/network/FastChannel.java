package network;

import blockchain.Block;
import network.Channel;
import node.Node;
import utils.Sha256;

import java.util.ArrayList;
import java.util.List;

/**
 * This Channel assumes that all when data is transferred the only delay is how long it
 * takes light to travel the distance between two nodes.
 */
public class FastChannel extends Channel {

    private static final double SOL = 300000000; // Approximate as 3 * 10^8
    private static final double TIMEOUT = 3.0; /**< Timeout time is 3 seconds */
    private State state;
    private double dataTransfered;
    private double timeDisconnected;
    private ArrayList<Sha256> toRequestFromA;
    private ArrayList<Sha256> toRequestFromB;
    private ArrayList<Block> toAddA;
    private ArrayList<Block> toAddB;

    public FastChannel(Node a, Node b, double bandwidth) {
        super(a, b, bandwidth);
        dataTransfered = 0.0;
        timeDisconnected = 0.0;
        toRequestFromA = new ArrayList<Sha256>();
        toRequestFromB = new ArrayList<Sha256>();
        toAddA = new ArrayList<Block>();
        toAddB = new ArrayList<Block>();
        state = State.GET_FRONTIER;
    }

    public void update(double timestep) {
        if (!a.canConnect(b)) {
            timeDisconnected += timestep;
            return;
        }
        timeDisconnected = 0.0;
        double canTransfer = bandwidth * timestep;
        switch (state) {
        case GET_FRONTIER:
            helperGetFrontier(timestep);
            break;

        case SEND_FRONTIER:
            helperSendFrontier(timestep);
            break;

        case GET_PARENTS:
            double blockSizes = sizeOfBlocks(toRequestFromB, b);
            double requestSize = toRequestFromB.size() * Sha256.HASH_SIZE;
            if (blockSizes + requestSize <= canTransfer + dataTransfered) {
                ArrayList<Sha256> toRemove = new ArrayList<Sha256>();
                ArrayList<Sha256> toAdd = new ArrayList<Sha256>();
                for (Sha256 hash : toRequestFromB) {
                    Block blk = b.getBlock(hash);
                    if (blk != null) {
                        Sha256 parentHash = blk.getParent();
                        if (a.containsBlock(parentHash)) {
                            a.addBlock(blk);
                            toRemove.add(hash);
                        } else {
                            toAddA.add(blk);
                            toAdd.add(parentHash);
                        }
                    }
                }
                toRequestFromB.addAll(toAdd);
                toRequestFromB.removeAll(toRemove);
                // Cleanup of toAddA
                ArrayList<Block> addBlocks = new ArrayList<Block>();
                for (Block blk : toAddA) {
                    if (a.containsBlock(blk.getParent())) {
                        a.addBlock(blk);
                        addBlocks.add(blk);
                    }
                }
                toAddA.removeAll(addBlocks);
                // Reset state variable
                dataTransfered = canTransfer + dataTransfered - blockSizes - requestSize;
                // If we are done getting parents, we then send any that need to be sent
                if (toRequestFromB.isEmpty()) {
                    state = State.SEND_PARENTS;
                }
            } else {
                dataTransfered += canTransfer;

            }
            break;

        case SEND_PARENTS:
            double blockSizs = sizeOfBlocks(toRequestFromA, a);
            double requestSizes = toRequestFromA.size() * Sha256.HASH_SIZE;
            if (blockSizs + requestSizes <= canTransfer + dataTransfered) {
                ArrayList<Sha256> toRemove = new ArrayList<Sha256>();
                ArrayList<Sha256> toAdd = new ArrayList<Sha256>();
                for (Sha256 hash : toRequestFromA) {
                    Block block = a.getBlock(hash);
                    Sha256 pHash = block.getParent();
                    if (b.containsBlock(pHash)) {
                        b.addBlock(block);
                        toRemove.add(hash);
                    } else {
                        toAddB.add(block);
                        toAdd.add(pHash);
                    }
                }
                toRequestFromA.addAll(toAdd);
                toRequestFromA.removeAll(toRemove);
                // Cleanup toAddB
                ArrayList<Block> addedBlocks = new ArrayList<Block>();
                for (Block blk : toAddB) {
                    if (b.containsBlock(blk.getParent())) {
                        b.addBlock(blk);
                        addedBlocks.add(blk);
                    }
                }
                toAddB.removeAll(addedBlocks);
                dataTransfered = canTransfer + dataTransfered - blockSizs - requestSizes;
                // if we are done with sending, we repeat the frontier process to search for new blocks
                if (toRequestFromA.isEmpty()) {
                    state = State.GET_FRONTIER;
                }
            } else {
                dataTransfered += canTransfer;
            }
            break;
        default:
            // Unsure yet, probably do nothing since should match on everything
            return;
        }
    }

    public boolean finished() { return false; }
    public boolean timedout() { return (timeDisconnected > TIMEOUT); }

    private void helperGetFrontier(double timestep) {
        List<Block> blocks = b.getFrontier();
        // Calculate frontier size
        double frontSize = 0.0;
        for (Block b : blocks) {
            frontSize += b.getByteSize();
        }
        double canTransfer = bandwidth * timestep;
        // Do checks for if they are contained, state transition
        if (frontSize <= canTransfer + dataTransfered) {
            // Node a checks for each block
            for (Block b : blocks) {
                // If we have the parent, add to chain
                Sha256 parentHash = b.getParent();
                if (a.containsBlock(parentHash)) {
                    a.addBlock(b);
                } else {
                    // Add parent to blocks to request, add recived blocks of ones to add later
                    toRequestFromB.add(parentHash);
                    toAddA.add(b);
                }
            }
            dataTransfered = dataTransfered + canTransfer - frontSize;
            // We have received the frontier, time to send ours
            state = State.SEND_FRONTIER;
            // We haven't finished the transfer yet
        } else {
            dataTransfered += canTransfer;
        }

    }

    private void helperSendFrontier(double timestep) {
        List<Block> toSend = a.getFrontier();
        double size = 0.0;
        for (Block b : toSend) {
            size += b.getByteSize();
        }
        double canSend = bandwidth * timestep;
        // If we have been able to send the whole frontier
        if (size <= canSend + dataTransfered) {
            for (Block b : toSend) {
                // If Node b doesn't have the block
                if (!this.b.containsBlock(b.getHash())) {
                    // Check for the parent
                    Sha256 parentHash = b.getParent();
                    if(this.b.containsBlock(parentHash)) {
                        this.b.addBlock(b);
                    } else {
                        // We will need to send the parent
                        toRequestFromA.add(parentHash);
                        toAddB.add(b);
                    }
                }
            }
            // Update our state
            dataTransfered = canSend + dataTransfered - size;
            // We have sent our frontier, now time to request any blocks we need
            state = State.GET_PARENTS;
        } else {
            dataTransfered += canSend;
        }

    }

    private double sizeOfBlocks(ArrayList<Sha256> hashes, Node n) {
        double size = 0.0;
        if (n != null) {
            for( Sha256 h : hashes) {
                Block b = n.getBlock(h);
                if (b != null) {
                    size += (n.getBlock(h)).getByteSize();
                }
            }
        }
        return size;
    }

    private enum State {
        GET_FRONTIER,
        SEND_FRONTIER,
        GET_PARENTS,
        SEND_PARENTS
    }
}
