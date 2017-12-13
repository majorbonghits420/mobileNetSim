package blockchain;

import utils.Sha256;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A Blockchain with simple adders and getters.
 * Used to store dummy blocks, has limited functionality as only meant to store blocks
 */
public class Blockchain {

    HashMap<Sha256, Block> chain; /**< Hashmap used to store Blocks. <k, V> = <SHA256(blk), blk>*/

    /**
     * Creates a blockchain with the given blocks added
     *
     * @param blocks Block sto be added to the blockchain
     */
    public Blockchain(List<Block> blocks) {
        chain = new HashMap<Sha256, Block>();
        this.addBlocks(blocks);
    }

    /**
     * Creates a blockchain with the given block added
     *
     * @param b Block to be added to the blockchain
     */
    public Blockchain(Block b) {
        this(Arrays.asList(b));
    }

    /**
     * Adds given block to the blockchain
     *
     * @param b Block to add to the blockchain
     */
    public void addBlock(Block b) {
        addBlocks(Arrays.asList(b));
    }

    /**
     * Adds the given blocks to the blockchain
     *
     * @param blocks a list of Blocks
     */
    public void addBlocks(List<Block> blocks) {
        for (Block b : blocks) {
            chain.put(b.getHash(), b);
        }
    }

    /**
     * Returns the number of blocks in the blockchain
     *
     * @return number of blocks in the blockchain
     */
    public int size() {
        return chain.size();
    }

    /**
     * Gets all the Blocks that are on the frontier of the blockchain
     *
     * @return List of Blocks
     */
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

    /**
     * Gets the SHA256 hashes of all frontier Blocks
     *
     * @return list of SHA256 hashes
     */
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

    /**
     * Returns a block with the given hash
     *
     * @param hash of the Block to retrieve
     * @return Block with the given hash, or the genisis block if requesting the parent of the genisis block
     */
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
     *
     * @return true is block with given hash is in the chain, or if hash is parent hash of genisis block, false otherwise
     */
    public boolean containsBlock(Sha256 hash) {
        return chain.containsKey(hash) || hash.equals(Block.getGenisis().getParent());
    }

}
