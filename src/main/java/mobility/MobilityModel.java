package mobility;

import node.Node;

import java.util.List;

public abstract class MobilityModel {

    public abstract void model(List<Node> nodes, double time);
}
