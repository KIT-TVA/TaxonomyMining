package compare.comparator.templates;

import java.io.Serializable;

import compare.comparator.interfaces.Comparator;
import model.interfaces.Node;

/**
 * This class serves as template for node comparator
 * @author Kamil Rosiak
 *
 */
public abstract class AbstractNodeComparator implements Comparator<Node>, Serializable {
	private static final long serialVersionUID = 7212002340935774949L;
	private String supportedNodeType;
	private Float weight = 0f;


	public AbstractNodeComparator(String supportedType) {
		this.supportedNodeType = supportedType;
	}

	@Override
	public String getSupportedNodeType() {
		return this.supportedNodeType;
	}
	
	
	@Override
	public Float getWeight() {
		return this.weight;
	}
	
	@Override
	public void setWeight(Float weight) {
		this.weight = weight;
	}

	@Override
	public boolean isComparable(Node firstNode, Node secondNode) {
		return ((firstNode.getNodeType().equals(secondNode.getNodeType()) || supportedNodeType.equals(WILDCARD))
				&& firstNode.getNodeType().equals(supportedNodeType)) ? true : false;
	}



}
