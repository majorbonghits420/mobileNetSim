import org.junit.Test;
import static org.junit.Assert.*;

import blockchain.Block;
import mobility.StaticPoint;
import network.FastChannel;
import node.Node;

import java.util.ArrayList;

public class FastChannelTest {

    @Test
    public void testTransfer() {
        StaticPoint p1 = new StaticPoint(10, 10);
        StaticPoint p2 = new StaticPoint(0, 0);
        double bw = 1000000; // bandwidth 1 MB/sec
        double range = 50;
        Node a = new Node(p1, bw, range);
        Node b = new Node(p2, bw, range);


        // Add nodes to a and b
        ArrayList<Block> allBlocks = new ArrayList<Block>();
        for (int i = 0; i < 10; i++) {
            allBlocks.add(a.generateBlock());
            allBlocks.add(b.generateBlock());
        }

        FastChannel channel = new FastChannel(a, b, bw);
        a.addChannel(channel);

        // Update Node a for plenty of time
        // NOTE: only Node a needs to be updates because it contains the channel
        double timestep = 1.0;
        for (int i = 0; i < 170; i++) {
            a.move(timestep);
        }

        // Check to make sure that both nodes have all of the blocks that have been created
        for (Block blk : allBlocks) {
            assertTrue("Node a should contain block", a.containsBlock(blk));
            assertTrue("Node b should contain block", b.containsBlock(blk));
        }
    }

    @Test
    public void testTransferNewBlock() {
        StaticPoint p1 = new StaticPoint(10, 10);
        StaticPoint p2 = new StaticPoint(0, 0);
        double bw = 100000; // 100 KB/sec

        Node a = new Node(p1, bw, 50);
        Node b = new Node(p2, bw, 40);

        FastChannel channel = new FastChannel(a, b, bw);
        a.addChannel(channel);

        double totalTime = 50;
        double timestep = 0.25;
        double time = 0.0;
        Block newBlock = a.generateBlock();
        while (time < totalTime) {
            a.move(timestep);
            assertTrue("A and B have the same number of blocks", a.numBlocks() >= b.numBlocks());
            if (b.containsBlock(newBlock)) {
                System.out.println("New block successfully sent to Node b after " + time + " seconds");
                newBlock = a.generateBlock();
                System.out.println("Generate new block with hash" + newBlock.getHash());
            }
            time += timestep;
            System.out.println(time);
        }
        assertTrue("Node a should have more than the starting blocks", a.numBlocks() > 2);
        System.out.println("We exited the while loop");
        assertTrue(a.numBlocks() == b.numBlocks() || a.numBlocks() == b.numBlocks() + 1);
    }
}
