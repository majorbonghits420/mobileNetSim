import org.junit.Test;
import static org.junit.Assert.*;

import blockchain.Block;
import mobility.StaticPoint;
import network.FastChannel;
import node.Node;
import node.PassiveNode;

public class PassiveNodeTest {

    @Test
    public void testTransfer() {
        // Create an active and passive node
        StaticPoint pt1 = new StaticPoint(0, 0);
        StaticPoint pt2 = new StaticPoint(15, 10);
        double bw = 500000; // bandwidth 500 KB/sec
        double range = 50;
        Node active = new Node(pt1, bw, range);
        PassiveNode pass = new PassiveNode(pt2, bw, range);

        // Generate some blocks in both nodes
        for (int i = 0; i < 3; i++) {
            active.generateBlock();
            // Need to manually create block since passive doesnt actually generate one
            pass.addBlock(new Block(Block.getGenisis().getHash()));
        }

        // Setup a channel between the two
        FastChannel chan = new FastChannel(active, pass, bw);
        active.addChannel(chan);

        // Update the channel for awhile
        double t = 1.0;
        for (int i = 0; i < 180; i++) {
            active.move(t);
        }

        // Check to make sure that the size of b grew, but not the size of a
        assertTrue("Node b should have more blocks", active.numBlocks() < pass.numBlocks());
        assertTrue("Node a should only have the blocks we gave it", active.numBlocks() == 4);
    }

}
