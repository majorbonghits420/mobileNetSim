import org.junit.Test;
import static org.junit.Assert.*;

import blockchain.Block;
import mobility.RandomWalk;
import node.Node;

public class NodeTest {
    @Test
    public void testConstructor() {
        RandomWalk m = new RandomWalk();
        double range = 50;
        double bw = 100000;

        Node n = new Node(m, bw, range);
        Block gen = Block.getGenisis();
        assertTrue("Node should have genisis block", n.containsBlock(gen));
        assertTrue("Range should be set", n.getRange() == range);
    }


}
