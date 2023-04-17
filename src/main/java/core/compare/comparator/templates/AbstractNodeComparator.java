package main.java.core.compare.comparator.templates;

import main.java.core.compare.comparator.interfaces.Comparator;
import main.java.core.model.interfaces.Node;

import java.io.Serializable;


public abstract class AbstractNodeComparator implements Comparator<Node>, Serializable {
    private static final long serialVersionUID = 7212002340935774949L;
    private String supportedNodeType;
    private double weight = 0f;


    public AbstractNodeComparator(String supportedType) {
        this.supportedNodeType = supportedType;
    }

    @Override
    public String getSupportedNodeType() {
        return this.supportedNodeType;
    }


    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public boolean isComparable(Node firstNode, Node secondNode) {
        return ((firstNode.getNodeType().equals(secondNode.getNodeType()) || supportedNodeType.equals(WILDCARD))
                && firstNode.getNodeType().equals(supportedNodeType)) ? true : false;
    }


}
