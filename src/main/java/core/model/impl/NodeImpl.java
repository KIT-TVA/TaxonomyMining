package main.java.core.model.impl;

import main.java.core.model.enums.NodeType;
import main.java.core.model.enums.VariabilityClass;
import main.java.core.model.interfaces.AbstractNode;
import main.java.core.model.interfaces.Attribute;
import main.java.core.model.interfaces.Node;

import java.util.List;

/**
 * The concrete implementation of the Node interface.
 */
public class NodeImpl extends AbstractNode {

    @SuppressWarnings("unused")
    private static final long serialVersionUID = 2646668251637650151L;

    public NodeImpl() {
        super();
    }

    /**
     * This constructor initializes a node without a parent , e.g, the root node.
     *
     * @param nodeType is the type of the node , e.g , statement, method, class
     */
    public NodeImpl(String nodeType) {
        this();
        setNodeType(nodeType);
        setStandardizedNodeType(NodeType.fromString(nodeType));
    }

    /**
     * This constructor initializes a node without a parent , e.g, the root node.
     *
     * @param standardizedNodeType is the standardized type of the node
     */
    public NodeImpl(NodeType standardizedNodeType) {
        this();
        setNodeType(standardizedNodeType.name());
        setStandardizedNodeType(standardizedNodeType);
    }

    /**
     * This constructor initializes a node without a parent , e.g, the root node.
     *
     * @param standardizedNodeType is the standardized type of the node
     * @param nodeType             is the type of the node , e.g , statement,
     *                             method, class
     */
    public NodeImpl(NodeType standardizedNodeType, String nodeType) {
        this();
        setStandardizedNodeType(standardizedNodeType);
        setNodeType(nodeType);
    }

    /**
     * This constructor initializes a node with a given type. Also, the given parent
     * node is set, and this node is added as a child node.
     *
     * @param nodeType is the type of the node , e.g , statement, method, class
     */
    public NodeImpl(String nodeType, Node parent) {
        this(nodeType);
        setParent(parent);
        if (parent != null) {
            parent.addChildWithParent(this);
        }
    }

    /**
     * This constructor initializes a node under a given parent.
     *
     * @param standardizedNodeType is the standardized type of the node
     * @param nodeType             is the type of the node , e.g , statement,
     *                             method, class
     * @param parent               is the node to set as parent
     */
    public NodeImpl(NodeType standardizedNodeType, String nodeType, Node parent) {
        this(standardizedNodeType, nodeType);
        setParent(parent);
        if (parent != null) {
            parent.addChildWithParent(this);
        }
    }

    /**
     * This constructor initializes a node with a given type and a
     * given @VariabilityClass. Also, the given parent node is set, and this node is
     * added as a child node.
     *
     * @param nodeType is the type of the node , e.g , statement, method, class
     */
    public NodeImpl(String nodeString, Node parent, VariabilityClass varClass) {
        this(nodeString, parent);
        setVariabilityClass(varClass);
    }

    @Override
    public Node cloneNode() {
        Node newNode = new NodeImpl();
        newNode.setNodeType(this.getNodeType());
        newNode.setStandardizedNodeType(this.getStandardizedNodeType());

        newNode.setVariabilityClass(getVariabilityClass());


        List<Attribute> attributes = getAttributes();
        for (Attribute attribute : attributes) {
            Attribute newAttribute = new AttributeImpl(attribute.getAttributeKey(), attribute.getAttributeValues());
            newNode.getAttributes().add(newAttribute);
        }

        List<Node> children = getChildren();
        for (Node child : children) {
            newNode.addChildWithParent(child.cloneNode());
        }

        return newNode;
    }

    @Override
    public void addAttribute(String attributeKey, String value) {
        this.addAttribute(new AttributeImpl(attributeKey, new StringValueImpl(value)));

    }

}
