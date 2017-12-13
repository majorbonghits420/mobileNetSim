package blockchain;

import network.Data;
import utils.Sha256;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.RuntimeException;
import java.util.Random;

/**
 * The basic Block of our Blockchain.
 * Dummy block contains no useful information, only includes a random integer and parent hash so that
 * its hash will be unique. All blocks are 100 KB big.
 */
public class Block implements Data {
    private Sha256 parent; /**< Hash of the parent of the Block */
     /** We don't actually care about this. This is a random number to ensure hashes differ */
    private int data; /**< Random value to make hash unique */
    private static final Random rng = new Random(); /**< rng used to generate random data values for new blocks */
    private static final double DATA_SIZE = 100000.0; /**< Size of every block is constant and set at 100 KB */

    /**
     * Creates a block with a given parent. Data value is random
     *
     * @param parent Sha256 hash of the parent block
     */
   public  Block(Sha256 parent) {
        this(parent, rng.nextInt());
    }

    /**
     * Creates a block with a given parent and data value
     *
     * @param parent Sha256 hash of the parent block
     * @param data Data value
     */
    public Block(Sha256 parent, int data) {
        this.parent = parent;
        this.data = data;
    }

    /**
     * Checks if a given block is the parent of this block
     *
     * @param b Block to check if parent
     * @return True is given block is the parent of this block, false otherwise
     */
    public boolean isParent(Block b) {
        return parent.equals(b.parent);
    }

    /**
     * Returns the SHA256 hash of the parent of this Block
     *
     * @return SHA256 hash of parent
     */
    public Sha256 getParent() {
        return parent;
    }

    /**
     * Returns hash of block
     *
     * @return SHA256 hash of the Block
     */
    public Sha256 getHash() {
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        bs.write(parent.getByteArray(), 0, Sha256.HASH_SIZE);
        bs.write(data);

        return Sha256.getHash(bs);
    }

    /**
     * Returns the genisis block. In our case the genisis block is a block with a parent hash
     * and data value both of 0.
     *
     * @return The genisis block
     */
    public static Block getGenisis() {
        byte[] zeroHash = new byte[32];
        return new Block(new Sha256(zeroHash), 0);
    }

    /**
     * Returns the size of the block
     *
     * @return size of block
     */
    public double getByteSize() {
        return parent.getByteSize() + DATA_SIZE;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null || !(obj instanceof Block)) {
            return false;
        }
        Block b = (Block) obj;
        return parent.equals(b.parent) && data == b.data;
    }

    @Override
    public int hashCode() {
        int hashcode = 0;
        for (byte b : parent.getByteArray()) {
            hashcode += Byte.hashCode(b);
        }
        return hashcode + data;
    }
}
