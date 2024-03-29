package main.java.core.model.interfaces;

import main.java.core.model.configuration.Configuration;
import main.java.core.model.enums.NodeType;
import main.java.core.model.enums.VariabilityClass;
import main.java.core.model.impl.AttributeImpl;
import main.java.core.model.impl.NodeIterator;

import java.util.*;


public abstract class AbstractNode implements Node {
    @SuppressWarnings("unused")
    private static final long serialVersionUID = 5776489857546412690L;
    private String nodeType;
    private String representation;
    private NodeType standardizedNodeType = NodeType.UNDEFINED;
    private List<Node> children;
    private transient Node parent;
    private List<Attribute> attributes;
    private VariabilityClass varClass = VariabilityClass.MANDATORY;
    private UUID uuid = UUID.randomUUID();
    private int startLine = -1;
    private int endLine = -1;

    public AbstractNode() {
        initializeNode();
    }

    /**
     * This method initializes all required objects.
     */
    private void initializeNode() {
        setChildren(new ArrayList<Node>());
        setAttributes(new ArrayList<Attribute>());
    }

    @Override
    public boolean isRoot() {
        if (parent == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isLeaf() {
        if (children.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Attribute> getAttributes() {
        return attributes;
    }

    @Override
    public void addAttribute(String key, @SuppressWarnings("rawtypes") Value value) {
        Optional<Attribute> attribute = attributes.stream().filter(e -> e.getAttributeKey().equals(key)).findAny();
        if (!attribute.isPresent()) {
            getAttributes().add(new AttributeImpl(key, value));
        } else {
            // adds this value as alternative for this attribute.
            attribute.get().addAttributeValue(value);
        }
    }

    @Override
    public void addAttribute(String key, @SuppressWarnings("rawtypes") List<Value> values) {
        Optional<Attribute> attribute = attributes.stream().filter(e -> e.getAttributeKey().equals(key)).findAny();
        if (!attribute.isPresent()) {
            getAttributes().add(new AttributeImpl(key, values));
        } else {
            // adds this value as alternative for this attribute.
            attribute.get().addAttributeValues(values);
        }
    }

    public void addAttribute(Attribute attr) {
        getAttributes().add(attr);
    }

    @Override
    public Attribute getAttributeForKey(String key) {
        try {
            return attributes.stream().filter(e -> e.getAttributeKey().equals(key)).findAny().get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public int getNumberOfChildren() {
        int size = 1;

        if (children.isEmpty()) {
            return size;
        } else {
            for (Node child : children) {
                size += child.getNumberOfChildren();
            }
            return size;
        }
    }

    @Override
    public List<Node> getNodesOfType(String nodeType) {
        List<Node> childrenList = new ArrayList<Node>();
        if (getNodeType().equals(nodeType)) {
            childrenList.add(this);
        }

        for (Node child : getChildren()) {
            childrenList.addAll(child.getNodesOfType(nodeType));
        }
        return childrenList;
    }

    @Override
    public List<String> getAllNodeTypes() {
        List<String> nodeTypes = new ArrayList<String>();
        nodeTypes.add(getNodeType());
        for (Node child : getChildren()) {
            nodeTypes.addAll(child.getAllNodeTypes());
        }
        return nodeTypes;
    }

    /******************************************************
     * GETTER AND SETTER
     ******************************************************/
    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    @Override
    public String getNodeType() {
        return nodeType;
    }

    @Override
    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    @Override
    public List<Node> getChildren() {
        return children;
    }

    @Override
    public void addChild(Node child) {
        this.children.add(child);
    }

    @Override
    public void sortChildNodes() {
        // sort child artifacts if not empty
        if (!getChildren().isEmpty()) {
            getChildren().sort((a, b) -> {
                if (a.getStartLine() < b.getStartLine()) {
                    return -1;
                }

                if (a.getStartLine() > b.getStartLine()) {
                    return 1;
                }
                return 0;
            });
        }
    }

    @Override
    public void addChild(Node child, int position) {
        if (position > getChildren().size()) {
            position = getChildren().size();
        }
        getChildren().add(position, child);
        child.setParent(this);
    }

    @Override
    public void addChildWithParent(Node child) {
        child.setParent(this);
        this.children.add(child);
    }

    @Override
    public void addChildWithParent(Node child, int position) {
        child.setParent(this);

        addChild(child, position);
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    @Override
    public VariabilityClass getVariabilityClass() {
        return varClass;
    }

    @Override
    public void setVariabilityClass(VariabilityClass varClass) {
        this.varClass = varClass;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    /**
     * @Override public String toString() { String nodeName = "NodeType:
     * "+getNodeType() +" \n"; for(Attribute attr : getAttributes()) {
     * nodeName += "Attrribute Key: "+ attr.getAttributeKey() + "\n";
     * for(String value : attr.getAttributeValues()) { nodeName += "
     * "+value +"\n"; } } return nodeName; }
     **/

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return representation == null ? nodeType : representation;
    }

    @Override
    public void setRepresentation(String representation) {
        this.representation = representation;
    }

    @Override
    public void setStandardizedNodeType(NodeType type) {
        standardizedNodeType = type;
    }

    @Override
    public NodeType getStandardizedNodeType() {
        return standardizedNodeType;
    }

    @Override
    public Iterable<Node> breadthFirstSearch() {
        return () -> new NodeIterator(this, true);
    }

    @Override
    public Iterable<Node> depthFirstSearch() {
        return () -> new NodeIterator(this, false);
    }

    @Override
    public int getPosition() {
        return parent.getChildren().indexOf(this);
    }

    @Override
    public void setPosition(int position) {
        parent.getChildren().remove(this);

        if (position > parent.getChildren().size()) {
            position = parent.getChildren().size();
        }

        parent.getChildren().add(position, this);
    }

    @Override
    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    @Override
    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }

    @Override
    public int getStartLine() {
        return this.startLine;
    }

    @Override
    public int getEndLine() {
        return this.endLine;
    }

    @Override
    public void addChildAtPosition(Node child, int position) {
        if (position > getChildren().size()) {
            position = getChildren().size();
        }
        getChildren().add(position, child);
        child.setParent(this);
    }

    @Override
    public void addNodeAfterwards(Node node) {
        int position = getPosition();
        if (position == parent.getChildren().size() - 1) {
            this.parent.getChildren().add(node);
        } else {
            this.parent.getChildren().add(position + 1, node);
        }
    }

    @Override
    public int numberOfOptionals() {
        return countVariabilityClassNodes(this, 0, VariabilityClass.OPTIONAL);
    }

    /**
     * Iterates over all comparisons recursively and counts optional elements.
     */
    private int countVariabilityClassNodes(Node node, int number, VariabilityClass varClass) {
        //if the node is an optional count number up
        int nodeNumber = 0;
        if (node.getVariabilityClass().equals(varClass)) {
            nodeNumber++;
        }
        //process child nodes
        for (Node childNode : node.getChildren()) {
            nodeNumber = nodeNumber + countVariabilityClassNodes(childNode, 0, varClass);
        }
        return nodeNumber;
    }

    @Override
    public int numberOfAlternatives() {
        return countVariabilityClassNodes(this, 0, VariabilityClass.ALTERNATIVE);
    }

    @Override
    public int numberOfMandatories() {
        return countVariabilityClassNodes(this, 0, VariabilityClass.MANDATORY);
    }


    @Override
    public Configuration createConfiguration() {
        return null;
    }

    @Override
    public int cut() {
        if (this.isRoot()) {
            return -1;
        }
        int index = getParent().getChildren().indexOf(this);

        getParent().getChildren().remove(index);
        return index;
    }

    @Override
    public int cutWithoutChildren() {
        if (this.isRoot()) {
            return -1;
        }
        for (Node child : children) {
            getParent().addChildWithParent(child);
        }
        return this.cut();
    }

    @Override
    public String getValueAt(int index) {
        if (attributes.size() < (index + 1)) {
            return null;
        }
        return attributes.get(index).getAttributeValues().get(0).getValue().toString();
    }

    @Override
    public void updateParent(Node parent) {
        this.cut();
        parent.addChildWithParent(this);
    }

    @Override
    public int getSize() {
        // temp starts with 1 which represents itself
        int temp = 1;
        for (Node node : this.getChildren()) {
            temp += node.getNumberOfChildren();
        }
        return temp;
    }

}
