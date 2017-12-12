package blockchain;

import utils.Sha256;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Blockchain {

    HashMap<Sha256, Block> chain;

    public Blockchain(List<Block> blocks) {
        chain = new HashMap<Sha256, Block>();
        this.addBlocks(blocks);
    }

    public Blockchain(Block b) {
        this(Arrays.asList(b));
    }


    public void addBlock(Block b) {
        addBlocks(Arrays.asList(b));
    }

    public void addBlocks(List<Block> blocks) {
        for (Block b : blocks) {
            chain.put(b.getHash(), b);
        }
    }

    public int size() {
        return chain.size();
    }

    public List<Block> getFrontier() {
        // Collect all the parents
        ArrayList<Sha256> parentHashes = new ArrayList<Sha256>();
        for (Map.Entry<Sha256, Block> kv : chain.entrySet()) {
            parentHashes.add(kv.getValue().getParent());
        }

        // Check if each block is in the set of parents. If it isn't its a frontier node
        ArrayList<Block> frontiers = new ArrayList<Block>();
        for (Block b : chain.values()) {
            Sha256 hash = b.getHash();
            if (!(parentHashes.contains(hash))) {
                frontiers.add(b);
            }
        }
        return frontiers;
    }

    public List<Sha256> getFrontierHashes() {
        // Collect all the parents
        ArrayList<Sha256> parentHashes = new ArrayList<Sha256>();
        for (Map.Entry<Sha256, Block> kv : chain.entrySet()) {
            parentHashes.add(kv.getValue().getParent());
        }

        // Check if each block is in the set of parents. If it isn't its a frontier node
        ArrayList<Sha256> frontiers = new ArrayList<Sha256>();
        for (Block b : chain.values()) {
            Sha256 hash = b.getHash();
            if (!(parentHashes.contains(hash))) {
                frontiers.add(hash);
            }
        }
        return frontiers;
    }

    public Block getBlock(Sha256 hash) {
        // If they are asking for the genisis block
        if (hash.equals(Block.getGenisis().getParent())) {
            return Block.getGenisis();
        }
        return chain.get(hash);
    }

    /**
     * Returns true is the given block is in the chain, false else
     */
    public boolean containsBlock(Block b) {
        return containsBlock(b.getHash());
    }

    /**
     * Checks the chain for the block corresponding to the given hash
     */
    public boolean containsBlock(Sha256 hash) {
        return chain.containsKey(hash) || hash.equals(Block.getGenisis().getParent());
    }

}
