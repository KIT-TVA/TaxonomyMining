package main.java.core.model.interfaces;

import main.java.core.model.configuration.Configuration;

import java.io.Serializable;
import java.util.List;

public interface Tree extends Serializable {
    public List<Configuration> getConfigurations();

    /**
     * @return the root node of this tree.
     */
    public Node getRoot();

    /**
     * @return set the root node of this tree.
     */
    public void setRoot(Node node);


    /**
     * This method returns the number of nodes that are contained in this tree.
     */
    public int getSize();

    /**
     * @return the maximal height of this tree
     */
    public int getNodeDepth(Node node);

    /**
     * This method returns the tree name. Normally, it is the file name, class name, folder name.
     */
    public String getTreeName();

    /**
     * This method set the tree name;
     */
    public void setTreeName(String name);

    /**
     * This method returns the artifactType of root node ,e.g, file , folder, class.
     *
     * @return
     */
    public String getArtifactType();

    /**
     * Returns the fileExtension of the parsed file.
     *
     * @return
     */
    public String getFileExtension();

    public void setFileExtension(String extension);

    /**
     * This method returns all nodes of this tree that have the given type.
     */
    public List<Node> getNodesForType(String nodeType);

    /**
     * This method returns all leaf Nodes of this tree.
     *
     * @return a list of leaf Node.
     */
    public List<Node> getLeaves();

    /**
     * This method returns all non leaf Nodes of this tree.
     *
     * @return a list of all inner Nodes.
     */
    public List<Node> getInnerNodes();

    /**
     * This method returns a shortes path between the starting Node and the targetNode. The path is stored in a list ordered by steps.
     * Moreover, the first Node in this list is the startingNode and the last element is the targetNode.
     */
    public List<Node> getPath(Node startingNode, Node targetNode);

    public Tree cloneTree();


}
