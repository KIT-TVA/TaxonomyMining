package main.java.core.model.interfaces;

import main.java.core.model.configuration.Configuration;
import main.java.core.model.util.TreeUtil;

import java.util.List;

public abstract class AbstractTree implements Tree {
    private static final long serialVersionUID = 7659681319811210012L;
    private Node root;
    private String treeName;
    private String fileExtension = "NONE";
    private List<Configuration> configurations;

    @Override
    public int getSize() {
        // temp starts with 1 which represents the root not itself
        int temp = 1;
        if (root == null) {
            return temp;
        }

        for (Node node : root.getChildren()) {
            temp += node.getNumberOfChildren();
        }
        return temp;
    }

    @Override
    public int getNodeDepth(Node node) {
        return TreeUtil.getNodeDepth(node);
    }

    @Override
    public String getArtifactType() {
        return root.getNodeType();
    }

    @Override
    public List<Node> getNodesForType(String nodeType) {
        return TreeUtil.getNodesForType(root, nodeType);
    }

    @Override
    public List<Node> getLeaves() {
        return TreeUtil.getAllLeaveNodes(root);
    }

    @Override
    public List<Node> getInnerNodes() {
        return TreeUtil.getAllInnerNodes(root);
    }

    @Override
    public List<Node> getPath(Node startingNode, Node targetNode) {
        // TODO Auto-generated method stub
        return null;
    }

    /***********************************************
     * GETTER AND SETTER
     */
    @Override
    public List<Configuration> getConfigurations() {
        return this.configurations;
    }

    public void setConfigurations(List<Configuration> configurations) {
        this.configurations = configurations;
    }

    @Override
    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    @Override
    public String getTreeName() {
        return this.treeName;
    }

    @Override
    public void setTreeName(String treeName) {
        this.treeName = treeName;
    }

    @Override
    public String getFileExtension() {
        return fileExtension;
    }

    @Override
    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

}
