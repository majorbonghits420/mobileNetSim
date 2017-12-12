package utils;

import network.Data;

import java.io.ByteArrayOutputStream;
import java.lang.RuntimeException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Class that represents a SHA256 hash.
 * Also includes utils for generating hashes
 */
public class Sha256 implements Data{
    public static final int HASH_SIZE = 32;

    private byte[] hash;

    /**
     * Creates a SHA256 from the given hash.
     * If the given hash is larger than 32 bytes, the hash is truncated to 32 bytes.
     *
     * @param hash A byte array containing a SHA256 hash.
     */
    public Sha256(byte[] hash) {
        this.hash = Arrays.copyOf(hash, HASH_SIZE);
    }

    public byte[] getByteArray() {
        return Arrays.copyOf(hash, HASH_SIZE);
    }

    public double getByteSize() {
        return HASH_SIZE;
    }

    public static Sha256 getHash(byte[] toHash) {
        try {
            MessageDigest msgDigest = MessageDigest.getInstance("SHA-256");
            msgDigest.update(toHash);
            return new Sha256(msgDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            // Should never happen, algo is hard coded...
            throw new RuntimeException(e);
        }
    }

    public static Sha256 getHash(ByteArrayOutputStream out) {
        return getHash(out.toByteArray());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if( obj == null || !(obj instanceof Sha256)) {
            return false;
        }
        Sha256 o = (Sha256) obj;
        return Arrays.equals(hash, o.hash);
    }

    @Override
    public String toString() {
        return Arrays.toString(hash);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(hash);

    }
}
