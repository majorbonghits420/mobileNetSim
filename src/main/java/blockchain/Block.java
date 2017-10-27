package blockchain;

import utils.Sha256;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.RuntimeException;
import java.util.Random;

public class Block {
    private Sha256 parent;
     /** We don't actually care about this. This is a random number to ensure hashes differ */
    private int data;
    private static final Random rng = new Random();

    Block(Sha256 parent) {
        this(parent, rng.nextInt());
    }

    Block(Sha256 parent, int data) {
        this.parent = parent;
        this.data = data;
    }

    public boolean isParent(Block b) {
        return parent.equals(b.parent);
    }

    public Sha256 getParent() {
        return parent;
    }

    public Sha256 getHash() {
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        bs.write(parent.getByteArray(), 0, Sha256.HASH_SIZE);
        bs.write(data);

        return Sha256.getHash(bs);
    }

    public static Block getGenisis() {
        byte[] zeroHash = new byte[32];
        return new Block(zeroHash, 0);
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
