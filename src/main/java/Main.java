import blockchain.Block;
import mobility.StaticPoint;
import mobility.RandomWalk;
import network.FastChannel;
import node.Node;
import node.PassiveNode;
import simulators.SpreadSim;

import java.util.ArrayList;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class Main {

    public static void main(String[] args) {
        passiveImpact();
    }

    public static void passiveImpact() {
        double bw = 100000; // 100 KB/s
        double maxNodeBw = 1000000; // 1 MB/s
        int numNodes = 50;
        double range = 70;

        for (double percentPassive = 0.1; percentPassive <= 0.9; percentPassive += 0.1) {
            // Output the percentage of passive nodes
            System.out.print(percentPassive);
            ArrayList<Node> nodes = new ArrayList<Node>();
            int numPass = (int) (percentPassive * numNodes);
            for (int i = 0; i < numNodes; i++) {
                RandomWalk m = new RandomWalk();
                for (int t = 0; t < 120; t++) {
                    m.model(1.0);
                }
                // Create a passive node
                if (i < numPass) {
                    PassiveNode p = new PassiveNode(m, maxNodeBw, range);
                    nodes.add(p);
                    // Create an active node
                } else {

                    Node n = new Node(m, maxNodeBw, range);
                    nodes.add(n);
                }
            }

            // Create our simulation
            double simStep = 0.5; // 0.5 seconds
            SpreadSim sim = new SpreadSim(simStep, nodes, bw);

            // Run the simulation
            sim.run(3600); // Run the simulation for 1 hour
            System.out.println();
        }
    }

    public static void runTrustedNodesRangeTest() {
        double bw = 100000; // 100 KB/s
        double maxNodeBw = 1000000; // 1 MB/s
        for (int range = 10; range <= 100; range += 10) {
            System.out.print(range + ", ");
            ArrayList<Node> nodes = new ArrayList<Node>();
            // 100 nodes in our simulation
            int numNodes = 50;
            for (int i = 0; i < numNodes; i++) {
                RandomWalk m = new RandomWalk();
                for (int t = 0; t < 120; t++) {
                    m.model(1.0);
                }
                Node n = new Node(m, maxNodeBw, range);
                nodes.add(n);
            }

            // Create the simulation
            double simStep = 0.5; // 0.5 seconds
            SpreadSim sim = new SpreadSim(simStep, nodes, bw);

            // Run the simulation
            sim.run(3600); // Run the simulation for 1 hours
            System.out.println();
        }
    }
}
