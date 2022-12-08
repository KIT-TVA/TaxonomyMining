package tva.kastel.kit.core.compare.comparator.templates;

import java.io.Serializable;

import tva.kastel.kit.core.compare.comparator.interfaces.Comparator;
import tva.kastel.kit.core.model.interfaces.Node;

/**
 * This class serves as template for node comparator
 *
 * @author Kamil Rosiak
 */
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
